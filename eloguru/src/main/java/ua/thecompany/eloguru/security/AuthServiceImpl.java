package ua.thecompany.eloguru.security;

import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
        Account account = accountMapper.accountInitDtoToAccountModel(request);
        account.setPwhash(passwordEncoder.encode(request.password()));
        account.setRole(role);
        account.setActive(false);

        var savedUser = accountRepository.save(account);
        var jwtToken = jwtService.generateToken(account);
        var refreshToken = jwtService.generateRefreshToken(account);
        saveRefreshToken(account, refreshToken);
        saveUserToken(savedUser, jwtToken);
        emailService.sendEmail(request.email(), "Account registration", UUID.randomUUID().toString());

        return savedUser;
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
    public void authenticate(LoginRequest request, HttpServletResponse response) throws MessagingException {
        var user = accountRepository.findByEmail(request.email())
                .orElseThrow(() -> new MessagingException("Account not found!"));
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.email(),
                            request.password()
                    )
            );
        }catch (BadCredentialsException e){
            throw new MessagingException("Invalid password!");
        }
        if(!user.isActive()){
            throw new MessagingException("Please verify account or contact administrator");
        }
        var jwtToken = jwtService.generateToken(user);
        updateToken(user, jwtToken);
        var refreshToken = jwtService.generateRefreshToken(user);
        refreshTokenRepository.updateRefreshTokenById(user.getId(), refreshToken);
        jwtService.setJwtCookies(response, jwtToken);
//        return ResponseEntity.ok(new String("Authentication successful"));
    }

//    @Transactional
//    public void activate(String activationCode) {
//        var user = accountRepository.findByActivationCode(activationCode)
//                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found!"));
//
////        return AuthenticationResponse.builder().message("User found").build();
//    }

    @Transactional
    public void accountPostregister(String activationCode, AccountInitDto accountInitDto) {
        var user = accountRepository.findByActivationCode(activationCode)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found!"));
        user.setActivationCode(null);
        user.setActive(true);

        accountService.updateAccount(accountInitDto, user.getId());
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
