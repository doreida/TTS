package sparklab.tts.restControllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sparklab.tts.dto.*;
import sparklab.tts.services.UserService;

import javax.mail.MessagingException;
import java.util.List;

@RestController
@RequestMapping("tts/")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("findAll")
    private List<UserSlimDTO> findAll() {
        return userService.findAll();
    }

    @GetMapping("/{id}")
    public UserDTO findUserById(@PathVariable String id){
        return userService.findById(id);
    }

    @PostMapping("forgetPassword/{email}")
    public ResponseEntity<?> forgetPassword(@PathVariable String email) {
        try {
            return userService.forgetPassword(email);
        } catch (MessagingException e) {
            return new ResponseEntity<>("An error occurred while sending the password reset email.", HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @PutMapping("resetPassword/{token}")
    public ResponseEntity<?> resetPassword(@PathVariable(value = "token") String token, @RequestBody ResetPasswordDTO resetPasswordDTO) {
        try {
            return userService.resetPassword(token, resetPasswordDTO);
        } catch (Exception e) {
            return new ResponseEntity<>("An error occurred while resetting the password.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{id}/projects")
    public ResponseEntity<?>findProjectByMember(@PathVariable String id){
       try {
           return new ResponseEntity<>(userService.findProjectByMember(id), HttpStatus.OK);
       } catch (Exception e) {
           return new ResponseEntity<>("An error occurred while trying to find project by member", HttpStatus.INTERNAL_SERVER_ERROR);
       }

    }
    @GetMapping("/{id}/tasks")
    public ResponseEntity<?> findTaskByAssignedBy(@PathVariable String id){
        try {
            return new ResponseEntity<>(userService.findTaskByAssignedBy(id), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity <>("An error occurred while trying to find tasks by assigned to field", HttpStatus.INTERNAL_SERVER_ERROR);
    }

    }

    @GetMapping("/{userId}/time-trackers")
    public ResponseEntity<List<TimeTrackerDTO>> getAllTimeTrackersForUser(@PathVariable Long userId) {
        List<TimeTrackerDTO> timeTrackers = userService.getAllTimeTrackersForUser(userId);
        return new ResponseEntity<>(timeTrackers, HttpStatus.OK);
    }
}
