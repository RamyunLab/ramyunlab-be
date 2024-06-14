package ramyunlab_be.mail;

import lombok.Builder;
import lombok.Data;
import org.springframework.beans.factory.annotation.Value;

@Data
@Builder
public class EmailMessage {
    private String userId;

    private String userEmail;

    private String subject;

    private String text;
}
