package cs.sw.service.impl;

import cs.sw.dto.CourseRequestDTO;
import cs.sw.entity.Course;
import cs.sw.repository.CourseRepo;
import cs.sw.service.CourseService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@Slf4j
public class CourseServiceImpl implements CourseService {

    private final CourseRepo courseRepo;
    private final ResourceLoader resourceLoader;

    @Override
    @PreAuthorize("hasAuthority('MANAGER')")
    public String createNewCourse(CourseRequestDTO courseRequestDTO, MultipartFile image) throws IOException {
        Objects.requireNonNull(courseRequestDTO.title());
        Objects.requireNonNull(courseRequestDTO.overview());
        Objects.requireNonNull(courseRequestDTO.numberOfHours());
        Objects.requireNonNull(courseRequestDTO.numberOfLessons());
        Objects.requireNonNull(courseRequestDTO.price());
        Objects.requireNonNull(courseRequestDTO.tag());
        Objects.requireNonNull(courseRequestDTO.whatWillYouLearn());
        Objects.requireNonNull(image);

        throwExceptionIfCourseExist(courseRequestDTO.title());

        // get image byte and store it in the static folder under resources folder
        String imageName = image.getOriginalFilename();
        Resource resource = resourceLoader.getResource("classpath:");
        String imagePath = resource.getFile().getAbsolutePath() + "/static/coursesImages/" + imageName;
        log.info("imageAbsolutePath: {}", imagePath);
        File targetDestinationFile = new File(imagePath);
        Files.write(targetDestinationFile.toPath(), image.getBytes());

        Course course = getCourse(courseRequestDTO, "/coursesImages/" + imageName);
        courseRepo.save(course);
        return "New course named [" + course.getTitle() + "] created Successfully";
    }

    private static Course getCourse(CourseRequestDTO courseRequestDTO, String imagePath) {
        Course course = new Course();
        course.setTitle(courseRequestDTO.title());
        course.setPrice(courseRequestDTO.price());
        course.setTag(courseRequestDTO.tag());
        course.setOverview(courseRequestDTO.overview());
        course.setNumberOfLessons(courseRequestDTO.numberOfLessons());
        course.setNumberOfHours(courseRequestDTO.numberOfHours());
        course.setWhatWillYouLearn(courseRequestDTO.whatWillYouLearn());
        course.setImagePath(imagePath);
        return course;
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
    @Transactional
    public String editCourse(Long courseId, CourseRequestDTO courseRequestDTO, MultipartFile image) throws IOException {
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
        if (image != null) {
            String imageName = image.getOriginalFilename();
            Resource resource = resourceLoader.getResource("classpath:");
            String imagePath = resource.getFile().getAbsolutePath() + "/static/coursesImages/" + imageName;
            log.info("imageAbsolutePath: {}", imagePath);
            File targetDestinationFile = new File(imagePath);
            Files.write(targetDestinationFile.toPath(), image.getBytes());
            course.setImagePath("/coursesImages/" + imageName);
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
