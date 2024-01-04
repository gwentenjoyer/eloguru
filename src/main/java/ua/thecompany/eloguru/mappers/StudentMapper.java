package ua.thecompany.eloguru.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ua.thecompany.eloguru.dto.InitDto.AccountInitDto;
import ua.thecompany.eloguru.dto.StudentDto;
import ua.thecompany.eloguru.model.Student;

@Mapper(componentModel = "spring")
public interface StudentMapper {

    @Mapping(target = "id", source = "account.id")
    @Mapping(target = "createdDate", source = "account.createdDate")
    @Mapping(target = "lastModifiedDate", source = "account.lastModifiedDate")
    @Mapping(target = "role", source = "account.role")
    @Mapping(target = "fullname", source = "account.fullname")
    @Mapping(target = "email", source = "account.email")
    @Mapping(target = "pwhash", source = "account.pwhash")
    @Mapping(target = "phone", source = "account.phone")
    StudentDto studentModelToStudentDto(Student student);

    @Mapping(target = "account.email", source = "email")
    @Mapping(target = "account.pwhash", source = "password")
    @Mapping(target = "account.phone", source = "phone")
    Student accountInitDtoToStudentModel(AccountInitDto accountInitDto);
}
