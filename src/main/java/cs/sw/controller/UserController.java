package cs.sw.controller;

import cs.sw.service.CourseService;
import cs.sw.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/student")
public class UserController {

    private final UserService userService;

    @GetMapping
    public ResponseEntity<?> getAllStudents() {
        try {
            return ResponseEntity.ok(userService.getAllStudents());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @PutMapping("/payCourse/{courseId}")
    public ResponseEntity<String> enroll(@PathVariable(name = "courseId") Long courseId, @RequestHeader("Authorization") String token) {
        try {
            return ResponseEntity.ok(userService.payCourse(courseId, token));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

}
