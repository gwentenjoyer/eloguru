package ua.thecompany.eloguru.mappers;

import org.mapstruct.*;


import ua.thecompany.eloguru.dto.AccountDto;
import ua.thecompany.eloguru.dto.InitDto.AccountInitDto;
import ua.thecompany.eloguru.dto.StudentDto;
import ua.thecompany.eloguru.dto.TeacherDto;
import ua.thecompany.eloguru.model.Account;
import ua.thecompany.eloguru.model.Student;
import ua.thecompany.eloguru.model.Teacher;

@Mapper(componentModel = "spring")
public interface AccountMapper {

//    Account accountDtoToAccountModel(AccountDto accountDto);

    AccountDto accountModelToAccountDto(Account account);
//    StudentDto accountModelToStudentDto(Account account);
//    TeacherDto accountModelToTeacherDto(Account account);

//    StudentDto studentModelToStudentDto(Student account);
//
//    TeacherDto teacherModelToTeacherDto(Teacher account);


    Account accountInitDtoToAccountModel(AccountInitDto accountInitDto);

    Account updateAccountModelViaAccountInitDto(AccountInitDto accountInitDto, @MappingTarget Account account);
}
