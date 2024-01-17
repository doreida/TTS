package sparklab.tts.restControllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import sparklab.tts.services.RoleService;

@RestController
@RequestMapping("/tts/role")
@RequiredArgsConstructor
public class RoleController {
    private final RoleService roleService;

    @GetMapping
    public ResponseEntity<?> findAllRoles() {
        try {
            return new ResponseEntity<>(roleService.findAll(), HttpStatus.OK);

        } catch (Exception e) {
            return new ResponseEntity<>(roleService.findAll(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
