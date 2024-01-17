package sparklab.tts.converters;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import sparklab.tts.dto.RoleDTO;
import sparklab.tts.model.Role;

@Component
public class RoleToRoleDTO implements Converter<Role, RoleDTO> {

    @Override
    public RoleDTO convert(Role source) {
        if (source!=null){
            RoleDTO roleDTO = new RoleDTO();
            roleDTO.setId(source.getId());
            roleDTO.setRoleName(source.getName());
            return roleDTO;
        }
        return null;
    }
}
