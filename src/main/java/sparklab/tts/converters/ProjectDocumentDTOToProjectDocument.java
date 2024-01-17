package sparklab.tts.converters;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import sparklab.tts.dto.ProjectDocumentDTO;
import sparklab.tts.exceptions.NotFoundException;
import sparklab.tts.exceptions.NotValidFileException;
import sparklab.tts.model.Project;
import sparklab.tts.model.ProjectDocument;
import sparklab.tts.repositories.ProjectDocumentRepository;
import sparklab.tts.repositories.ProjectRepository;

import java.io.IOException;

@Component
public class ProjectDocumentDTOToProjectDocument implements Converter<ProjectDocumentDTO, ProjectDocument> {
    private final ProjectRepository projectRepository;
    private final ProjectDocumentRepository projectDocumentRepository;

    public ProjectDocumentDTOToProjectDocument(ProjectRepository projectRepository, ProjectDocumentRepository projectDocumentRepository) {
        this.projectRepository = projectRepository;
        this.projectDocumentRepository = projectDocumentRepository;
    }

    @Override
    public ProjectDocument convert(ProjectDocumentDTO source) {
        if (source == null) {
            return null;
        }

        if (source.getFile() == null) {
            throw new NotValidFileException("You should add a document to save the record");
        }

        ProjectDocument projectDocument = new ProjectDocument();

        try {
            if (source.getId() != null) {
                ProjectDocument existingDocument = projectDocumentRepository.findById(source.getId())
                        .orElseThrow(() -> new NotFoundException("Project Document with id: " + source.getId() + " does not exist!"));

                projectDocument.setId(existingDocument.getId());
                projectDocument.setFile_upload(existingDocument.getFile_upload());
                projectDocument.setFileName(existingDocument.getFileName());
                projectDocument.setFileType(existingDocument.getFileType());
                projectDocument.setProject(getProjectById(source.getProjectId()));
            } else {
                projectDocument.setFile_upload(source.getFile().getBytes());
                projectDocument.setFileName(StringUtils.cleanPath(source.getFile().getOriginalFilename()));
                projectDocument.setFileType(source.getFile().getContentType());
                projectDocument.setProject(getProjectById(source.getProjectId()));
            }
        } catch (IOException e) {
            throw new RuntimeException("Error while processing the file", e);
        }

        return projectDocument;
    }

    private Project getProjectById(Long projectId) {
        return (projectId == null) ? null : projectRepository.findById(projectId)
                .orElseThrow(() -> new NotFoundException("Project with id: " + projectId + " does not exist!"));
    }
}
