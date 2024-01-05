package ua.thecompany.eloguru.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ua.thecompany.eloguru.dto.ContentDto;
import ua.thecompany.eloguru.dto.InitDto.ContentInitDto;
import ua.thecompany.eloguru.services.AccountService;
import ua.thecompany.eloguru.services.ContentService;

import java.security.Principal;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/content")
public class ContentController {
    private final ContentService contentService;
    private final AccountService accountService;

    @GetMapping("/course")
    public ResponseEntity<List<ContentDto>> getContentsOfTopic(@RequestParam(value = "id", required = true) Long courseId){
        return new ResponseEntity<>(contentService.getContents(courseId), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<ContentDto> getContent(@RequestParam(value = "id", required = true) Long id){
        return new ResponseEntity<>(contentService.getContentById(id), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<ContentDto> saveFeedback(Principal principal, @Valid @RequestBody ContentInitDto contentInitDto){
        contentService.saveContent(contentInitDto);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @DeleteMapping(value="/{id}")
    public ResponseEntity<ContentDto> deleteFeedbackById(Principal principal, @PathVariable Long id){
        contentService.deleteContentById(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping(value="/{id}")
    public ResponseEntity<ContentDto> updateFeedback(Principal principal, @Valid @RequestBody ContentInitDto contentInitDto, @PathVariable Long id){
        contentService.updateContent(id, contentInitDto);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
