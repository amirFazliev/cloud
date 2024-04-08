package diploma.cloud.domain;

import lombok.*;

import java.io.Serializable;

@ToString
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class File implements Serializable {
    private String hash;
    private String filename;
}
