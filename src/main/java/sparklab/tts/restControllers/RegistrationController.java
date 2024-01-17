package sparklab.tts.restControllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import sparklab.tts.dto.ConfirmationRequest;
import sparklab.tts.dto.RegisterDTO;
import sparklab.tts.services.RegistrationService;

import javax.mail.internet.AddressException;

@RestController
@RequestMapping("/tts")
@RequiredArgsConstructor
public class RegistrationController {
    private final RegistrationService registrationService;

    @PostMapping("/registration")
    public ResponseEntity<?> registerUser(@RequestBody RegisterDTO registerDTO) {
        try {
            return registrationService.registerUser(registerDTO);
        } catch (AddressException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @PostMapping("savepassword/{token}")
    public String savePassword(@PathVariable("token") String token, @RequestBody ConfirmationRequest confirmationRequest) {
        try {
            return registrationService.savePassword(token, confirmationRequest);
        } catch(IllegalArgumentException  e){
            throw new IllegalArgumentException(e.getMessage());
        }
    }
}
