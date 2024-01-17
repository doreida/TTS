package sparklab.tts.repositories;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import sparklab.tts.model.Project;

import java.util.List;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Long> {

    @Query(value = "select * from project where id in(Select project_id from project_member  where user_id=:id)", nativeQuery = true)
    List<Project> findProjectsByMembers(@Param("id") Long id);


    @Modifying
    @Transactional
    @Query(value = "DELETE c from project_member c where c.user_id =:parseId AND c.project_id=:parseprojectId", nativeQuery = true)
    void deleteMember(Long parseId, Long parseprojectId);


    @Modifying
    @Transactional
    @Query(value = "Insert INTO project_member (user_id,project_id) values(?1,?2) ", nativeQuery = true)
    void insertMember(Long parseId, Long parseprojectId);


    Boolean existsByName(String name);

}

