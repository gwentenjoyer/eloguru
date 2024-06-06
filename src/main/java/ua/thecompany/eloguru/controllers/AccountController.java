package ua.thecompany.eloguru.controllers;

import jakarta.mail.MessagingException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ua.thecompany.eloguru.dto.*;
import ua.thecompany.eloguru.dto.InitDto.AccountInitDto;
import ua.thecompany.eloguru.model.EnumeratedRole;
import ua.thecompany.eloguru.security.AuthService;
import ua.thecompany.eloguru.services.AccountService;
import ua.thecompany.eloguru.services.StudentService;
import ua.thecompany.eloguru.services.TeacherService;

import java.security.Principal;
import java.util.ArrayList;

@RequiredArgsConstructor
@RestController
@RequestMapping("/accounts")
public class AccountController {
    private final AccountService accountService;
    private final TeacherService teacherService;
    private final StudentService studentService;
    private final AuthService authService;

    @PostMapping("signup")
    public ResponseEntity<?> saveAccount(@RequestParam(value = "role", required = true) @Valid String accountType,
                                          @Valid @RequestBody AccountInitDto accountInitDto) {
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
    public ResponseEntity<ArrayList<AccountDto>> getAccounts() {
        ArrayList<AccountDto> accountDtos = (ArrayList<AccountDto>) accountService.getAccounts();
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
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletResponse response) {
        authService.logout(response);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/check")
    public ResponseEntity<?> checkAuth(Principal principal) {
        ArrayList<String> res = new ArrayList<>();
        var userRole = accountService.getUserRole(principal.getName());
        res.add(userRole);
        var userId = accountService.getIdByEmail(principal.getName()).toString();
        if(userRole == EnumeratedRole.TEACHER.toString()){
            var idByRole = accountService.getTeacherByAccountId(Integer.valueOf(userId).longValue());
            res.add(idByRole.id().toString());
        }
        else if(userRole == EnumeratedRole.STUDENT.toString()){
            var idByRole = accountService.getStudentByAccountId(Integer.valueOf(userId).longValue());
            res.add(idByRole.id().toString());
        }
        res.add(principal.getName());
        if(principal.getName() != null) {
            return new ResponseEntity<>(res, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PutMapping
    public ResponseEntity<CourseDto> updateAccount(Principal principal, @Valid @RequestBody AccountInitDto accountInitDto) {
        try {
            if (principal.getName().equals(accountInitDto.email())) {
                accountService.updateAccount(accountInitDto);
                return new ResponseEntity<>(HttpStatus.OK);
            }
            else return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        catch (EntityNotFoundException e){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping(value="/{id}")
    public ResponseEntity<TeacherDto> getTeacherById(@PathVariable Long id){
        try{
            TeacherDto accountModel = accountService.getTeacherById(id);
            return new ResponseEntity<>(accountModel, HttpStatus.OK);
        }
        catch (EntityNotFoundException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @GetMapping(value="/{id}")
    public ResponseEntity<StudentDto> getStudentById(@PathVariable Long id){
        try{
            StudentDto accountModel = accountService.getStudentById(id);
            return new ResponseEntity<>(accountModel, HttpStatus.OK);
        }
        catch (EntityNotFoundException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }
}
