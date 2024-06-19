package ua.thecompany.eloguru.security;

import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletResponse;
import ua.thecompany.eloguru.dto.InitDto.AccountInitDto;
import ua.thecompany.eloguru.dto.LoginRequest;
import ua.thecompany.eloguru.model.Account;
import ua.thecompany.eloguru.model.EnumeratedRole;

public interface AuthService {
    Account register(AccountInitDto request, EnumeratedRole role) throws MessagingException;
    void authenticate(LoginRequest request, HttpServletResponse response)  throws MessagingException;
    void logout(HttpServletResponse response);
    void updateAllTokens(Account user, HttpServletResponse response);
}
