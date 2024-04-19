package cs.sw.mapper;

import cs.sw.dto.StudentDTO;
import cs.sw.entity.AppUser;
import cs.sw.entity.Role;

import java.util.ArrayList;
import java.util.List;

public class UserMapper {

    public static List<StudentDTO> fromUserToStudentDTO(List<AppUser> users) {
        List<StudentDTO> studentDTOS = new ArrayList<>();
        for (AppUser user : users) {
            studentDTOS.add(fromUserToStudentDTO(user));
        }
        return studentDTOS;
    }

    public static StudentDTO fromUserToStudentDTO(AppUser user) {
        return StudentDTO
                .builder()
                .name(user.getName())
                .phoneNumber(user.getPhoneNumber())
                .email(user.getEmail())
                .courses(user.getCourses())
                .build();
    }

}
