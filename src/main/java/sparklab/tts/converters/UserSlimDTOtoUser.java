package sparklab.tts.converters;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import sparklab.tts.dto.UserSlimDTO;
import sparklab.tts.model.User;
import sparklab.tts.repositories.UserRepository;

@Component
public class UserSlimDTOtoUser implements Converter<UserSlimDTO, User> {

    public final UserRepository userRepository;

    public UserSlimDTOtoUser(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public User convert(UserSlimDTO source) {
        if (source != null) {
            User user = new User();
            if (source.getUserId() != null) {
                user.setId(source.getUserId());
            }

            user.setName(source.getName()!=null ? source.getName():userRepository.findById(source.getUserId()).get().getName());
            user.setSurname(source.getSurname()!=null?source.getSurname():userRepository.findById(source.getUserId()).get().getSurname());
            user.setUsername(source.getUsername()!=null?source.getUsername():userRepository.findById(source.getUserId()).get().getUsername());
            user.setEmail(source.getEmail()!=null? source.getEmail():userRepository.findById(source.getUserId()).get().getEmail());


            return user;
        }
        return null;

    }
}
