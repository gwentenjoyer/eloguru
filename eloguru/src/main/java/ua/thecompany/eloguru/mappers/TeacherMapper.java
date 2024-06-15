package ua.thecompany.eloguru.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ua.thecompany.eloguru.dto.InitDto.AccountInitDto;
import ua.thecompany.eloguru.dto.TeacherDto;
import ua.thecompany.eloguru.model.Teacher;

@Mapper(componentModel = "spring", uses = CourseMapper.class)
public interface TeacherMapper {
//    @Mapping(target = "accountId", source = "account.id")
    @Mapping(target = "id", source = "id")
    @Mapping(target = "createdDate", source = "account.createdDate")
    @Mapping(target = "lastModifiedDate", source = "account.lastModifiedDate")
    @Mapping(target = "role", source = "account.role")
    @Mapping(target = "fullname", source = "account.fullname")
    @Mapping(target = "email", source = "account.email")
    @Mapping(target = "country", source = "account.country")
    @Mapping(target = "active", source = "account.active")
    @Mapping(target = "phone", source = "account.phone")
    @Mapping(target = "teachingCoursesId", source = "teaching", qualifiedByName = "courseToId")
    TeacherDto teacherModelToTeacherDto(Teacher teacher);

    @Mapping(target = "account.email", source = "email")
    @Mapping(target = "account.pwhash", source = "password")
    @Mapping(target = "account.phone", source = "phone")
    Teacher accountInitDtoToTeacherModel(AccountInitDto accountInitDto);
}
