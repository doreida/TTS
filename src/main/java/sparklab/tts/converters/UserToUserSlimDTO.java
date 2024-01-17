package sparklab.tts.converters;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import sparklab.tts.dto.UserSlimDTO;
import sparklab.tts.model.User;

import java.util.stream.Collectors;

@Component
public class UserToUserSlimDTO implements Converter<User, UserSlimDTO> {

    @Override
    public UserSlimDTO convert(User source) {
        if (source!=null){
            UserSlimDTO userSlimDTO = new UserSlimDTO();
            if (source.getId()!=null){
                userSlimDTO.setUserId(source.getId());
            }
            userSlimDTO.setName(source.getName());
            userSlimDTO.setSurname(source.getSurname());
            userSlimDTO.setUsername(source.getUsername());
            userSlimDTO.setEmail(source.getEmail());

            return userSlimDTO;

        }
        return null;
    }
}
