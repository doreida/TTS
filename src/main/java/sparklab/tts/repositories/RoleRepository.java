package sparklab.tts.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sparklab.tts.model.Role;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
}
