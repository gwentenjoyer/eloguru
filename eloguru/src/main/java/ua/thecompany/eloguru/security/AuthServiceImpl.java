package ua.thecompany.eloguru.security;

import jakarta.mail.AuthenticationFailedException;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AuthorizationServiceException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import ua.thecompany.eloguru.dto.InitDto.AccountInitDto;
import ua.thecompany.eloguru.dto.LoginRequest;
import ua.thecompany.eloguru.email.EmailService;
import ua.thecompany.eloguru.mappers.AccountMapper;
import ua.thecompany.eloguru.model.*;
import ua.thecompany.eloguru.repositories.AccountRepository;
import ua.thecompany.eloguru.repositories.RefreshTokenRepository;
import ua.thecompany.eloguru.repositories.TokenRepository;
import ua.thecompany.eloguru.services.AccountService;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final AccountRepository accountRepository;
    private final TokenRepository tokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtServiceImpl jwtService;
    private final AuthenticationManager authenticationManager;
    private final AccountMapper accountMapper;
    private final RefreshTokenRepository refreshTokenRepository;
    private final EmailService emailService;
    private final AccountService accountService;

    @Override
    public Account register(AccountInitDto request, EnumeratedRole role) throws MessagingException {
        if(accountRepository.existsByEmail(request.email())){
            throw new MessagingException("User with this email is already exist");
        }
        String token = UUID.randomUUID().toString();
        Account account = accountMapper.accountInitDtoToAccountModel(request);
        account.setPwhash(passwordEncoder.encode(request.password()));
        account.setRole(role);
        account.setActive(false);
        account.setActivationCode(token);
        var savedUser = accountRepository.save(account);
        var jwtToken = jwtService.generateToken(account);
        var refreshToken = jwtService.generateRefreshToken(account);
        saveRefreshToken(account, refreshToken);
        saveUserToken(savedUser, jwtToken);
        emailService.sendEmail(request.email(), "Account registration", token);

        return savedUser;
    }

    @Transactional
    public String activate(String activationCode) {
        var user = accountRepository.findByActivationCode(activationCode)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found!"));
        return user.getEmail();
    }

    private void saveRefreshToken(Account account, String refreshToken) {
        var token = RefreshToken.builder()
                .account(account)
                .RefreshToken(refreshToken)
                .tokenType(TokenType.BEARER)
                .build();
        refreshTokenRepository.save(token);
    }

    @Override
    @Transactional
    public void authenticate(LoginRequest request, HttpServletResponse response) throws MessagingException, AuthorizationServiceException {
        var user = accountRepository.findByEmail(request.email())
                .orElseThrow(() -> new MessagingException("Account not found!"));
        if(!user.isActive()){
            throw new MessagingException("Please verify account or contact administrator");
        }
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.email(),
                            request.password()
                    )
            );
        }
        catch (BadCredentialsException e){
            throw new AuthorizationServiceException("Invalid password!");
        }



        updateAllTokens(user, response);
    }

    private void updateRefreshToken(Account user, String refreshToken) {
        refreshTokenRepository.updateRefreshTokenById(user.getId(), refreshToken);
    }

    @Transactional
    public void updateAllTokens(Account user, HttpServletResponse response) {
        var jwtToken = jwtService.generateToken(user);
        var refreshToken = jwtService.generateRefreshToken(user);
        updateToken(user, jwtToken);
        jwtService.setJwtCookies(response, jwtToken, refreshToken);
        updateRefreshToken(user, refreshToken);
        refreshTokenRepository.updateRefreshTokenById(user.getId(), refreshToken);
        jwtService.setJwtCookies(response, jwtToken);
    }


    @Transactional
    public String refreshToken(HttpServletRequest request, HttpServletResponse response) {
        var refreshToken = jwtService.getRefreshTokenFromCookie(request);
        if (refreshToken != null && jwtService.isTokenGetSign(refreshToken) && refreshTokenRepository.findRefreshTokenByRefreshToken(refreshToken)) {
            var user = accountRepository.findByEmail(jwtService.extractUsername(refreshToken))
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found!"));
            var jwtToken = jwtService.generateToken(user);
            var newRefreshToken = jwtService.generateRefreshToken(user);
            updateToken(user, jwtToken);
            updateRefreshToken(user, newRefreshToken);
            jwtService.setJwtCookies(response, jwtToken, newRefreshToken);
            return "new Tokens";
        } else {
            return null;
        }
    }

    @Transactional
    public void accountPostregister(String activationCode, AccountInitDto accountInitDto, HttpServletResponse response) {
        var user = accountRepository.findByActivationCode(activationCode)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found!"));
        user.setActivationCode(null);
        user.setActive(true);
        accountService.updateAccount(accountInitDto, user.getId(), response);
        updateAllTokens(user, response);
//        var jwtToken = jwtService.generateToken(user);
//        updateToken(user, jwtToken);
//        var refreshToken = jwtService.generateRefreshToken(user);
//        refreshTokenRepository.updateRefreshTokenById(user.getId(), refreshToken);
//        jwtService.setJwtCookies(response, jwtToken);
    }

    @Override
    @Transactional
    public void logout(HttpServletResponse response) {
        jwtService.deleteJwtCookies(response);
    }

    private void updateToken(Account user, String token) {
        tokenRepository.updateTokenById(user.getId(), token);
    }

    private void saveUserToken(Account user, String jwtToken) {
        var token = Token.builder()
                .account(user)
                .token(jwtToken)
                .build();
        tokenRepository.save(token);
    }

}
