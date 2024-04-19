package cs.sw.dto;

import lombok.Builder;

import java.util.List;

@Builder
public record CourseRequestDTO(String title, Integer numberOfLessons, Float numberOfHours, String overview,
                               List<String> whatWillYouLearn, Float price, String tag) {
}
