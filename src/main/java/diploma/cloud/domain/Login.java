package diploma.cloud.domain;

import jakarta.persistence.Entity;
import lombok.*;

@ToString
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Login {
    private String login;
    private String password;
}
