package sparklab.tts.services;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import sparklab.tts.converters.*;
import sparklab.tts.dto.*;
import sparklab.tts.exceptions.NotFoundException;
import sparklab.tts.exceptions.NotValidFileException;
import sparklab.tts.model.Project;
import sparklab.tts.repositories.ProjectDocumentRepository;
import sparklab.tts.repositories.ProjectRepository;
import sparklab.tts.repositories.TaskRepository;
import sparklab.tts.repositories.UserRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProjectService {

    private  final ProjectRepository projectRepository;
    private  final ProjectDTOToProject toProject;
    private final ProjectToProjectDTO toProjectDTO;

    private final TaskRepository taskRepository;
    private final TaskToTaskDTO toTaskDTO;
    private final UserRepository userRepository;
    private final ProjectDocumentRepository projectDocumentRepository;
    private final ProjectDocumentToProjectDocumentDTO toProjectDocumentDTO;

    private final UserToUserSlimDTO toUserSlimDTO;
    private final TaskToTaskBasicDTO toTaskBasicDTO;



    public List<ProjectDTO> findAll() {
        return projectRepository.findAll().stream().map(project->toProjectDTO.convert(project)).collect(Collectors.toList());
    }



    public ProjectDTO findById(String id) {
        Long parseId;
        try {
            parseId = Long.parseLong(id);
        }catch (NumberFormatException e){
            throw new NumberFormatException("Project id: " + id + " can't be parsed!");
        }

        return toProjectDTO.convert(projectRepository.findById(parseId).orElseThrow(()-> new NotFoundException("Record with id: " + id + " not found!")));
    }


    public List<TaskDTO> findTaskByProject(String id) {
        Long parseId;
        try {
            parseId = Long.parseLong(id);
        }catch (NumberFormatException e){
            throw new NumberFormatException("Project id: " + id + " can't be parsed!");
        }
        return taskRepository.findByProjectId(parseId).stream().map(task->toTaskDTO.convert(task)).collect(Collectors.toList());
    }

    public List<UserSlimDTO> findMemberByProject(String id) {
        Long parseId;
        try {
            parseId = Long.parseLong(id);
        }catch (NumberFormatException e){
            throw new NumberFormatException("Project id: " + id + " can't be parsed!");
        }

        return userRepository.findMemberByProjects(parseId).stream().map(user->toUserSlimDTO.convert(user)).collect(Collectors.toList());

    }

    public ResponseEntity<?> saveOrUpdate(ProjectDTO projectDTO) {

        try {
            if(projectRepository.existsByName(projectDTO.getName()) && projectDTO.getId()==null){
                return new ResponseEntity<>("There is already a project with this name", HttpStatus.BAD_REQUEST);
            }else     {
                projectRepository.save(toProject.convert(projectDTO));

                return new ResponseEntity<>("Project saved successfully", HttpStatus.OK);}

        }

        catch(NotValidFileException e){
            return new ResponseEntity<>(e.getMessage(),HttpStatus.BAD_REQUEST);
        }

        catch(Exception e){
            return new ResponseEntity<>("Project could not be saved.", HttpStatus.BAD_REQUEST);
        }

    }

    public ResponseEntity<?> deleteProjectById(String id) {
        try {
            Long parseId = Long.parseLong(id);
            projectRepository.deleteById(parseId);
            return new ResponseEntity<>("Project deleted successfully", HttpStatus.OK);
        }catch (NumberFormatException e){
            return new ResponseEntity<>("Project id: " + id + " can't be parsed!",HttpStatus.BAD_REQUEST);
        }catch (EmptyResultDataAccessException e){
            return new ResponseEntity<>("Project with id "+id+" not found", HttpStatus.BAD_REQUEST);
        }
        catch(Exception e){
            return new ResponseEntity<>("Project could not be deleted", HttpStatus.BAD_REQUEST);
        }
    }

    public List<ProjectDocumentDTO> findAllDocuments(String id) {

        try {
           Long parseId = Long.parseLong(id);
            return projectDocumentRepository.findProjectDocumentByProject_Id(parseId).stream().map(projectDocument -> toProjectDocumentDTO.convert(projectDocument)).collect(Collectors.toList());
        }catch (NumberFormatException e){
            throw new NumberFormatException("Project id: " + id + " can't be parsed!");
        }catch (Exception e) {
            throw new RuntimeException("An error occurred while trying to find all project documents", e);
        }


    }

    public List<TaskBasicDTO> findTaskBasicByProject(String id) {

        try {
            Long parseId = Long.parseLong(id);
            return taskRepository.findByProjectId(parseId).stream().map(task -> toTaskBasicDTO.convert(task)).collect(Collectors.toList());
        } catch (NumberFormatException e) {
            throw new NumberFormatException("Project id: " + id + " can't be parsed!");
        } catch (Exception e) {
            throw new RuntimeException("An error occurred while trying to find task basic information by project", e);
        }

    }

    public ResponseEntity<?> deleteProjectMember(String id,String projectId) {

        try {
            Long userId = Long.parseLong(id);
            Long parseprojectId = Long.parseLong(projectId);
            projectRepository.deleteMember(userId,parseprojectId);
            return new ResponseEntity<>("Member deleted successfully", HttpStatus.OK);
        }catch (NumberFormatException e){
            return new ResponseEntity<>("Project id: " + id + " can't be parsed!",HttpStatus.BAD_REQUEST);
        }catch (EmptyResultDataAccessException e){
            return new ResponseEntity<>("Project with id "+id+" not found", HttpStatus.BAD_REQUEST);
        }
        catch(Exception e){
            return new ResponseEntity<>("Project member could not be deleted.", HttpStatus.BAD_REQUEST);
        }

    }

    public ResponseEntity<?> insertProjectMember(String userid, String projectId) {
        try {
            Long userId = Long.parseLong(userid);
            Long parseprojectId = Long.parseLong(projectId);
            projectRepository.insertMember(userId,parseprojectId);
            return new ResponseEntity<>("Member saved successfully!", HttpStatus.OK);
        }catch (NumberFormatException e){
            return new ResponseEntity<>("Record id: can't be parsed!",HttpStatus.BAD_REQUEST);
        }catch (EmptyResultDataAccessException e){
            return new ResponseEntity<>("Record not found.", HttpStatus.BAD_REQUEST);
        }
        catch(Exception e){
            return new ResponseEntity<>("Member could not be saved.Try again.", HttpStatus.BAD_REQUEST);
        }
    }
}
