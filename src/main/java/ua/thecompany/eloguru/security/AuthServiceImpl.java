package ua.thecompany.eloguru.security;

import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.thecompany.eloguru.dto.InitDto.AccountInitDto;
import ua.thecompany.eloguru.dto.LoginRequest;
import ua.thecompany.eloguru.mappers.AccountMapper;
import ua.thecompany.eloguru.model.Account;
import ua.thecompany.eloguru.model.EnumeratedRole;
import ua.thecompany.eloguru.model.Token;
import ua.thecompany.eloguru.repositories.AccountRepository;
import ua.thecompany.eloguru.repositories.TokenRepository;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final AccountRepository accountRepository;
    private final TokenRepository tokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtServiceImpl jwtService;
    private final AuthenticationManager authenticationManager;
    private final AccountMapper accountMapper;

    @Override
    public Account register(AccountInitDto request, EnumeratedRole role) throws MessagingException {
        if(accountRepository.existsByEmail(request.email())){
            throw new MessagingException("User with this email is already exist");
        }
        Account account = accountMapper.accountInitDtoToAccountModel(request);
        account.setPwhash(passwordEncoder.encode(request.password()));
        account.setRole(role);

        var savedUser = accountRepository.save(account);
        var jwtToken = jwtService.generateToken(account);
        saveUserToken(savedUser, jwtToken);

        return savedUser;
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
            throw new MessagingException("Please verify account");
        }
        var jwtToken = jwtService.generateToken(user);
        updateToken(user, jwtToken);
        jwtService.setJwtCookies(response, jwtToken);
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
