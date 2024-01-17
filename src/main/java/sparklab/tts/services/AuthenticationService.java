package sparklab.tts.services;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import sparklab.tts.dto.LoginDTO;
import sparklab.tts.dto.LoginResponse;
import sparklab.tts.exceptions.LogoutFailedException;
import sparklab.tts.model.User;
import sparklab.tts.repositories.UserRepository;
import sparklab.tts.utils.JwtUtils;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final UserRepository userRepository;

    private final JwtUtils jwtUtils;
    private HttpSession session;

    private final AuthenticationProvider authenticationManager;

    private static final Logger logger = LogManager.getLogger(AuthenticationService.class);



    public Object login(LoginDTO loginDTO) {


        LoginResponse loginResponse;
        try {
            Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                    loginDTO.getEmail(), loginDTO.getPassword()));
            System.out.println(authentication);
            SecurityContextHolder.getContext().setAuthentication(authentication);
            String token = jwtUtils.generateAccessToken(userRepository.findUsersByEmailEnabled(loginDTO.getEmail()).get());
            loginResponse = setUserData(userRepository.findUsersByEmailEnabled(loginDTO.getEmail()).get(), token);
            return new ResponseEntity<>(loginResponse, HttpStatus.OK);

        } catch (BadCredentialsException e) {
            throw new BadCredentialsException("Login failed ! Invalid email or password.");
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new InternalError("We encountered an issue while processing your request. Please try again later.Thank you for your understanding.");
        }
    }


    public LoginResponse setUserData(User user, String token) {
        LoginResponse loginResponse = new LoginResponse();
        loginResponse.setId(user.getId());
        loginResponse.setEmail(user.getEmail());
        loginResponse.setName(user.getName());
        loginResponse.setSurname(user.getSurname());
        loginResponse.setRole(user.getRole().getName());
        loginResponse.setAccessToken(token);
        return loginResponse;
    }


    public String logout(HttpServletRequest request, String id) {

        try {
            Long parseId = Long.parseLong(id);
            if (parseId != null && userRepository.existsById(parseId)) {
                session = request.getSession(false);
                if(session != null)
                session.invalidate();
                SecurityContextHolder.clearContext();
                return "You have been successfully logged out.";
            }
            throw new LogoutFailedException("Logout failed. No user exists with this id");
        } catch (NumberFormatException e) {
            throw new NumberFormatException("User id: " + id + " can't be parsed!");
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new LogoutFailedException("Logout failed. Please try again later.");
        }
    }
}
