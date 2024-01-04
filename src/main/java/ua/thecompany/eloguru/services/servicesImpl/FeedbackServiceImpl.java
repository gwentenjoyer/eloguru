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

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@AllArgsConstructor
public class FeedbackServiceImpl implements FeedbackService {

    private FeedbackRepository feedbackRepository;
    private FeedbackMapper feedbackMapper;

    @Override
    @Transactional
    public void saveFeedback(FeedbackInitDto feedbackInitDto) {
        log.info("Feedback with id " + (feedbackRepository
                .save(feedbackMapper.feedbackInitDtoToFeedbackModel(feedbackInitDto))).getId() + " successfully saved.");
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
        updateFeedback.setId(feedbackId);
        updateFeedbackFields(updateFeedback, feedback);
        return feedbackMapper.feedbackModelToFeedbackDto(feedbackRepository.save(feedback));
    }

    @Override
    @Transactional
//    @CacheEvict
    public void deleteFeedbackById(Long id) {
        feedbackRepository.deleteById(id);
        log.info("Feedback with id " + id + " successfully DELETED.");
    }

    private void updateFeedbackFields(Feedback source, Feedback target) {
        if (source.getRating() != null) {
            target.setRating(source.getRating());
        }
        if (source.getText() != null) {
            target.setText(target.getText());
        }
    }

    @Override
    @Transactional
    public boolean isAccountOwnsFeedback(Long feedbackId, Long accountId) {
        return feedbackRepository.findByIdAndActive(feedbackId, true).orElseThrow(() -> new EntityNotFoundException())
                .getOwner().getAccount().getId() == accountId;
    }
}
