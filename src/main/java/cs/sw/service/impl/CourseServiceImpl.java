package cs.sw.service.impl;

import cs.sw.dto.CourseRequestDTO;
import cs.sw.entity.Course;
import cs.sw.repository.CourseRepo;
import cs.sw.service.CourseService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@Slf4j
public class CourseServiceImpl implements CourseService {

    private final CourseRepo courseRepo;


    @Override
    @PreAuthorize("hasAuthority('MANAGER')")
    public String createNewCourse(CourseRequestDTO courseRequestDTO) {
        Objects.requireNonNull(courseRequestDTO.title());
        Objects.requireNonNull(courseRequestDTO.overview());
        Objects.requireNonNull(courseRequestDTO.numberOfHours());
        Objects.requireNonNull(courseRequestDTO.numberOfLessons());
        Objects.requireNonNull(courseRequestDTO.price());
        Objects.requireNonNull(courseRequestDTO.tag());
        Objects.requireNonNull(courseRequestDTO.whatWillYouLearn());
        throwExceptionIfCourseExist(courseRequestDTO.title());
        Course course = new Course();
        course.setTitle(courseRequestDTO.title());
        course.setPrice(courseRequestDTO.price());
        course.setTag(courseRequestDTO.tag());
        course.setOverview(courseRequestDTO.overview());
        course.setNumberOfLessons(courseRequestDTO.numberOfLessons());
        course.setNumberOfHours(courseRequestDTO.numberOfHours());
        course.setWhatWillYouLearn(courseRequestDTO.whatWillYouLearn());
        courseRepo.save(course);
        return "New course named [" + course.getTitle() + "] created Successfully";
    }

    @Override
    public Course getSingleCourse(Long courseId) {
        return courseRepo.findById(courseId).orElseThrow(() -> new IllegalStateException("Course with id = " + courseId + " not exist"));
    }

    @Override
    public List<Course> getAllCourses(String tag) {
        return courseRepo.listAllByTagIfProvided(tag);
    }

    @Override
    @PreAuthorize("hasAuthority('MANAGER')")
    public String editCourse(Long courseId, CourseRequestDTO courseRequestDTO) {
        Course course = getSingleCourse(courseId);
        if (courseRequestDTO.tag() != null) {
            course.setTag(courseRequestDTO.tag());
        }
        if (courseRequestDTO.title() != null) {
            course.setTitle(courseRequestDTO.title());
        }
        if (courseRequestDTO.overview() != null) {
            course.setOverview(courseRequestDTO.overview());
        }
        if (courseRequestDTO.numberOfLessons() != null) {
            course.setNumberOfLessons(courseRequestDTO.numberOfLessons());
        }
        if (courseRequestDTO.numberOfHours() != null) {
            course.setNumberOfHours(courseRequestDTO.numberOfHours());
        }
        if (courseRequestDTO.price() != null) {
            course.setPrice(courseRequestDTO.price());
        }
        if (courseRequestDTO.whatWillYouLearn() != null) {
            course.setWhatWillYouLearn(courseRequestDTO.whatWillYouLearn());
        }


        return "course with id = " + courseId + " updated successfully";
    }

    @Override
    @PreAuthorize("hasAuthority('MANAGER')")
    public String deleteCourse(Long courseId) {
        Course course = getSingleCourse(courseId);
        courseRepo.delete(course);
        return "course with id = " + courseId + " deleted successfully";
    }

    private boolean isCourseExist(String title) {
        return courseRepo.findByTitle(title).isPresent();
    }

    private void throwExceptionIfCourseExist(String title) {
        if (isCourseExist(title)) {
            throw new RuntimeException("course with title = " + title + " already exist");
        }
    }
}
