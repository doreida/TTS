package sparklab.tts.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class User extends BaseEntity implements UserDetails {

    private String name;
    private String surname;
    private LocalDate birthdate;
    private String phone;
    private String email;
    private String username;
    private String password;

    private boolean isEnabled;
    private boolean isAccountNonExpired = true;
    private boolean isAccountNonLocked = true;
    private boolean isCredentialsNonExpired = true;

    private String confirmationToken;
    private LocalDateTime tokenCreationDate;
    private LocalDateTime tokenConfirmationDate;

    private LocalDateTime forgetPasswordTokenCreationDate;
    private String forgetPasswordToken;


    @OneToMany(mappedBy="createdBy",cascade = CascadeType.ALL)
    private Set<Task> tasks_cb=new HashSet<>();

    @OneToMany(mappedBy="user",cascade = CascadeType.ALL)
    @JsonIgnoreProperties("user")
    private List<TimeTracker> timeTrackers;

    @ManyToOne
    @JoinColumn(name = "role_id", referencedColumnName = "id", nullable = false)
    private Role role;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(role.getName()));
        return authorities;
    }

    @Override
    public boolean isAccountNonExpired() {
        return false;
    }
    @Override
    public boolean isCredentialsNonExpired() {
        return false;
    }

    public void setEnabled(boolean enabled) { isEnabled = enabled; }
}
