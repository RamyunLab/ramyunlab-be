package ramyunlab_be.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import ramyunlab_be.mail.EmailMessage;
import ramyunlab_be.repository.UserRepository;

@Slf4j
@Service
@PropertySource("classpath:application-dev.yml")
public class EmailService {

    private final UserRepository userRepository;
    private final JavaMailSender javaMailSender;
    private final TemplateEngine templateEngine;

    @Autowired
    public EmailService(UserRepository userRepository,
                        JavaMailSender javaMailSender,
                        TemplateEngine templateEngine) {
        this.userRepository = userRepository;
        this.javaMailSender = javaMailSender;
        this.templateEngine = templateEngine;
    }

    @Value("${spring.mail.username}@naver.com" )
    private String to;

    @Value("${spring.mail.username}@naver.com" )
    private String from;

    public MimeMessage sendEmail(EmailMessage emailMessage,
                                 final String userIdx) throws MessagingException, UnsupportedClassVersionError{
        userRepository.findByUserIdx(Long.valueOf(userIdx))
            .orElseThrow(() -> new RuntimeException("로그인을 진행해주세요."));


        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        String title = "RamyunLab 건의사항 : " + emailMessage.getSubject() ;
        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, false, "UTF-8");
        mimeMessageHelper.setFrom(from);
        mimeMessageHelper.setTo(to);
        mimeMessageHelper.setSubject(title);
        Context context = new Context();
        context.setVariable("userEmail", emailMessage.getUserEmail());
        context.setVariable("userId", emailMessage.getUserId());
        context.setVariable("text", emailMessage.getText());
        String content = templateEngine.process("mail", context);
        mimeMessageHelper.setText(content, true);



        javaMailSender.send(mimeMessage);
        return mimeMessage;
    }

}