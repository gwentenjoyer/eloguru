package ua.thecompany.eloguru.controllers;

import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.NoResultException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ua.thecompany.eloguru.dto.CourseDto;
import ua.thecompany.eloguru.dto.InitDto.TopicInitDto;
import ua.thecompany.eloguru.dto.TopicDto;
import ua.thecompany.eloguru.services.AccountService;
import ua.thecompany.eloguru.services.CourseService;
import ua.thecompany.eloguru.services.TopicService;

import java.security.Principal;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/courses/{courseId}/topics")
public class TopicController {
    private final CourseService courseService;
    private final TopicService topicService;
    private final CourseController courseController;
    private final AccountService accountService;


    @GetMapping("/{topicId}")
    public ResponseEntity<TopicDto> getTopic(@PathVariable Long courseId, @PathVariable Long topicId) {
        if (HttpStatus.OK == courseController.getCourse(courseId).getStatusCode()){
            try{
                return new ResponseEntity<>(topicService.getTopicById(topicId), HttpStatus.OK);
            }
            catch(EntityNotFoundException e){
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PostMapping("/create")
    public ResponseEntity<TopicDto> createTopic(@Valid @RequestBody TopicInitDto topicInitDto, @PathVariable Long courseId) {
        try{
            TopicInitDto topic = new TopicInitDto(topicInitDto.label(), courseId, topicInitDto.description());
            return new ResponseEntity<>(courseService.addTopic(courseId, topic), HttpStatus.CREATED);
        }
        catch(EntityNotFoundException e){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }


    @PostMapping("/{topicId}/delete")
    public ResponseEntity<CourseDto> deleteTopic(@PathVariable Long courseId, @PathVariable Long topicId) {

        if (HttpStatus.OK == courseController.getCourse(courseId).getStatusCode()){
            topicService.deleteTopicById(topicId);
            return new ResponseEntity<>(HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PutMapping("/{topicId}")
    public ResponseEntity<CourseDto> updateTopic(@Valid @RequestBody TopicInitDto topicInitDto, @PathVariable Long courseId, @PathVariable Long topicId) {
        if (HttpStatus.OK == courseController.getCourse(courseId).getStatusCode()){
            try{
                TopicInitDto topic = new TopicInitDto(topicInitDto.label(), courseId, topicInitDto.description());
                topicService.updateTopic(topic, topicId);
                return new ResponseEntity<>(HttpStatus.OK);
            }
            catch(EntityNotFoundException e){
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping
    public ResponseEntity<List<TopicDto>> getTopics(@PathVariable Long courseId) {
        if (HttpStatus.OK == courseController.getCourse(courseId).getStatusCode()){
            try{
                return new ResponseEntity<>(topicService.getTopicsByCourse(courseId), HttpStatus.OK);
            }
            catch(EntityNotFoundException e){
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PostMapping("/{topicId}/save_topic_progress")
    public ResponseEntity<?> saveCompletedTopic(Principal principal, @PathVariable Long courseId, @PathVariable Long topicId) {
        try{
            if (principal == null) throw new EntityNotFoundException("Empty principal");
            topicService.saveCompletedTopic(courseId, topicId,
                    accountService.getStudentByAccountId(accountService.getIdByEmail(principal.getName())).id());
            return new ResponseEntity<>(HttpStatus.CREATED);
        }
        catch(EntityNotFoundException e){
            return new ResponseEntity<>(e.getMessage(),HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/{topicId}/remove_topic_progress")
    public ResponseEntity<?> removeCompletedTopic(Principal principal, @PathVariable Long courseId, @PathVariable Long topicId) {
        try {
            if (principal == null) throw new EntityNotFoundException("Empty principal");
            topicService.removeCompletedTopic(courseId, topicId,
                    accountService.getStudentByAccountId(accountService.getIdByEmail(principal.getName())).id());
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        catch(EntityNotFoundException e){
            return new ResponseEntity<>(e.getMessage(),HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/{topicId}/completed")
    public ResponseEntity<?> getCompletedTopics(Principal principal, @PathVariable Long courseId, @PathVariable Long topicId) {
        try {
            if (principal == null) throw new EntityNotFoundException("Empty principal");
            return new ResponseEntity<>(topicService.getProgress(courseId, topicId,
                    accountService.getStudentByAccountId(accountService.getIdByEmail(principal.getName())).id()),HttpStatus.OK);
        }
        catch(EntityNotFoundException e){
            return new ResponseEntity<>(e.getMessage(),HttpStatus.NOT_FOUND);
        }
        catch(NoResultException e){
            return new ResponseEntity<>(e.getMessage(),HttpStatus.NO_CONTENT);
        }
    }
}
