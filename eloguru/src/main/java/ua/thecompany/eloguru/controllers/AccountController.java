package ua.thecompany.eloguru.controllers;

import jakarta.mail.MessagingException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AuthorizationServiceException;
import org.springframework.web.bind.annotation.*;
import ua.thecompany.eloguru.dto.*;
import ua.thecompany.eloguru.dto.InitDto.AccountInitDto;
import ua.thecompany.eloguru.model.EnumeratedRole;
import ua.thecompany.eloguru.security.AuthService;
import ua.thecompany.eloguru.security.AuthServiceImpl;
import ua.thecompany.eloguru.services.AccountService;
import ua.thecompany.eloguru.services.StudentService;
import ua.thecompany.eloguru.services.TeacherService;

import java.security.Principal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
@RestController
@RequestMapping("/accounts")
public class AccountController {
    private final AccountService accountService;
    private final TeacherService teacherService;
    private final StudentService studentService;
    private final AuthService authService;
    private final AuthServiceImpl service;
//    private final AccountRepository accountRepository;
//    private final EmailService emailService;

    @PostMapping("/signup")
    public ResponseEntity<?> saveAccount(@RequestParam(value = "role", required = true) @Valid String accountType,
                                         @RequestBody AccountInitDto accountInitDto) {
        if ((accountInitDto.password() == null) || (accountInitDto.email() == null))
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        try {

            if (accountType.equals(EnumeratedRole.TEACHER.toString())) {
                teacherService.createTeacher(accountInitDto);
            }
            else if (accountType.equals(EnumeratedRole.STUDENT.toString())) {
                studentService.createStudent(accountInitDto);
            }
            else
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

            return new ResponseEntity<>(HttpStatus.CREATED);
        }
        catch (MessagingException e){
            return new ResponseEntity<>("Account with the email already exists", HttpStatus.CONFLICT);
        }
    }

//    @GetMapping("/activate")
//    public ResponseEntity<?> activate(@RequestParam(defaultValue = "") String activationCode) {
//        service.activate(activationCode);
//        return new ResponseEntity<>("activated", HttpStatus.OK);
//    }

    @PostMapping("/activate")
    public ResponseEntity<?> activate(@RequestParam(required = true, defaultValue = "") String activationCode,
                                      @RequestBody AccountInitDto accountInitDto, HttpServletResponse response) {
        service.accountPostregister(activationCode, accountInitDto, response);
        return new ResponseEntity<>("activated", HttpStatus.OK);
    }

    @GetMapping("/activate")
    public ResponseEntity<String> activate(@RequestParam(defaultValue = "") String activationCode) {
        return ResponseEntity.ok(service.activate(activationCode));
    }


