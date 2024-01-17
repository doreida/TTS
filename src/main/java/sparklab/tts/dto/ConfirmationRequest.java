package sparklab.tts.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ConfirmationRequest {

    private String password;
    private LocalDateTime confirmationDate = LocalDateTime.now();
}
