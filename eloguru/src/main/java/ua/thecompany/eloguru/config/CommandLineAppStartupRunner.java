package ua.thecompany.eloguru.config;

import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import ua.thecompany.eloguru.dto.InitDto.AccountInitDto;
import ua.thecompany.eloguru.model.EnumeratedRole;
import ua.thecompany.eloguru.repositories.AccountRepository;
import ua.thecompany.eloguru.security.AuthService;

@Component
public class CommandLineAppStartupRunner implements CommandLineRunner {
    @Autowired
    AccountRepository accountRepository;

    @Autowired
    AuthService authService;

    @Override
    public void run(String...args) throws Exception {
        try {
            AccountInitDto admin = new AccountInitDto("root@mail.com", "root", "0", null, null);
            authService.register(admin, EnumeratedRole.ADMIN);
        }
        catch (MessagingException e) {
            System.out.println("Admin's account already exists.");
        }
    }
}
