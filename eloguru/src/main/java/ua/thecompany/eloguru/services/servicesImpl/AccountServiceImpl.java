package ua.thecompany.eloguru.services.servicesImpl;

import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.thecompany.eloguru.dto.AccountDto;
import ua.thecompany.eloguru.dto.InitDto.AccountInitDto;
import ua.thecompany.eloguru.dto.LoginRequest;
import ua.thecompany.eloguru.dto.StudentDto;
import ua.thecompany.eloguru.dto.TeacherDto;
import ua.thecompany.eloguru.mappers.AccountMapper;
import ua.thecompany.eloguru.mappers.StudentMapper;
import ua.thecompany.eloguru.mappers.TeacherMapper;
import ua.thecompany.eloguru.model.Account;
import ua.thecompany.eloguru.repositories.*;
import ua.thecompany.eloguru.security.AuthService;
import ua.thecompany.eloguru.security.JwtServiceImpl;
import ua.thecompany.eloguru.services.AccountService;

import java.util.ArrayList;
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

    private TeacherMapper teacherMapper;
    private StudentMapper studentMapper;
    private final JwtServiceImpl jwtService;
    private final TokenRepository tokenRepository;
    private final RefreshTokenRepository refreshTokenRepository;


    @Override
    @Transactional
    public void saveAccount(AccountInitDto accountInitDTO) {
        accountRepository.save(accountMapper.accountInitDtoToAccountModel(accountInitDTO));
        log.info("Account with name " + accountInitDTO.email() + " successfully registered.");
    }

    @Override
    @Transactional
//    @Cacheable
    public Page<AccountDto> getAccounts(@PageableDefault Pageable pageable) {
        log.info("Retrieving all accounts");
        return accountRepository.findAll(pageable).map(accountMapper::accountModelToAccountDto);
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
        if (optionalAccount.isEmpty()) throw new EntityNotFoundException("Can't find account to update");
        Account account = optionalAccount.get();
        return accountRepository.save(updateAccountFromAccountInitDto(accountInitDto, account));
    }

    @Override
    @Transactional
//    @CachePut
    public Account updateAccount(AccountInitDto accountInitDto, Long accountId, HttpServletResponse response) {
        Optional<Account> optionalAccount = accountRepository.findById(accountId);
        if (optionalAccount.isEmpty()) throw new EntityNotFoundException("Can't find account to update");
        Account account = optionalAccount.get();
        String oldEmail = account.getEmail();
        Account updatedAccount = updateAccountFromAccountInitDto(accountInitDto, account);
        if (!updatedAccount.getEmail().equals(oldEmail)) {
            updateAllTokens(updatedAccount, response);
        }
        return accountRepository.save(updatedAccount);
    }

    private Account updateAccountFromAccountInitDto(AccountInitDto accountInitDto, Account account) {
        Optional<Account> optionalAccountNewEmail = accountRepository.findByEmail(accountInitDto.email());
        if ((accountInitDto.email() != null) && (optionalAccountNewEmail.isEmpty()))
            account.setEmail(accountInitDto.email());
        if (accountInitDto.phone() != null)
            account.setPhone(accountInitDto.phone());
//        account.setPwhash(passwordEncoder.encode(accountInitDto.password()));
        if (accountInitDto.country() != null)
            account.setCountry(accountInitDto.country());
        if (accountInitDto.fullname() != null)
            account.setFullname(accountInitDto.fullname());
        if (accountInitDto.password() != null)
            account.setPwhash(passwordEncoder.encode(accountInitDto.password()));
        return account;
    }

    public void updateAccountRoleById(Long accountId, Boolean statusActive){
        Optional<Account> optionalAccount = accountRepository.findById(accountId);
        if (optionalAccount.isEmpty()) throw new EntityNotFoundException("Can't find account to update");
        Account account = optionalAccount.get();
        account.setActive(statusActive);
        accountRepository.save( account);
        log.info("Account with id " + accountId + " has updated status to " + statusActive);
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
//    @CacheEvict
    public void forceDeleteAccountById(Long id) {
        accountRepository.forceDeleteById(id);
        log.info("Account with id " + id + " fully successfully DELETED.");
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
        return teacherMapper.teacherModelToTeacherDto(teacherRepository.findByAccountIdAndActive(id, true)
                .orElseThrow(() -> new EntityNotFoundException("Could not found by id: "+ id)));
    }
    @Override
    @Transactional
    public StudentDto getStudentByAccountId(Long id){
        return studentMapper.studentModelToStudentDto(studentRepository.findByAccountIdAndActive(id, true)
                .orElseThrow(() -> new EntityNotFoundException("Could not found by student id: "+ id)));
    }

    @Override
    @Transactional
    public TeacherDto getTeacherById(Long id){
        return teacherMapper.teacherModelToTeacherDto(teacherRepository.findByIdAndActive(id, true)
                .orElseThrow(() -> new EntityNotFoundException("Could not found by id: "+ id)));
    }
    @Override
    @Transactional
    public StudentDto getStudentById(Long id){
        return studentMapper.studentModelToStudentDto(studentRepository.findByIdAndActive(id, true)
                .orElseThrow(() -> new EntityNotFoundException("Could not found by id: "+ id)));
    }

    @Override
    @Transactional
    public AccountDto getAccountByEmail(String email){
        Account userData = accountRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("Could not found by email: "+ email));
        return accountMapper.accountModelToAccountDto(userData);
    }

    private void updateAllTokens(Account user, HttpServletResponse response) {
        var jwtToken = jwtService.generateToken(user);
        var refreshToken = jwtService.generateRefreshToken(user);
        updateToken(user, jwtToken);
        jwtService.setJwtCookies(response, jwtToken, refreshToken);
        updateRefreshToken(user, refreshToken);
        refreshTokenRepository.updateRefreshTokenById(user.getId(), refreshToken);
        jwtService.setJwtCookies(response, jwtToken);
    }

    private void updateRefreshToken(Account user, String refreshToken) {
        refreshTokenRepository.updateRefreshTokenById(user.getId(), refreshToken);
    }

    private void updateToken(Account user, String token) {
        tokenRepository.updateTokenById(user.getId(), token);
    }

}
