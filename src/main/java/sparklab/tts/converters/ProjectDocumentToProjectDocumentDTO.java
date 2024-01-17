package sparklab.tts.converters;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import sparklab.tts.dto.ProjectDocumentDTO;
import sparklab.tts.model.ProjectDocument;

@Component
public class ProjectDocumentToProjectDocumentDTO implements Converter<ProjectDocument, ProjectDocumentDTO> {

    @Override
    public ProjectDocumentDTO convert(ProjectDocument source) {
        if (source != null){
            ProjectDocumentDTO projectDocumentDTO=new ProjectDocumentDTO();
            projectDocumentDTO.setId(source.getId());
            projectDocumentDTO.setProjectId(source.getProject().getId());
            projectDocumentDTO.setFileName(source.getFileName());
            projectDocumentDTO.setFileType(source.getFileType());
            projectDocumentDTO.setDocument(source.getFile_upload());
            projectDocumentDTO.setDownloadUrl(generatedownloadURL(source));
            return projectDocumentDTO;
        }
        return null;
    }

    public String generatedownloadURL(ProjectDocument projectDocument){

        return ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/tts/projectdocument/download/")
                .path(String.valueOf(projectDocument.getId()))
                .toUriString();
    }
}
