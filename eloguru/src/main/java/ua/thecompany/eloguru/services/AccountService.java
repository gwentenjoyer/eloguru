package ua.thecompany.eloguru.services;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Service;
import ua.thecompany.eloguru.dto.AccountDto;
import ua.thecompany.eloguru.dto.InitDto.AccountInitDto;
import ua.thecompany.eloguru.dto.StudentDto;
import ua.thecompany.eloguru.dto.TeacherDto;
import ua.thecompany.eloguru.model.Account;

import java.util.ArrayList;
import java.util.List;

@Service
public interface AccountService {
    void saveAccount(AccountInitDto accountInitDTO);

    Page<AccountDto> getAccounts(@PageableDefault Pageable pageable);

    AccountDto getAccountById(Long id);

    Account updateAccount(AccountInitDto accountInitDto);
    Account updateAccount(AccountInitDto accountInitDto, Long accountId);
    void updateAccountRoleById(Long accountId, Boolean statusActive);


    void deleteAccountById(Long id);
    void forceDeleteAccountById(Long id);


    String getUserRole(String email);

    boolean validateIdByEmail(String email, Long id);

    Long getIdByEmail(String email) throws EntityNotFoundException;

    TeacherDto getTeacherByAccountId(Long id);
    StudentDto getStudentByAccountId(Long id);

    TeacherDto getTeacherById(Long id);
    StudentDto getStudentById(Long id);

    AccountDto getAccountByEmail(String email);

}
