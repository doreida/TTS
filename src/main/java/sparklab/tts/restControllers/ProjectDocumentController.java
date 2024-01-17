package sparklab.tts.restControllers;

import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sparklab.tts.dto.ProjectDocumentDTO;
import sparklab.tts.services.ProjectDocumentService;

@RestController
@RequestMapping("tts/projectdocument")
public class ProjectDocumentController {

    private final ProjectDocumentService projectDocumentService;


    public ProjectDocumentController(ProjectDocumentService projectDocumentService) {
        this.projectDocumentService = projectDocumentService;
    }


    @PostMapping
    public ResponseEntity<?> saveFile(@ModelAttribute ProjectDocumentDTO projectDocumentDTO){
        return  projectDocumentService.saveFile(projectDocumentDTO);

    }


    @GetMapping("/{id}")
    public ProjectDocumentDTO findById(@PathVariable String id){
        return projectDocumentService.findById(id);
    }



    @GetMapping("/download/{id}")
    public ResponseEntity<Resource> download(@PathVariable String id){
        return projectDocumentService.download(id);
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteDocument(@PathVariable String id){

        return  projectDocumentService.deleteDocumentById(id);
    }

}
