package ua.thecompany.eloguru.controllers;

import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ua.thecompany.eloguru.dto.CourseDto;
import ua.thecompany.eloguru.dto.InitDto.TopicInitDto;
import ua.thecompany.eloguru.dto.TopicDto;
import ua.thecompany.eloguru.services.CourseService;
import ua.thecompany.eloguru.services.TopicService;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/courses/{courseId}/topics")
public class TopicController {
    private final CourseService courseService;
    private final TopicService topicService;
    private final CourseController courseController;

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
    public ResponseEntity<CourseDto> createTopic(@Valid @RequestBody TopicInitDto topicInitDto, @PathVariable Long courseId) {
        ResponseEntity<CourseDto> responseEntity = courseController.getCourse(courseId);
        if (HttpStatus.OK == responseEntity.getStatusCode()){
            try{
                TopicInitDto topic = new TopicInitDto(topicInitDto.label(), courseId);
                courseService.addTopic(responseEntity.getBody(), topic);
                return new ResponseEntity<>(HttpStatus.OK);
            }
            catch(EntityNotFoundException e){
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }


    @DeleteMapping("/{topicId}")
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
                TopicInitDto topic = new TopicInitDto(topicInitDto.label(), courseId);
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
                return new ResponseEntity<>(topicService.getTopics(), HttpStatus.OK);
            }
            catch(EntityNotFoundException e){
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}
