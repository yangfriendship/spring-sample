package me.youzheng.springsecurity.security.util;

import com.google.common.collect.ImmutableList;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import java.security.Key;
import java.util.Date;
import me.youzheng.springsecurity.security.principal.JwtResult;
import me.youzheng.springsecurity.security.principal.UserContext;
import me.youzheng.springsecurity.security.principal.UserInfo;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

public class JwtProvider {

    public static final String SUB = "Authentication";
    public static final String AUTHENTICATION_KEY = "auth";
    public static final String HOST = "http//www.youzheng.me";
    public static final String TOKEN_PREFIX = "Bearer ";

    private final String secret;
    private final long expireTime;
    private Key key;

    public JwtProvider(String secret, Long expireTimePerSeconds) {
        this.secret = secret;
        this.expireTime = expireTimePerSeconds * 1000L;
        this.key = Keys.hmacShaKeyFor(secret.getBytes());
    }

    public String createToken(Authentication authentication) {

        UserContext context = (UserContext) authentication.getPrincipal();
        UserInfo user = context.getUserInfo();

        return TOKEN_PREFIX + Jwts.builder()
            .setSubject(SUB)
            .setIssuer(HOST)
            .claim("menuAuthGroupNo", user.getMenuAuthGroupNo())
            .claim("userNo", user.getUserNo())
            .claim("userId", user.getUserId())
            .signWith(key, SignatureAlgorithm.HS512)
            .setExpiration(parseToDate())
            .compact();
    }

    private Date parseToDate() {
        long now = (new Date().getTime());
        return new Date(now + this.expireTime);
    }

    public Authentication resolveToken(String token) {

        Claims claims = Jwts.parserBuilder()
            .setSigningKey(key)
            .build()
            .parseClaimsJws(token)
            .getBody();

        String userId = (String) claims.get("userId");
        Long userNo = (Long) claims.get("userNo");
        Long menuAuthGroupNo = (Long) claims.get("menuAuthGroupNo");
        UserInfo userInfo = UserInfo.builder()
            .userId(userId)
            .userNo(userNo)
            .menuAuthGroupNo(menuAuthGroupNo)
            .build();

        return getUserToken(userInfo);
    }

    private UsernamePasswordAuthenticationToken getUserToken(UserInfo user) {
        UserContext userContext = new UserContext(user);
        return new UsernamePasswordAuthenticationToken(userContext, null,
            userContext.getAuthorities());
    }

    public JwtResult validate(String token) {
        try {
            Claims claims = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
            return JwtResult.VALID;
        } catch (MalformedJwtException e) {
            return JwtResult.INVALID;
        } catch (ExpiredJwtException e) {
            return JwtResult.EXPIRED;
        } catch (RuntimeException e) {
            throw e;
        }
    }

}