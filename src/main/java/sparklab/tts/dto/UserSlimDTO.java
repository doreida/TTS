package sparklab.tts.dto;

import lombok.Data;
import sparklab.tts.model.Role;

@Data
public class UserSlimDTO {
    private Long userId;
    private String name = null;
    private String surname = null;
    private String username = null;
    private String email = null;
    private Role role;
}
