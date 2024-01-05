package ua.thecompany.eloguru.services;

import jakarta.mail.MessagingException;
import org.springframework.stereotype.Service;
import ua.thecompany.eloguru.dto.InitDto.AccountInitDto;
import ua.thecompany.eloguru.dto.StudentDto;
import ua.thecompany.eloguru.model.Student;

import java.util.List;

@Service
public interface StudentService {
    void createStudent(AccountInitDto accountInitDto) throws MessagingException;

    List<StudentDto> getStudent();

    StudentDto getStudentById(Long id);

    StudentDto updateStudent(AccountInitDto accountInitDto, Long id);

    void deleteStudentById(Long id);

    void enrollToCourse(Long courseId, Long studentAccountId);
    void disenrollFromCourse(Long courseId, Long studentAccountId);

    Long getStudentId(Student student);

    Student getStudentModel(Long id);
//    Student getStudentModelByAccoundId(Long id);
}
