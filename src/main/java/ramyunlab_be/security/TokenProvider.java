package ramyunlab_be.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ramyunlab_be.config.jwt.JwtProperties;
import ramyunlab_be.entity.UserEntity;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

@Component
@Slf4j
public class TokenProvider {
    @Autowired
    private JwtProperties jwtProperties;

    public String create(UserEntity user) {
        Date expiredDate = Date.from(Instant.now().plus(1, ChronoUnit.DAYS));

        boolean isKakao = user.getUserId().startsWith("kakao_");

        return Jwts.builder()
            .signWith(SignatureAlgorithm.HS512, jwtProperties.getSecretkey())
            .setSubject(String.valueOf(user.getUserIdx()))
            .setIssuer(jwtProperties.getIssuer())
            .setExpiration(expiredDate)
            .setIssuedAt(new Date())
            .claim("isKaKao", isKakao)
            .compact();
    }

    public String validateAndGetUserIdx(String token) {
        Claims claims = Jwts.parser()
            .setSigningKey(jwtProperties.getSecretkey())
            .parseClaimsJws(token)
            .getBody();

        return claims.getSubject();
    }
}
