package sparklab.tts.services;

import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import sparklab.tts.exceptions.NotFoundException;
import sparklab.tts.model.Role;
import sparklab.tts.repositories.RoleRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RoleService {
    private final RoleRepository roleRepository;
    private static final Logger logger = LogManager.getLogger(RoleService.class);

    public List<Role> findAll() {
        try{
            return roleRepository.findAll();
        }
        catch (EmptyResultDataAccessException e) {
            throw new NotFoundException("No roles found in the database.");
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new InternalError("We encountered an issue while processing your request. Please try again later. Thank you for your understanding.");
        }
    }
}
