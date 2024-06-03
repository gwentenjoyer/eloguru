package ua.thecompany.eloguru.services.servicesImpl;

import jakarta.mail.MessagingException;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.thecompany.eloguru.dto.InitDto.AccountInitDto;
import ua.thecompany.eloguru.dto.TeacherDto;
import ua.thecompany.eloguru.mappers.TeacherMapper;
import ua.thecompany.eloguru.model.EnumeratedRole;
import ua.thecompany.eloguru.model.Student;
import ua.thecompany.eloguru.model.Teacher;
import ua.thecompany.eloguru.repositories.TeacherRepository;
import ua.thecompany.eloguru.security.AuthService;
import ua.thecompany.eloguru.services.TeacherService;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@AllArgsConstructor
public class TeacherServiceImpl implements TeacherService {

    private TeacherRepository teacherRepository;
    private TeacherMapper teacherMapper;
    private AuthService authService;

    @Override
    @Transactional
    public void createTeacher(AccountInitDto accountInitDto) throws MessagingException {
        try {
            Teacher teacher = teacherMapper.accountInitDtoToTeacherModel(accountInitDto);
            teacher.setAccount(authService.register(accountInitDto, EnumeratedRole.TEACHER));
            var savedData = teacherRepository.save(teacher);
            log.info("Account (teacher) with id " + savedData.getAccount().getId() + " successfully registered.");
        }
        catch (MessagingException e){
            throw e;
        }
    }

    @Override
    @Transactional
    public List<TeacherDto> getTeachers() {
        log.info("Retrieving all teachers");
        return teacherRepository.findByAccountActive(true).stream().map(entity -> teacherMapper.teacherModelToTeacherDto(entity)).collect(Collectors.toList());
    }


    @Override
    @Transactional
    public TeacherDto getTeacherById(Long id) throws EntityNotFoundException {
        log.info("Getting account by id: " + id);
        return teacherMapper.teacherModelToTeacherDto(teacherRepository.findByIdAndActive(id, true)
                .orElseThrow(() -> new EntityNotFoundException("Could not found by id: "+ id)));
    }


    @Override
    @Transactional
    public TeacherDto updateTeacher(AccountInitDto teacherInitDto, Long id) {
        return null;
    }

    @Override
    @Transactional
    public void deleteTeacherById(Long id) {
        teacherRepository.deleteById(id);
        log.info("Account (teacher) with name " + id + " successfully DELETED.");
    }

    @Override
    @Transactional
    public Teacher getTeacherModelById(Long id) throws EntityNotFoundException {
        return teacherRepository.findByIdAndActive(id, true)
                .orElseThrow(() -> new EntityNotFoundException("Could not found teacher by id: " + id));
    }

    @Override
    @Transactional
    public Long getTeacherId(Teacher teacher){
        return teacher.getId();
    }

}
