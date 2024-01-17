package sparklab.tts.dto;

import lombok.Data;
import sparklab.tts.model.Role;

@Data
public class RegisterDTO {
    private String username;
    private String email;
    private String name;
    private String surname;
    private Role role;

}
