package cs.sw.service;

import cs.sw.dto.RegisterDTO;
import cs.sw.dto.StudentDTO;
import cs.sw.entity.AppUser;

import java.util.List;

public interface UserService {
    AppUser createUser(RegisterDTO registerDTO);
    AppUser createUser(AppUser appUser);
    AppUser getSingleUser(Long userId);
    void checkUserExistence(String phoneNumber);
    AppUser findByEmail(String Email);
    String payCourse(Long studentId, Long courseId);
    List<StudentDTO> getAllStudents();
}
