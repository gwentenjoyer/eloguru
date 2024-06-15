package ua.thecompany.eloguru.services.servicesImpl;

import jakarta.mail.MessagingException;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.thecompany.eloguru.dto.InitDto.AccountInitDto;
import ua.thecompany.eloguru.dto.StudentDto;
import ua.thecompany.eloguru.mappers.StudentMapper;
import ua.thecompany.eloguru.model.EnumeratedRole;
import ua.thecompany.eloguru.model.Student;
import ua.thecompany.eloguru.repositories.CourseRepository;
import ua.thecompany.eloguru.repositories.StudentRepository;
import ua.thecompany.eloguru.security.AuthService;
import ua.thecompany.eloguru.services.StudentService;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@AllArgsConstructor
public class StudentServiceImpl implements StudentService {
    private final CourseRepository courseRepository;
    private StudentRepository studentRepository;
    private StudentMapper studentMapper;
    private AuthService authService;

    @Override
    @Transactional
    public void createStudent(AccountInitDto accountInitDto) throws MessagingException {
        try {
            Student student = studentMapper.accountInitDtoToStudentModel(accountInitDto);
            student.setAccount(authService.register(accountInitDto, EnumeratedRole.STUDENT));
            var savedData = studentRepository.save(student);
            log.info("Account (student) with id " + savedData.getAccount().getId() + " successfully registered.");
        }
        catch (MessagingException e){
            throw e;
        }
    }

    @Override
    @Transactional
//    @Cacheable
    public List<StudentDto> getStudent() {
        log.info("Retrieving all students");
        return studentRepository.findByAccountActive(true).stream().map(entity -> studentMapper.studentModelToStudentDto(entity)).collect(Collectors.toList());
    }

    @Override
    @Transactional
//    @Cacheable
    public StudentDto getStudentById(Long id) {
        log.info("Getting account by id: " + id);
        return studentMapper.studentModelToStudentDto(studentRepository.findByIdAndActive(id, true)
                .orElseThrow(() -> new EntityNotFoundException("Could not found by id: "+ id)));
    }

    @Override
    @Transactional
    public StudentDto updateStudent(AccountInitDto accountInitDto, Long id) {
        return null;
    }

    public void deleteStudentById(Long id) {

    }

    @Override
    @Transactional
    public void enrollToCourse(Long courseId, Long studentAccountId) {
        log.info("Enrolling student with id: " + studentAccountId + " to course with id: " + courseId);
        Student student = studentRepository.findByIdAndActive(studentAccountId, true).orElseThrow(() -> new EntityNotFoundException("Could not find student with id: " + studentAccountId));
        log.info("Enrolling student with id: " + studentAccountId + " to course with id: " + courseId);
        student.getCourses().add(courseRepository.findByIdAndActive(courseId, true).orElseThrow(() -> new EntityNotFoundException("Could not find course with id: " + courseId)));
        studentRepository.save(student);
        log.info("Successfully enrolled.");
    }

    @Override
    @Transactional
    public void disenrollFromCourse(Long courseId, Long studentAccountId) {
        Student student = studentRepository.findByIdAndActive(studentAccountId, true).orElseThrow(() -> new EntityNotFoundException("Could not find student with id: " + studentAccountId));
        student.getCourses().remove(courseRepository.findByIdAndActive(courseId, true).orElseThrow(() -> new EntityNotFoundException("Could not find course with id: " + courseId)));
        studentRepository.save(student);
        log.info("Successfully enrolled student with id:" + studentAccountId + " to course with id: " + courseId);
    }

    public Long getStudentId(Student student){
        return student.getId();
    }

    public Student getStudentModel(Long id) throws EntityNotFoundException{
        return studentRepository.findByIdAndActive(id, true)
                .orElseThrow(() -> new EntityNotFoundException("Could not found student with id " + id));
    }

    public String getFullnameById(Long id) {
        var test = getStudentModel(id).getAccount();
        var test2 = studentMapper.studentModelToStudentDto(getStudentModel(id));
        return getStudentModel(id).getAccount().getFullname();
    }

}
