package sparklab.tts.converters;

import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import sparklab.tts.dto.ProjectDTO;
import sparklab.tts.model.Project;

import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class ProjectToProjectDTO  implements Converter<Project, ProjectDTO> {


    private final UserToUserSlimDTO toUserSlimDTO;
    private  final ProjectDocumentToProjectDocumentDTO toProjectDocumentDTO;


    @Override
    public ProjectDTO convert(Project source) {

        if (source != null){
            ProjectDTO projectDTO=new ProjectDTO();
            projectDTO.setId(source.getId());
            projectDTO.setDescription(source.getDescription());
            projectDTO.setName(source.getName());
            projectDTO.setMembers(source.getMembers().stream().map(user->toUserSlimDTO.convert(user)).collect(Collectors.toList()));
            projectDTO.setDocuments(source.getDocuments().stream().map(projectDocument -> toProjectDocumentDTO .convert(projectDocument)).collect(Collectors.toList()));
            projectDTO.setCreated_date(source.getCreatedDate());
            projectDTO.setCreatedBy(toUserSlimDTO.convert(source.getCreatedBy()));

            return projectDTO;
        }
        return null;

    }
}
