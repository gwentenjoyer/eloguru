package ua.thecompany.eloguru.services.servicesImpl;

import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.thecompany.eloguru.dto.AccountDto;
import ua.thecompany.eloguru.dto.InitDto.AccountInitDto;
import ua.thecompany.eloguru.dto.StudentDto;
import ua.thecompany.eloguru.dto.TeacherDto;
import ua.thecompany.eloguru.mappers.AccountMapper;
import ua.thecompany.eloguru.model.Account;
import ua.thecompany.eloguru.repositories.AccountRepository;
import ua.thecompany.eloguru.repositories.StudentRepository;
import ua.thecompany.eloguru.repositories.TeacherRepository;
import ua.thecompany.eloguru.services.AccountService;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
@AllArgsConstructor
public class AccountServiceImpl implements AccountService {

    private AccountRepository accountRepository;
    private TeacherRepository teacherRepository;
    private StudentRepository studentRepository;

    private AccountMapper accountMapper;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public void saveAccount(AccountInitDto accountInitDTO) {
        accountRepository.save(accountMapper.accountInitDtoToAccountModel(accountInitDTO));
        log.info("Account with name " + accountInitDTO.email() + " successfully registered.");
    }

    @Override
    @Transactional
//    @Cacheable
    public List<AccountDto> getAccounts() {
        log.info("Retrieving all accounts");
        return accountRepository.findByActive(true).stream().map(entity -> accountMapper.accountModelToAccountDto(entity)).collect(Collectors.toList());

    }


    @Override
    @Transactional
//    @Cacheable
    public AccountDto getAccountById(Long id) throws EntityNotFoundException {
        log.info("Getting account by id: " + id);
        return accountMapper.accountModelToAccountDto(accountRepository.findByIdAndActive(id, true)
                .orElseThrow(() -> new EntityNotFoundException("Could not found by id: "+ id)));
    }


    @Override
    @Transactional
//    @CachePut
    public Account updateAccount(AccountInitDto accountInitDto) {
        Optional<Account> optionalAccount = accountRepository.findByEmail(accountInitDto.email());
        if (!optionalAccount.isPresent()) throw new EntityNotFoundException("Can't find account to update");
        Account account = optionalAccount.get();
        return accountRepository.save(updateAccountFromAccountInitDto(accountInitDto, account));
    }

    private Account updateAccountFromAccountInitDto(AccountInitDto accountInitDto, Account account) {
        account.setEmail(accountInitDto.email());
        account.setPhone(accountInitDto.phone());
        account.setPwhash(passwordEncoder.encode(accountInitDto.password()));
        if (accountInitDto.country() != null)
            account.setCountry(accountInitDto.country());
        if (accountInitDto.fullname() != null)
            account.setFullname(accountInitDto.fullname());
        if (accountInitDto.password() != null)
            account.setPwhash(passwordEncoder.encode(accountInitDto.password()));
        return account;
    }

    @Override
    @Transactional
//    @CacheEvict
    public void deleteAccountById(Long id) {
        accountRepository.deleteById(id);
        log.info("Account with id " + id + " successfully DELETED.");
    }

    @Override
    @Transactional
    public String getUserRole(String email) {
        Optional<Account> userData = accountRepository.findByEmail(email);
        return userData.get().getRole().toString();
    }

    @Override
    @Transactional
    public boolean validateIdByEmail(String email, Long id) {
        Optional<Account> optionalAccount = accountRepository.findByEmail(email);
        if (optionalAccount.isPresent()) {
            return id == optionalAccount.get().getId();
        }
        return false;
    }

    @Override
    @Transactional
    public Long getIdByEmail(String email) throws EntityNotFoundException {
        return accountRepository.findByEmail(email).orElseThrow(() -> new EntityNotFoundException("")).getId();
    }



//    public TeacherDto getTeacherByAccountId(Long id){
//        return teacherRepository.findByIdAndActive(id, true).stream().map(entity -> accountMapper.accountModelToAccountDto(entity)).collect(Collectors.toList());
//
//    }
//
//    public StudentDto getStudentByAccountId(Long id){
//
//    }
    @Override
    @Transactional
    public TeacherDto getTeacherByAccountId(Long id){
        return accountMapper.teacherModelToTeacherDto(teacherRepository.findByAccountIdAndActive(id, true)
                .orElseThrow(() -> new EntityNotFoundException("Could not found by id: "+ id)));
    }
    @Override
    @Transactional
    public StudentDto getStudentByAccountId(Long id){
        return accountMapper.studentModelToStudentDto(studentRepository.findByAccountIdAndActive(id, true)
                .orElseThrow(() -> new EntityNotFoundException("Could not found by id: "+ id)));
    }

    @Override
    @Transactional
    public TeacherDto getTeacherById(Long id){
        return accountMapper.teacherModelToTeacherDto(teacherRepository.findByIdAndActive(id, true)
                .orElseThrow(() -> new EntityNotFoundException("Could not found by id: "+ id)));
    }
    @Override
    @Transactional
    public StudentDto getStudentById(Long id){
        return accountMapper.studentModelToStudentDto(studentRepository.findByIdAndActive(id, true)
                .orElseThrow(() -> new EntityNotFoundException("Could not found by id: "+ id)));
    }
}
