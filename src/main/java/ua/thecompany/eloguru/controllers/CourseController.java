package ua.thecompany.eloguru.controllers;

import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ua.thecompany.eloguru.dto.CourseDto;
import ua.thecompany.eloguru.dto.InitDto.CourseInitDto;
import ua.thecompany.eloguru.services.AccountService;
import ua.thecompany.eloguru.services.CourseService;
import ua.thecompany.eloguru.services.StudentService;

import java.security.Principal;

@RequiredArgsConstructor
@RestController
@RequestMapping("/courses")
public class CourseController {

    private final CourseService courseService;
    private final StudentService studentService;
    private final AccountService accountService;

    @PostMapping("/create")
    public ResponseEntity<CourseDto> createCourse(Principal principal, @Valid @RequestBody CourseInitDto courseInitDto) {
        try {
            courseService.createCourse(courseInitDto, accountService.getIdByEmail(principal.getName()));
            return new ResponseEntity<>(HttpStatus.CREATED);
        }
        catch (EntityNotFoundException e){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping
    public ResponseEntity<Page<CourseDto>> getCourses(@PageableDefault Pageable pageable, @RequestParam(value = "label", required = false) String searchQuery) {
        if (searchQuery != null && !searchQuery.isEmpty()) {
            Page<CourseDto> coursesList = courseService.searchCourseByHeader(pageable, searchQuery);
            return new ResponseEntity<>(coursesList, HttpStatus.OK);
        }
        else {
            Page<CourseDto> coursesList = courseService.getCourses(pageable);
            return new ResponseEntity<>(coursesList, HttpStatus.OK);
        }
    }

    @GetMapping("{id}")
    public ResponseEntity<CourseDto> getCourse(@PathVariable Long id) {
        try {
            return new ResponseEntity<>(courseService.getCourseById(id), HttpStatus.OK);
        }
        catch (EntityNotFoundException e){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("{id}")
    public ResponseEntity<CourseDto> deleteCourse(Principal principal, @PathVariable Long id) {
        if (courseService.isAccountOwnsCourse(id, accountService.getIdByEmail(principal.getName()))) {
            courseService.deleteCourseById(id);
            return new ResponseEntity<>(HttpStatus.OK);
        }
        else return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @PutMapping("{id}")
    public ResponseEntity<CourseDto> updateCourse(Principal principal, @Valid @RequestBody CourseInitDto courseInitDto, @PathVariable Long id) {
        try {
            if (courseService.isAccountOwnsCourse(id, accountService.getIdByEmail(principal.getName()))) {
                courseService.updateCourse(courseInitDto, id);
                return new ResponseEntity<>(HttpStatus.OK);
            }
            else return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        catch (EntityNotFoundException e){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/{courseId}/enroll")
    public ResponseEntity<CourseDto> enrollToCourse(Principal principal, @PathVariable Long courseId, @RequestParam(value = "accountId", required = false) Long accountId) {
            try{
                if (accountService.validateIdByEmail(principal.getName(), accountId)) {
                    enroll(courseId, accountId);
                    return new ResponseEntity<>(HttpStatus.OK);
                }
                else return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
            catch(EntityNotFoundException e){
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
    }

    @PostMapping("/{courseId}/disenroll")
    public ResponseEntity<CourseDto> disenrollToCourse(Principal principal, @PathVariable Long courseId, @RequestParam(value = "accountId", required = false) Long accountId) {
        try{
            if (accountService.validateIdByEmail(principal.getName(), accountId)) {
                disenroll(courseId, accountId);
                return new ResponseEntity<>(HttpStatus.OK);
            }
            else return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        catch(EntityNotFoundException e){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/force_delete")
    public ResponseEntity<CourseDto> forceDelete() {
        courseService.force_delete();
        return new ResponseEntity<>(HttpStatus.OK);
    }

    private void enroll(Long courseId, Long studentAccountId) {
        courseService.enrollToCourse(courseId, studentAccountId);
        studentService.enrollToCourse(courseId, studentAccountId);
    }

    private void disenroll(Long courseId, Long studentAccountId) {
        courseService.disenrollFromCourse(courseId, studentAccountId);
        studentService.disenrollFromCourse(courseId, studentAccountId);
    }
}
