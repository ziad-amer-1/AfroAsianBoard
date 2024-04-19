package cs.sw.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Entity
@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Course {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private Integer numberOfLessons;
    private Float numberOfHours;
    private String overview;
    @ElementCollection(targetClass = String.class)
    private List<String> whatWillYouLearn;
    private Float price;
    private String tag;

    @ManyToMany(mappedBy = "courses")
    @JsonIgnore
    private List<AppUser> students;

}




