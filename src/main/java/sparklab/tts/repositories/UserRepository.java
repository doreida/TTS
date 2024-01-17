package sparklab.tts.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import sparklab.tts.model.TimeTracker;
import sparklab.tts.model.User;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User,Long> {


    boolean existsByEmail(String email);
    Optional<User> findUsersByEmail(String email);

    User findUserByConfirmationToken(String token);

    @Query(value = "select * from user u where u.email=:email AND u.is_enabled=1", nativeQuery = true)
    Optional<User> findUsersByEmailEnabled(String email);

    User findByForgetPasswordToken(String forgetPasswordToken);

    @Query(value="select * from user where id in(Select user_id from project_member  where project_id=:id)",nativeQuery = true)
    List<User> findMemberByProjects(@Param("id") Long id);


}