    @GetMapping(value="/{id}")
    public ResponseEntity<AccountDto> getAccountById(@PathVariable Long id){
        try{
            AccountDto accountModel = accountService.getAccountById(id);
            return new ResponseEntity<>(accountModel, HttpStatus.OK);
        }
        catch (EntityNotFoundException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @DeleteMapping
    public ResponseEntity<AccountDto> deleteAccountById(Principal principal){
        accountService.deleteAccountById(accountService.getIdByEmail(principal.getName()));
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @GetMapping("/list")
    public ResponseEntity<Page<AccountDto>> getAccounts(@PageableDefault Pageable pageable) {
        Page<AccountDto> accountDtos = accountService.getAccounts(pageable);
        return new ResponseEntity<>(accountDtos, HttpStatus.OK);
    }

    @PostMapping("/login")
    public ResponseEntity<?> authenticate(@Valid @RequestBody LoginRequest request, HttpServletResponse response) {
        try {
            authService.authenticate(request, response);
            return new ResponseEntity<>(HttpStatus.OK);
        }
        catch (MessagingException e){
            System.out.println(e.getMessage());
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
        catch (AuthorizationServiceException authe){
            System.out.println(authe.getMessage());
            return new ResponseEntity<>(authe.getMessage(), HttpStatus.UNAUTHORIZED);
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletResponse response) {
        authService.logout(response);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/check")
    public ResponseEntity<?> checkAuth(Principal principal) {
        if (principal == null)
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        Map res = new HashMap();
        var userRole = accountService.getUserRole(principal.getName());
        res.put("role", userRole);
        var userId = accountService.getIdByEmail(principal.getName()).toString();
        res.put("userId", userId);
        if(userRole == EnumeratedRole.TEACHER.toString()){
            var idByRole = accountService.getTeacherByAccountId(Integer.valueOf(userId).longValue());
            res.put("idByRole", idByRole.id().toString());
        }
        else if(userRole == EnumeratedRole.STUDENT.toString()){
            var idByRole = accountService.getStudentByAccountId(Integer.valueOf(userId).longValue());
            res.put("idByRole", idByRole.id().toString());
        }
        res.put("email", principal.getName());
        if(principal.getName() != null) {
            return new ResponseEntity<>(res, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping("/refreshToken")
    public ResponseEntity<?> refreshToken(
            HttpServletRequest request, HttpServletResponse response
    ) {
        var isTokenValid = service.refreshToken(request, response);
        if (isTokenValid != null) {
            return ResponseEntity.ok(isTokenValid);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    @PutMapping
    public ResponseEntity<CourseDto> updateAccount(Principal principal,
                                                   @Valid @RequestBody AccountInitDto accountInitDto,
                                                   HttpServletResponse response) {
        try {
            if (principal == null)
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            accountService.updateAccount(accountInitDto, accountService.getIdByEmail(principal.getName()), response);
            return new ResponseEntity<>(HttpStatus.OK);
//            }
        }
        catch (EntityNotFoundException e){
            System.out.println(e);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<CourseDto> updateAccountRoleById(@RequestParam(required = true) String statusActive,
                                                           @PathVariable Long id) {
        try {
            accountService.updateAccountRoleById(id, Boolean.parseBoolean(statusActive));
            return new ResponseEntity<>(HttpStatus.OK);
//            }
        }
        catch (EntityNotFoundException e){
            System.out.println(e);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/forceDelete/{id}")
    public ResponseEntity<AccountDto> deleteAccountById(@PathVariable Long id){
        accountService.forceDeleteAccountById(id);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @GetMapping(value="/teacher/{id}")
    public ResponseEntity<TeacherDto> getTeacherById(@PathVariable Long id){
        try{
            TeacherDto accountModel = accountService.getTeacherById(id);
            return new ResponseEntity<>(accountModel, HttpStatus.OK);
        }
        catch (EntityNotFoundException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @GetMapping(value="/teacher/{id}/getName")
    public ResponseEntity<String> getTeacherNameById(@PathVariable Long id){
        try{
            String accountModel = teacherService.getTeacherNameById(id);
            return new ResponseEntity<>(accountModel, HttpStatus.OK);
        }
        catch (EntityNotFoundException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

//    @GetMapping("/activate")
//    public ResponseEntity<?> activate(@RequestParam(defaultValue = "") String activationCode) throws MessagingException {
//        emailService.sendEmail("neongenesisbaklan@gmail.com", "test", "test");
////        return ResponseEntity.ok();
//        return ResponseEntity.status(HttpStatus.OK).body(null);
//    }


    @GetMapping(value="/student/{id}")
    public ResponseEntity<StudentDto> getStudentById(@PathVariable Long id){
        try{
            StudentDto accountModel = accountService.getStudentById(id);
            return new ResponseEntity<>(accountModel, HttpStatus.OK);
        }
        catch (EntityNotFoundException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @PutMapping(value="/student/{id}")
    public ResponseEntity<StudentDto> putStudentById(@PathVariable Long id){
        try{
            StudentDto accountModel = accountService.getStudentById(id);
            return new ResponseEntity<>(accountModel, HttpStatus.OK);
        }
        catch (EntityNotFoundException e){
            System.out.println(e);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @GetMapping(value="/getUserInfo")
    public ResponseEntity<?> getUserInfo(Principal principal){
        if (principal == null)
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        var email = principal.getName();
        var acc = accountService.getAccountByEmail(email);
        var accRole = acc.role().toString();
        if(accRole == EnumeratedRole.TEACHER.toString()){
            var teacher = accountService.getTeacherByAccountId(acc.id());
            return new ResponseEntity<>(teacher, HttpStatus.OK);
        }
        else if(accRole == EnumeratedRole.STUDENT.toString()){
            var student = accountService.getStudentByAccountId(acc.id());
            return new ResponseEntity<>(student, HttpStatus.OK);

        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
    }
}
