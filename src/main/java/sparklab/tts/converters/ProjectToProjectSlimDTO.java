package sparklab.tts.converters;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import sparklab.tts.dto.ProjectSlimDTO;
import sparklab.tts.model.Project;

@Component
public class ProjectToProjectSlimDTO implements Converter<Project, ProjectSlimDTO> {

    @Override
    public ProjectSlimDTO convert(Project source) {
        if (source!=null){
            ProjectSlimDTO projectSlimDTO = new ProjectSlimDTO();
            if (source.getId()!=null){
                projectSlimDTO.setId(source.getId());
            }
            projectSlimDTO.setName(source.getName());
            return projectSlimDTO;
        }

        return null;
    }
}
