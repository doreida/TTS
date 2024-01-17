package sparklab.tts.services;

import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import sparklab.tts.converters.*;
import sparklab.tts.dto.*;
import sparklab.tts.exceptions.NotFoundException;
import sparklab.tts.model.TimeTracker;
import sparklab.tts.model.User;
import sparklab.tts.repositories.ProjectRepository;
import sparklab.tts.repositories.TaskRepository;
import sparklab.tts.repositories.TimeTrackingRepository;
import sparklab.tts.repositories.UserRepository;

import javax.mail.MessagingException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {

    private static final long EXPIRE_TOKEN_AFTER_MINUTES = 60;
    private final UserRepository userRepository;
    private final ProjectRepository projectRepository;

    private final TaskRepository taskRepository;
    private final EmailService emailService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    private CustomUserDetailsService customUserDetailsService;

    private final UserToUserSlimDTO toUserSlimDTO;
    private final ProjectToProjectDTO toProjectDTO;
    private final UserToUserDTO toUserDTO;

    private final TaskToTaskDTO toTaskDTO;

    private final TimeTrackingRepository timeTrackingRepository;
    private final TimeTrackerToTimeTrackerDTO timeTrackerToTimeTrackerDTO;

    private static final Logger LOGGER = LogManager.getLogger(UserService.class);


    public List<UserSlimDTO> findAll() {
        return userRepository.findAll().stream().map(user -> toUserSlimDTO.convert(user)).collect(Collectors.toList());
    }

    public UserDTO findById(String id) {
        Long parseId;
        try {
            parseId = Long.parseLong(id);
        } catch (NumberFormatException e) {
            throw new NumberFormatException("Photo id: \"" + id + "\" can't be parsed!");
        }
        return toUserDTO.convert(userRepository.findById(parseId).orElseThrow(() -> new NotFoundException("Record with id: " + id + " not found!")));
    }


    private String generateToken() {
        UUID uuid = UUID.randomUUID();
        return String.valueOf(uuid);
    }

    private boolean tokenIsExpired(final LocalDateTime tokenCreationDateForgotPassword) {
        LocalDateTime now = LocalDateTime.now();
        Duration difference = Duration.between(tokenCreationDateForgotPassword, now);
        return difference.toMinutes() >= EXPIRE_TOKEN_AFTER_MINUTES;

    }

    public ResponseEntity<?> forgetPassword(String email) throws MessagingException {
        try {
            Optional<User> userOptional = userRepository.findUsersByEmail(email);
            if (!userOptional.isPresent()) {
                return new ResponseEntity<>("There is no user with this email address.", HttpStatus.BAD_REQUEST);
            }

            User user = userOptional.get();
            user.setForgetPasswordToken(generateToken());
            user.setForgetPasswordTokenCreationDate(LocalDateTime.now());
            userRepository.save(user);

            String link = "http://localhost:3000/tts/resetPassword/" + user.getForgetPasswordToken();

            if (customUserDetailsService.exists(email)) {
                emailService.send(user.getEmail(), emailService.buildResetEmail(user.getName(), link));
            }
            return new ResponseEntity<>(user.getForgetPasswordToken(), HttpStatus.OK);
        } catch (DataAccessException e) {
            LOGGER.error("Database error while processing forgetPassword: " + e.getMessage());
            return new ResponseEntity<>("A database error occurred while processing the request.", HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (Exception e) {
            LOGGER.error("Error in forgetPassword: " + e.getMessage());
            return new ResponseEntity<>("An error occurred while processing the password reset request.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<?> resetPassword(String uuid, ResetPasswordDTO resetPasswordDTO) {
        try {
            Optional<User> optionalUser = Optional.ofNullable(userRepository.findByForgetPasswordToken(uuid));
            if (!optionalUser.isPresent()) {
                return new ResponseEntity<>("The redirection link is invalid ", HttpStatus.NOT_ACCEPTABLE);
            }

            LocalDateTime tokenCreationDate = optionalUser.get().getForgetPasswordTokenCreationDate();
            if (tokenIsExpired(tokenCreationDate)) {
                return new ResponseEntity<>("The link has expired. Please complete once again the forgot password form.", HttpStatus.NOT_ACCEPTABLE);
            }

            User user = optionalUser.get();
            user.setPassword(bCryptPasswordEncoder.encode(resetPasswordDTO.getPassword()));
            user.setForgetPasswordToken(null);
            userRepository.save(user);
            return new ResponseEntity<>("Your password was successfully changed!", HttpStatus.OK);

        } catch (DataAccessException e) {
            LOGGER.error("Database error while processing resetPassword: " + e.getMessage());
            return new ResponseEntity<>("A database error occurred while processing the request.", HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (Exception e) {
            LOGGER.error("Unexpected error in resetPassword: " + e.getMessage());
            return new ResponseEntity<>("An unexpected error occurred.", HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    public List<ProjectDTO> findProjectByMember(String id) {
        try {
            Long parseId = Long.parseLong(id);
            return projectRepository.findProjectsByMembers(parseId).stream().map(project -> toProjectDTO.convert(project)).collect(Collectors.toList());
        } catch (NumberFormatException e) {
            throw new NumberFormatException("Member id: " + id + " can't be parsed!");
        } catch (Exception e) {
            throw new RuntimeException("An error occurred while finding projects by member", e);
        }

    }

    public List<TaskDTO> findTaskByAssignedBy(String id) {

        try {

            Long parseId = Long.parseLong(id);
            return taskRepository.findByAssignedTo(parseId).stream().map(task -> toTaskDTO.convert(task)).collect(Collectors.toList());
        } catch (NumberFormatException e) {
            throw new NumberFormatException("User id: " + id + " can't be parsed!");
        } catch (Exception e) {
            throw new RuntimeException("An error occurred while finding task by user assigned to", e);

        }

    }

    public List<TimeTrackerDTO> getAllTimeTrackersForUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User with id " + userId + " not found"));

        List<TimeTracker> timeTrackers = timeTrackingRepository.findByUser_Id(userId);
        return timeTrackers.stream()
                .map(timeTrackerToTimeTrackerDTO::convert)
                .collect(Collectors.toList());
    }


}






