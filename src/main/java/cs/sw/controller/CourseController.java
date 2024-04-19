package cs.sw.controller;

import cs.sw.dto.CourseRequestDTO;
import cs.sw.service.CourseService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/course")
public class CourseController {

    private final CourseService courseService;

    @GetMapping
    public ResponseEntity<?> getAllCourses(@RequestParam(value = "tag", required = false) String tag) {
        try {
            return ResponseEntity.ok(courseService.getAllCourses(tag));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @GetMapping("/{courseId}")
    public ResponseEntity<?> getSingleCourse(@PathVariable Long courseId) {
        try {
            return ResponseEntity.ok(courseService.getSingleCourse(courseId));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @PostMapping
    public ResponseEntity<?> createNewCourse(@RequestBody CourseRequestDTO courseRequestDTO) {
        try {
            return ResponseEntity.ok(courseService.createNewCourse(courseRequestDTO));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @PutMapping("/{courseId}")
    public ResponseEntity<?> updateCourse(@PathVariable Long courseId, @RequestBody CourseRequestDTO courseRequestDTO) {
        try {
            return ResponseEntity.ok(courseService.editCourse(courseId, courseRequestDTO));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @DeleteMapping("/{courseId}")
    public ResponseEntity<?> deleteCourse(@PathVariable Long courseId) {
        try {
            return ResponseEntity.ok(courseService.deleteCourse(courseId));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

}
