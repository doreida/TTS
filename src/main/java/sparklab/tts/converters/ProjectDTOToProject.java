package sparklab.tts.converters;

import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import sparklab.tts.dto.ProjectDTO;
import sparklab.tts.exceptions.NotFoundException;
import sparklab.tts.model.Project;
import sparklab.tts.repositories.ProjectRepository;
import sparklab.tts.repositories.UserRepository;


import java.time.LocalDateTime;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class ProjectDTOToProject implements Converter<ProjectDTO, Project> {


    private final UserRepository userRepository;
    private final ProjectRepository projectRepository;

    private final UserSlimDTOtoUser  toUser;
    private final ProjectDocumentDTOToProjectDocument toProjectDocument;


    @Override
    public Project convert(ProjectDTO source) {
        if (source != null) {
            Project project = new Project();

            if (source.getId() != null) {
                Optional<Project> existingProject = projectRepository.findById(source.getId());
                if (existingProject.isPresent()) {
                    project = existingProject.get();
                } else {
                    throw new NotFoundException("Project with id: " + source.getId() + " does not exist!");
                }
            } else {
                project.setCreatedDate(LocalDateTime.now());
                project.setCreatedBy(toUser.convert(source.getCreatedBy()));

                if (source.getDocuments() != null) {
                    project.setDocuments(source.getDocuments().stream().map(toProjectDocument::convert).collect(Collectors.toSet()));
                }
            }

            project.setName(source.getName());
            project.setDescription(source.getDescription());

            if (source.getMembers() != null) {
                project.setMembers(source.getMembers().stream().map(toUser::convert).collect(Collectors.toSet()));
            } else if (source.getId() != null) {
                project.setMembers(project.getMembers());
            }

            return project;
        }
        return null;
    }

}


