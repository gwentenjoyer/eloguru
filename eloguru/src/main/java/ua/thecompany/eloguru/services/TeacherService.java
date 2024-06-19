package ua.thecompany.eloguru.services;

import jakarta.mail.MessagingException;
import org.springframework.stereotype.Service;
import ua.thecompany.eloguru.dto.InitDto.AccountInitDto;
import ua.thecompany.eloguru.dto.TeacherDto;
import ua.thecompany.eloguru.model.Teacher;

import java.util.List;

@Service
public interface TeacherService {
    void createTeacher(AccountInitDto accountInitDto) throws MessagingException;

    List<TeacherDto> getTeachers();

    TeacherDto getTeacherById(Long id);

    Teacher getTeacherModelById(Long id);

    TeacherDto updateTeacher(AccountInitDto teacherInitDto, Long id);

    void deleteTeacherById(Long id);

    Long getTeacherId(Teacher teacher);

    String getTeacherNameById(Long teacherId);

//    Teacher getTeacherModelByAccoundId(Long id);
}
