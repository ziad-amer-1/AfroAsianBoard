package cs.sw.service;

import cs.sw.dto.CourseRequestDTO;
import cs.sw.entity.Course;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface CourseService {
    String createNewCourse(CourseRequestDTO courseRequestDTO, MultipartFile image) throws IOException;
    Course getSingleCourse(Long courseId);
    List<Course> getAllCourses(String tag);
    String editCourse(Long courseId, CourseRequestDTO courseRequestDTO, MultipartFile image) throws IOException;
    String deleteCourse(Long courseId);
}
