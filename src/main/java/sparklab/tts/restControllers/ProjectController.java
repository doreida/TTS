package sparklab.tts.restControllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sparklab.tts.dto.*;
import sparklab.tts.services.ProjectService;

import java.util.List;
@RestController
@RequestMapping("tts/project")
public class ProjectController {

    private final ProjectService projectService;

    public ProjectController(ProjectService projectService) {
        this.projectService = projectService;
    }


    @GetMapping("/all")
    public List<ProjectDTO> findAllProjects(){

        return projectService.findAll();
    }

    @GetMapping("/{projectId}")
    public ProjectDTO findProjectById(@PathVariable String projectId){
        return projectService.findById(projectId);
    }


    @GetMapping("/{projectId}/tasks")
    public List<TaskDTO> findTaskByProject(@PathVariable String projectId){
        return projectService.findTaskByProject(projectId);
    }


    @GetMapping("/{projectId}/task")
    public List<TaskBasicDTO> findTaskBasicByProject(@PathVariable String projectId){
        return projectService.findTaskBasicByProject(projectId);
    }


    @GetMapping("/{projectId}/documents")
    public List<ProjectDocumentDTO> findAllDocuments(@PathVariable String projectId){

        return projectService.findAllDocuments(projectId);
    }

    @GetMapping("/{projectId}/members")
    public List<UserSlimDTO> findMemberByProject(@PathVariable String projectId){
        return  projectService.findMemberByProject(projectId);
    }


    @PostMapping
    public ResponseEntity<?> saveOrUpdate(@RequestBody ProjectDTO projectDTO){
        return projectService.saveOrUpdate(projectDTO);
    }

    @DeleteMapping("/{projectId}")
    public ResponseEntity<?> deleteProject(@PathVariable String projectId){

        return  projectService.deleteProjectById(projectId);
    }


    @DeleteMapping("/{userid}/{projectId}")

    public ResponseEntity<?> deleteMember(@PathVariable String userid,@PathVariable String projectId){

        return  projectService.deleteProjectMember(userid, projectId);
    }


    @PostMapping("/{userid}/{projectId}")

    public ResponseEntity<?> insertMember(@PathVariable String userid,@PathVariable String projectId){

        return  projectService.insertProjectMember(userid, projectId);
    }
}
