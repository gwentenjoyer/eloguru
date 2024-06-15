package ua.thecompany.eloguru.services;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import ua.thecompany.eloguru.dto.AccountDto;
import ua.thecompany.eloguru.dto.InitDto.AccountInitDto;
import ua.thecompany.eloguru.dto.StudentDto;
import ua.thecompany.eloguru.dto.TeacherDto;
import ua.thecompany.eloguru.model.Account;

import java.util.List;

@Service
public interface AccountService {
    void saveAccount(AccountInitDto accountInitDTO);

    List<AccountDto> getAccounts();

    AccountDto getAccountById(Long id);

    Account updateAccount(AccountInitDto accountInitDto);
    Account updateAccount(AccountInitDto accountInitDto, Long accountId);


    void deleteAccountById(Long id);

    String getUserRole(String email);

    boolean validateIdByEmail(String email, Long id);

    Long getIdByEmail(String email) throws EntityNotFoundException;

    TeacherDto getTeacherByAccountId(Long id);
    StudentDto getStudentByAccountId(Long id);

    TeacherDto getTeacherById(Long id);
    StudentDto getStudentById(Long id);

    AccountDto getAccountByEmail(String email);

}
