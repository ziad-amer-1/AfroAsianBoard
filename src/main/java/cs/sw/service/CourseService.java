package cs.sw.service;

import cs.sw.dto.CourseRequestDTO;
import cs.sw.entity.Course;

import java.util.List;

public interface CourseService {
    String createNewCourse(CourseRequestDTO courseRequestDTO);
    Course getSingleCourse(Long courseId);
    List<Course> getAllCourses(String tag);
    String editCourse(Long courseId, CourseRequestDTO courseRequestDTO);
    String deleteCourse(Long courseId);
}
