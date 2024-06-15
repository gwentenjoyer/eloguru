package ua.thecompany.eloguru.services.servicesImpl;

import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.thecompany.eloguru.dto.FeedbackDto;
import ua.thecompany.eloguru.dto.InitDto.FeedbackInitDto;
import ua.thecompany.eloguru.mappers.FeedbackMapper;
import ua.thecompany.eloguru.model.Feedback;
import ua.thecompany.eloguru.repositories.FeedbackRepository;
import ua.thecompany.eloguru.services.FeedbackService;
import ua.thecompany.eloguru.services.StudentService;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@AllArgsConstructor
public class FeedbackServiceImpl implements FeedbackService {

    private FeedbackRepository feedbackRepository;
    private FeedbackMapper feedbackMapper;
    private StudentService studentService;

    @Override
    @Transactional
    public void saveFeedback(FeedbackInitDto feedbackInitDto, Long ownerId) {
        Feedback feedback = feedbackMapper.feedbackInitDtoToFeedbackModel(feedbackInitDto);
        feedback.setOwner(studentService.getStudentModel(ownerId));
        log.info("Feedback with id " + (feedbackRepository.save(feedback)).getId() + " successfully saved.");
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
        return feedbackMapper.feedbackModelToFeedbackDto(feedbackRepository.save(updateFeedbackFields(updateFeedback, feedback)));
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
}
