package com.instabook.utils;

import com.instabook.model.dos.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * generate and decode jwt
 */
public class JWTUtil implements Serializable {

    private static final String SECRET = "IUHHSIyiuyw87sj";

    public static final int CALENDAR_FIELD = Calendar.DATE;

    public static final int CALENDAR_INTERVAL = 30;

    public static String generateToken(User user) {
        if (user == null || user.getUserId() == null) {
            return null;
        }
        Date iatDate = new Date();
        // expire time
        Calendar nowTime = Calendar.getInstance();
        nowTime.add(CALENDAR_FIELD, CALENDAR_INTERVAL);
        Date expiresDate = nowTime.getTime();

        // header Map
        Map<String, Object> map = new ConcurrentHashMap<>();
        map.put("alg", "HS512");
        map.put("typ", "JWT");

        // build token
        // param backups {iss:Service, aud:APP}
        String token =
                Jwts.builder()
                        .setSubject("instabook")
                        .setHeader(map)
                        .claim("sequence_no", UUID.randomUUID())
                        .claim("user_id", String.valueOf(user.getUserId()))
                        .claim("user_name", user.getUserName())
                        .setId(UUID.randomUUID().toString())
                        .setIssuedAt(iatDate)
                        .setExpiration(expiresDate)
                        .signWith(SignatureAlgorithm.HS512, SECRET)
                        .compact();
        return "Bearer " + token;
    }

    public static Claims verifyToken(String token) {

        try {
            final Claims claims = Jwts.parser().setSigningKey(SECRET).parseClaimsJws(token).getBody();
            return claims;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Date getExpiresDate(String token) {
        return Jwts.parser().setSigningKey(SECRET).parseClaimsJws(token).getBody().getExpiration();
    }

    public static String parserToken(String token) {

        Jws<Claims> claims = Jwts.parser().setSigningKey(SECRET).parseClaimsJws(token);

        return claims.getBody().getSubject();

    }
}
