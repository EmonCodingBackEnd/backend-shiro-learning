package com.coding.shiro.springboot.example03.common.jwt;

import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.shiro.codec.Hex;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.exceptions.SignatureVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;
import com.auth0.jwt.interfaces.Verification;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * 自定义jwtToken管理者
 */
@Slf4j
@Component
@EnableConfigurationProperties({JwtProperties.class})
@RequiredArgsConstructor
public class JwtTokenManager {

    private final JwtProperties jwtProperties;

    /** Token在HTTP请求头中存在的具体属性. */
    public static final String TOKEN_HEADER = "Authorization";

    /** Token值前缀. */
    public static final String TOKEN_PREFIX = "Bearer "; // ˈbeərə(r)

    public static final String $X_APP_SESSION_EXP = "$x-app-session-exp";

    public String fetchToken(String authHeader) {
        String authToken = null;
        if (authHeader != null && authHeader.startsWith(TOKEN_PREFIX)) {
            authToken = authHeader.substring(TOKEN_PREFIX.length()); // The part after "Bearer "
        }
        return authToken;
    }

    /**
     * 签发令牌
     * 
     * @param sessionId - jwt的唯一身份标识
     * @param userId - jwt所面向的用户
     * @param claims - 构建非隐私信息
     * @param ttlInMillis - 毫秒单位的有效时间
     * @return jwt令牌
     */
    @SuppressWarnings(value = {"unchecked", "rawtypes"})
    public String issueJwtToken(String sessionId, String userId, Map<String, ?> claims, long ttlInMillis) {
        String token;
        if (CollectionUtils.isEmpty(claims)) {
            claims = new HashMap<>();
        }

        // 获取当前时间
        long nowInMillis = System.currentTimeMillis();
        // 获取加密签名
        String hexEncodedSecuretKey =
            Hex.encodeToString(jwtProperties.getHexEncodedSecretKey().getBytes(StandardCharsets.UTF_8));
        // 构建令牌
        Algorithm algorithm = Algorithm.HMAC256(hexEncodedSecuretKey);
        JWTCreator.Builder builder = JWT.create();
        builder.withIssuer(jwtProperties.getIssuer()); // jwt签发者
        builder.withIssuedAt(new Date(nowInMillis)); // jwt的签发时间
        builder.withSubject(userId);// jwt所面向的用户，放登录的用户名等
        builder.withJWTId(sessionId); // jwt的唯一身份标识

        // 构建非隐私信息
        claims.forEach((key, value) -> {
            if (null == value) {
                builder.withNullClaim(key);
            } else if (value instanceof Boolean) {
                builder.withClaim(key, (Boolean)value);
            } else if (value instanceof Integer) {
                builder.withClaim(key, (Integer)value);
            } else if (value instanceof Long) {
                builder.withClaim(key, (Long)value);
            } else if (value instanceof Double) {
                builder.withClaim(key, (Double)value);
            } else if (value instanceof String) {
                builder.withClaim(key, (String)value);
            } else if (value instanceof Date) {
                builder.withClaim(key, (Date)value);
            } else if (value instanceof Instant) {
                builder.withClaim(key, (Instant)value);
            } else if (value instanceof Map) {
                builder.withClaim(key, (Map)value);
            } else if (value instanceof List) {
                builder.withClaim(key, (List<?>)value);
            } else if (value instanceof String[]) {
                builder.withArrayClaim(key, (String[])value);
            } else if (value instanceof Integer[]) {
                builder.withArrayClaim(key, (Integer[])value);
            } else if (value instanceof Long[]) {
                builder.withArrayClaim(key, (Long[])value);
            } else {
                throw new IllegalArgumentException("Claim values must only be of types Map, List, Boolean, Integer, "
                    + "Long, Double, String, Date, Instant, and Null");
            }
        });

        if (ttlInMillis > 0) {
            long expInMillis = nowInMillis + ttlInMillis;
            builder.withExpiresAt(new Date(expInMillis));
        }

        token = builder.sign(algorithm);
        return token;
    }

    /**
     * 解析令牌：忽略是否已过期，只要是合法的（符合jwt规范的）jwtToken即可。
     *
     * 因为jwt的header.payload.signature的前两部分，仅仅是base64urlencoder编码，等效于明文。
     * 
     * @param jwtToken - 令牌
     * @return - 令牌中携带的非隐私数据
     */
    public DecodedJWT decodeJwtToken(String jwtToken) {
        DecodedJWT jwt;
        try {
            jwt = JWT.decode(jwtToken);
            return jwt;
        } catch (JWTDecodeException e) {
            log.error("token解码失败！", e);
            throw e;
        }
    }

    /**
     * 校验令牌
     * 
     * @param jwtToken 令牌字符串
     * @param expireLeewayInSeconds 令牌过期的容忍时间，过期时间不大于该值，认为有效
     * @return - 如果返回空字符串，表示成功；否则，返回错误原因。
     */
    public String verifyJwtToken(String jwtToken, long expireLeewayInSeconds) {
        String verifyResult = "";
        try {
            String hexEncodedSecuretKey =
                Hex.encodeToString(jwtProperties.getHexEncodedSecretKey().getBytes(StandardCharsets.UTF_8));
            Algorithm algorithm = Algorithm.HMAC256(hexEncodedSecuretKey);
            Verification verification = JWT.require(algorithm);
            if (expireLeewayInSeconds > 0) {
                verification.acceptExpiresAt(expireLeewayInSeconds);
            }
            JWTVerifier verifier = verification.build(); // Reusable verifier instance
            verifier.verify(jwtToken);
            return verifyResult;
        } catch (JWTVerificationException e) {
            log.error("token验证异常", e);
            if (e instanceof TokenExpiredException) {
                verifyResult = String.format("token已过期:%s", e.getMessage());
            } else if (e instanceof SignatureVerificationException) {
                verifyResult = String.format("token签名无效:%s", e.getMessage());
            } else if (e instanceof JWTDecodeException) {
                verifyResult = String.format("token解码失败:%s", e.getMessage());
            } else {
                // Invalid signature/claims
                verifyResult = String.format("token验证失败:%s", e.getMessage());
            }
            return verifyResult;
        }
    }

    public static void main(String[] args) {
        JwtTokenManager jwtTokenManager = new JwtTokenManager(new JwtProperties());
        String jwtToken =
            jwtTokenManager.issueJwtToken("61e3af70-8333-4f94-8654-6116b4abdbe6", "emon", null, 3600 * 1000);
        System.out.println(jwtToken);
    }
}
