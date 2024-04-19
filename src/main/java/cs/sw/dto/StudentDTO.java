package cs.sw.dto;

import cs.sw.entity.Course;
import lombok.Builder;

import java.util.List;

@Builder
public record StudentDTO(String name, String email, String phoneNumber, List<Course> courses) {
}
