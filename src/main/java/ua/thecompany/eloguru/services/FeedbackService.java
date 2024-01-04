package ua.thecompany.eloguru.services;

import org.springframework.stereotype.Service;
import ua.thecompany.eloguru.dto.FeedbackDto;
import ua.thecompany.eloguru.dto.InitDto.FeedbackInitDto;

import java.util.List;

@Service
public interface FeedbackService {
    void saveFeedback(FeedbackInitDto feedbackInitDto);

    List<FeedbackDto> getFeedbacks(Long courseId);

    FeedbackDto getFeedbackById(Long id);

    FeedbackDto updateFeedback(Long feedbackId, FeedbackInitDto feedbackInitDto);

    void deleteFeedbackById(Long id);

    boolean isAccountOwnsFeedback(Long feedbackId, Long accountId);
}
