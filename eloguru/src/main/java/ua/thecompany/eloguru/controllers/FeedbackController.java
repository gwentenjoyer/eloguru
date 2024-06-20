package ua.thecompany.eloguru.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ua.thecompany.eloguru.dto.FeedbackDto;
import ua.thecompany.eloguru.dto.InitDto.FeedbackInitDto;
import ua.thecompany.eloguru.model.EnumeratedRole;
import ua.thecompany.eloguru.services.AccountService;
import ua.thecompany.eloguru.services.FeedbackService;

import java.security.Principal;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/feedbacks")
public class FeedbackController {
    private final FeedbackService feedbackService;
    private final AccountService accountService;

    @GetMapping("/course")
    public ResponseEntity<List<FeedbackDto>> getFeedbacksOfCourse(@RequestParam(value = "id", required = true) Long courseId){
        return new ResponseEntity<>(feedbackService.getFeedbacks(courseId), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<FeedbackDto> getFeedback(@RequestParam(value = "id", required = true) Long id){
        return new ResponseEntity<>(feedbackService.getFeedbackById(id), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<FeedbackDto> saveFeedback(Principal principal, @Valid @RequestBody FeedbackInitDto feedbackInitDto){
        return feedbackService.saveFeedbackOrUpdate(principal, feedbackInitDto);
    }

    @DeleteMapping(value="/{id}")
    public ResponseEntity<FeedbackDto> deleteFeedbackById(Principal principal, @PathVariable Long id){
        if (feedbackService.isAccountOwnsFeedback(id, accountService.getIdByEmail(principal.getName()))) {
            feedbackService.deleteFeedbackById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        else return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @PutMapping(value="/{id}")
    public ResponseEntity<FeedbackDto> updateFeedback(Principal principal, @Valid @RequestBody FeedbackInitDto feedbackInitDto, @PathVariable Long id){
//        System.out.println(accountService.getIdByEmail(principal.getName()));
        if (feedbackService.isAccountOwnsFeedback(id, accountService.getIdByEmail(principal.getName()))) {
            feedbackService.updateFeedback(id, feedbackInitDto);
            return new ResponseEntity<>(HttpStatus.OK);
        }
        else return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }
}
