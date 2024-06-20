package ua.thecompany.eloguru.services;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import ua.thecompany.eloguru.dto.FeedbackDto;
import ua.thecompany.eloguru.dto.InitDto.FeedbackInitDto;

import java.security.Principal;
import java.util.List;

@Service
public interface FeedbackService {
    void saveFeedback(FeedbackInitDto feedbackInitDto, Long ownerId);

    List<FeedbackDto> getFeedbacks(Long courseId);

    FeedbackDto getFeedbackById(Long id);

    FeedbackDto updateFeedback(Long feedbackId, FeedbackInitDto feedbackInitDto);

    void deleteFeedbackById(Long id);

    boolean isAccountOwnsFeedback(Long feedbackId, Long accountId);
    ResponseEntity<FeedbackDto> saveFeedbackOrUpdate(Principal principal, @Valid @RequestBody FeedbackInitDto feedbackInitDto);

}
