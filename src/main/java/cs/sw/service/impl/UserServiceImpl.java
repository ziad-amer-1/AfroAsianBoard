package cs.sw.service.impl;

import cs.sw.dto.RegisterDTO;
import cs.sw.dto.StudentDTO;
import cs.sw.entity.AppUser;
import cs.sw.entity.Course;
import cs.sw.entity.Role;
import cs.sw.exception.UserAlreadyFoundException;
import cs.sw.mapper.UserMapper;
import cs.sw.repository.UserRepo;
import cs.sw.service.CourseService;
import cs.sw.service.UserService;
import cs.sw.utils.JwtUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static cs.sw.utils.SecurityConstant.TOKEN_PREFIX;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserRepo userRepo;
    private final PasswordEncoder passwordEncoder;
    private final CourseService courseService;
    private final JwtUtils jwtUtils;

    @Override
    public AppUser createUser(RegisterDTO registerDTO) {
        checkUserExistence(registerDTO.phoneNumber());
        return userRepo.save(createUserFromDTO(registerDTO));
    }

    @Override
    public AppUser createUser(AppUser appUser) {
        checkUserExistence(appUser.getPhoneNumber());
        return userRepo.save(appUser);
    }

    @Override
    public AppUser getSingleUser(Long userId) {
        return userRepo.findById(userId).orElseThrow(() -> new UsernameNotFoundException("user with id = " + userId + " not exist"));
    }

    @Override
    public void checkUserExistence(String phoneNumber) {
        if (userRepo.findByPhoneNumber(phoneNumber).isPresent()) {
            log.error("User with phoneNumber: [{}] already exist", phoneNumber);
            throw new UserAlreadyFoundException(" المستخدم صاحب رقم الهاتف " + phoneNumber + " موجود بالفعل ");
        }
    }


    @Override
    public AppUser findByEmail(String Email) {
        return userRepo.findByEmail(Email).orElseThrow(() -> new UsernameNotFoundException("حطأ في الايميل أو كلمة المرور"));
    }

    @Override
    @Transactional
    public String payCourse(Long courseId, String token) {
        Long studentId = extractUserIdFromToken(token).longValue();
        AppUser student = getSingleUser(studentId);
        Course course = courseService.getSingleCourse(courseId);

        List<Course> courses = student.getCourses();
        courses.add(course);
        student.setCourses(courses);

        return "Student [" + student.getName() + "] pay course [" + course.getTitle() + "]";
    }

    private Integer extractUserIdFromToken(String token) {
        token = token.split(TOKEN_PREFIX)[1];
        log.info("userId: {}", jwtUtils.extractClaims(token).get("user_id"));
        return (int) jwtUtils.extractClaims(token).get("user_id");
    }

    @Override
    @PreAuthorize("hasAuthority('MANAGER')")
    public List<StudentDTO> getAllStudents() {
        return UserMapper.fromUserToStudentDTO(userRepo.getAllStudents());
    }

    private AppUser createUserFromDTO(RegisterDTO registerDTO) {
        return AppUser
                .builder()
                .name(registerDTO.name())
                .phoneNumber(registerDTO.phoneNumber())
                .email(registerDTO.email())
                .role(Role.STUDENT)
                .password(passwordEncoder.encode(registerDTO.password()))
                .build();
    }
}