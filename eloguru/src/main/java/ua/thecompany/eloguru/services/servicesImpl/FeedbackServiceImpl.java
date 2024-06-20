package ua.thecompany.eloguru.services.servicesImpl;

import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import ua.thecompany.eloguru.dto.FeedbackDto;
import ua.thecompany.eloguru.dto.InitDto.FeedbackInitDto;
import ua.thecompany.eloguru.mappers.FeedbackMapper;
import ua.thecompany.eloguru.model.EnumeratedRole;
import ua.thecompany.eloguru.model.Feedback;
import ua.thecompany.eloguru.model.Student;
import ua.thecompany.eloguru.repositories.CourseRepository;
import ua.thecompany.eloguru.repositories.FeedbackRepository;
import ua.thecompany.eloguru.services.AccountService;
import ua.thecompany.eloguru.services.FeedbackService;
import ua.thecompany.eloguru.services.StudentService;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@AllArgsConstructor
public class FeedbackServiceImpl implements FeedbackService {
    private final CourseRepository courseRepository;

    private FeedbackRepository feedbackRepository;
    private FeedbackMapper feedbackMapper;
    private StudentService studentService;
    private final AccountService accountService;

    @Override
    @Transactional
    public void saveFeedback(FeedbackInitDto feedbackInitDto, Long studentOwnerId) {
        Feedback feedback = feedbackMapper.feedbackInitDtoToFeedbackModel(feedbackInitDto);
        feedback.setOwner(studentService.getStudentModel(accountService.getStudentById(studentOwnerId).id()));
        var saved = feedbackRepository.save(feedback);
        var courseId = feedbackInitDto.courseId();
        log.info("Feedback with id " + saved.getId() + " successfully saved.");
        updateRate(courseId);
    }

    private void updateRate(Long courseId){
        ArrayList<FeedbackDto> feedbackDtos = (ArrayList<FeedbackDto>) getFeedbacks(courseId);
        if (!feedbackDtos.isEmpty()) {
            courseRepository.findByIdAndActive(courseId, true)
                    .orElseThrow(() -> new EntityNotFoundException("Could not course with id: " + courseId + " to set feedback"))
                    .setRating(String.valueOf(feedbackDtos.stream()
                            .mapToDouble(FeedbackDto::rating)
                            .sum() / feedbackDtos.size()));
            log.info("Course with id " + courseId + " successfully updated rate.");
        }
    }

    @Override
    @Transactional
//    @Cacheable
    public List<FeedbackDto> getFeedbacks(Long courseId) {
        log.info("Retrieving all feedbacks of course with id " + courseId);
        return feedbackRepository.findByCourse(courseId).stream().map(entity -> feedbackMapper.feedbackModelToFeedbackDto(entity)).collect(Collectors.toList());

    }

    @Override
    @Transactional
//    @Cacheable
    public FeedbackDto getFeedbackById(Long id) {
        log.info("Feedback with id " + id);
        return feedbackMapper.feedbackModelToFeedbackDto(feedbackRepository.findByIdAndActive(id, true)
                .orElseThrow(() -> new EntityNotFoundException("Could not found feedback by id: "+ id)));
    }

    @Override
    @Transactional
//    @CachePut
    public FeedbackDto updateFeedback(Long feedbackId, FeedbackInitDto feedbackInitDto) throws EntityNotFoundException {
        Feedback feedback = feedbackRepository.findByIdAndActive(feedbackId, true)
                .orElseThrow(() -> new EntityNotFoundException("Could not found feedback by id: "+ feedbackId));
        Feedback updateFeedback = feedbackMapper.feedbackInitDtoToFeedbackModel(feedbackInitDto);
        var dto =  feedbackMapper.feedbackModelToFeedbackDto(feedbackRepository.save(updateFeedbackFields(updateFeedback, feedback)));
        updateRate(dto.courseId());
        return dto;
    }

    @Override
    @Transactional
//    @CacheEvict
    public void deleteFeedbackById(Long id) {
        feedbackRepository.deleteById(id);
        log.info("Feedback with id " + id + " successfully DELETED.");
    }

    private Feedback updateFeedbackFields(Feedback source, Feedback target) {
        if (source.getRating() != null) {
            target.setRating(source.getRating());
        }
        if (source.getText() != null) {
            target.setText(source.getText());
        }
        return target;
    }

    @Override
    @Transactional
    public boolean isAccountOwnsFeedback(Long feedbackId, Long accountId) {
//        System.out.println(feedbackRepository.findByIdAndActive(feedbackId, true).orElseThrow(() -> new EntityNotFoundException()).getOwner().getAccount().getId());
        return feedbackRepository.findByIdAndActive(feedbackId, true).orElseThrow(() -> new EntityNotFoundException())
                .getOwner().getAccount().getId() == accountId;
    }

    @Override
    @Transactional
    public ResponseEntity<FeedbackDto> saveFeedbackOrUpdate(Principal principal, @Valid @RequestBody FeedbackInitDto feedbackInitDto){
        if (principal == null ) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        var user = accountService.getAccountById(accountService.getIdByEmail(principal.getName()));
        if (user.role() != EnumeratedRole.STUDENT) {
            // TODO: logs
            log.info("Account with id " + user.id() + " cant leave feedback");
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        Student student = studentService.getStudentModel(accountService.getStudentByAccountId(user.id()).id());
        var optFeedback = feedbackRepository.findByOwnerAndCourse(student.getId(), feedbackInitDto.courseId());
        if (optFeedback.isEmpty()) {
            saveFeedback(feedbackInitDto, accountService.getStudentByAccountId(accountService.getIdByEmail(principal.getName())).id());
            return new ResponseEntity<>(HttpStatus.CREATED);
        }
        else{
            updateFeedback(optFeedback.get().getId(), feedbackInitDto);
            return new ResponseEntity<>(HttpStatus.OK);
        }
    }
}
