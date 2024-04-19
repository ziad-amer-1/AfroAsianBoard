package cs.sw.repository;

import cs.sw.entity.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface CourseRepo extends JpaRepository<Course, Long> {
    Optional<Course> findByTitle(String title);

    @Query("SELECT c FROM Course c WHERE (?1 IS NULL OR c.tag = ?1)")
    List<Course> listAllByTagIfProvided(String tag);
}
