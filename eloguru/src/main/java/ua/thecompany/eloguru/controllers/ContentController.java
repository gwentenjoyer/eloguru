package ua.thecompany.eloguru.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ua.thecompany.eloguru.dto.ContentDto;
import ua.thecompany.eloguru.dto.InitDto.ContentInitDto;
import ua.thecompany.eloguru.services.ContentService;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/contents")
public class ContentController {
    private final ContentService contentService;

    @GetMapping("/topic")
    public ResponseEntity<List<ContentDto>> getContentsOfTopic(@RequestParam(value = "id", required = true) Long topicId){
        return new ResponseEntity<>(contentService.getContents(topicId), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<ContentDto> getContent(@RequestParam(value = "id", required = true) Long id){
        return new ResponseEntity<>(contentService.getContentById(id), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<ContentDto> saveFeedback(@Valid @RequestBody ContentInitDto contentInitDto){
        contentService.saveContent(contentInitDto);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @DeleteMapping(value="/{id}")
    public ResponseEntity<ContentDto> deleteFeedbackById(@PathVariable Long id){
        contentService.deleteContentById(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping(value="/{id}")
    public ResponseEntity<ContentDto> updateFeedback(@Valid @RequestBody ContentInitDto contentInitDto, @PathVariable Long id){
        contentService.updateContent(id, contentInitDto);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
