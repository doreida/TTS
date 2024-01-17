package sparklab.tts.dto;

import lombok.Data;
import sparklab.tts.model.Role;

import java.time.LocalDate;
@Data
public class UserDTO {
    private Long userId;
    private String name;
    private String surname;
    private LocalDate birthdate;
    private String phone;
    private Role role;
    private String username;
    private String email;
    private String password;

}

