package sparklab.tts.dto;

import lombok.Data;

@Data
public class LoginResponse {
    private Long id;
    private String accessToken;
    private String email;
    private String name;
    private String surname;
    private String role;
}
