package ramyunlab_be.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ramyunlab_be.config.jwt.JwtProperties;
import ramyunlab_be.entity.UserEntity;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

@Component
public class TokenProvider {
    @Autowired
    private JwtProperties jwtProperties;

    public String create(UserEntity user){
        Date expiredDate = Date.from(Instant.now().plus(1, ChronoUnit.DAYS));

        return Jwts.builder()
            .signWith(SignatureAlgorithm.HS512, jwtProperties.getSecretkey())
            .setSubject(String.valueOf(user.getIdx()))
            .setIssuer(jwtProperties.getIssuer())
            .setExpiration(expiredDate)
            .setIssuedAt(new Date())
            .compact();
    }

    public String validateAndGetUserId(String token){
        Claims claims = Jwts.parser()
            .setSigningKey(jwtProperties.getSecretkey())
            .parseClaimsJws(token)
            .getBody();

        return claims.getSubject();
    }
}
