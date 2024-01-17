package sparklab.tts.services;

import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import sparklab.tts.converters.ProjectDocumentDTOToProjectDocument;
import sparklab.tts.converters.ProjectDocumentToProjectDocumentDTO;
import sparklab.tts.dto.ProjectDocumentDTO;
import sparklab.tts.exceptions.NotFoundException;
import sparklab.tts.exceptions.NotValidFileException;
import sparklab.tts.model.ProjectDocument;
import sparklab.tts.repositories.ProjectDocumentRepository;

@Service
@RequiredArgsConstructor
public class ProjectDocumentService {
    private final ProjectDocumentRepository projectDocumentRepository;
    private final ProjectDocumentDTOToProjectDocument toProjectDocument;
    private final ProjectDocumentToProjectDocumentDTO toProjectDocumentDTO;


    public ResponseEntity<?> saveFile(ProjectDocumentDTO projectDocumentDTO) {

        try {
            projectDocumentRepository.save(toProjectDocument.convert(projectDocumentDTO));
            return new ResponseEntity<>("Document saved successfully", HttpStatus.OK);
        } catch (NotValidFileException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>("Document not saved successfully", HttpStatus.BAD_REQUEST);
        }
    }

    public ResponseEntity<Resource> download(String id) {

        try {
           Long parseId = Long.parseLong(id);
            return download(projectDocumentRepository.findById(parseId).orElseThrow(() -> new NotFoundException("Record with id: " + id + " not found!")));
        } catch (NumberFormatException e) {
            throw new NumberFormatException("File id: " + id + " can't be parsed!");
        } catch (Exception e) {
            throw new RuntimeException("An error occurred while trying to download the file", e);
        }


    }


    public ResponseEntity<Resource> download(ProjectDocument projectDocument)
    {
        return  ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(projectDocument.getFileType()))
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "projectdocument; filename=" + projectDocument.getFileName()
                                + "")
                .body(new ByteArrayResource(projectDocument.getFile_upload()));

    }

    public ProjectDocumentDTO findById(String id) {

        try {
            Long parseId = Long.parseLong(id);
            return toProjectDocumentDTO.convert(projectDocumentRepository.findById(parseId).orElseThrow(() -> new NotFoundException("Record with id: " + id + " not found!")));
        } catch (NumberFormatException e) {
            throw new NumberFormatException("File id: " + id + " can't be parsed!");
        }catch (Exception e) {
            throw new RuntimeException("An error occurred while trying to find the project document by id", e);
        }

    }

    public ResponseEntity<?> deleteDocumentById(String id) {

        try {
            Long parseId = Long.parseLong(id);
            projectDocumentRepository.deleteById(parseId);
            return new ResponseEntity<>("Document deleted successfully", HttpStatus.OK);
        } catch (NumberFormatException e) {
            return new ResponseEntity<>("Document id: " + id + " can't be parsed!", HttpStatus.BAD_REQUEST);
        } catch (EmptyResultDataAccessException e) {
            return new ResponseEntity<>("Record with id " + id + " not found", HttpStatus.BAD_REQUEST);

        }
    }
}
