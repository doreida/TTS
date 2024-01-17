package sparklab.tts.converters;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import sparklab.tts.dto.UserDTO;
import sparklab.tts.model.User;


@Component
public class UserToUserDTO implements Converter<User, UserDTO> {

    public final RoleToRoleDTO toRoleDTO;

    public UserToUserDTO(RoleToRoleDTO toRoleDTO) {
        this.toRoleDTO = toRoleDTO;
    }

    @Override
    public UserDTO convert(User source) {

        if (source!=null){
            UserDTO userDTO = new UserDTO();
            userDTO.setUserId(source.getId());
            userDTO.setName(source.getName());
            userDTO.setSurname(source.getSurname());
            userDTO.setBirthdate(source.getBirthdate());
            userDTO.setPhone(source.getPhone());
            userDTO.setRole(source.getRole());
            userDTO.setEmail(source.getEmail());

            return userDTO;
        }
        return null;

    }
}
