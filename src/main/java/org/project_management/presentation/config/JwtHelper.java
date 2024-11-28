package org.project_management.presentation.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;

@Component
public class JwtHelper {
    @Value("${jwt.secret}")
    private String JWT_SECRET;

    public String extractUserEmail(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public Date extractIssuedAt(String token) {
        return extractClaim(token, Claims::getIssuedAt);
    }

    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String userEmail = extractUserEmail(token);
        final Date tokenExpirationDate = extractClaim(token, Claims::getExpiration);

        boolean usernameMatch = Objects.equals(userEmail, userDetails.getUsername());
        boolean tokenIsExpired = tokenExpirationDate.before(new Date(System.currentTimeMillis()));

        return usernameMatch && !tokenIsExpired;
    }

    public String generateInvitationToken(String email, String workspaceId) {
        Map<String, Object> extraClaims = new HashMap<>();
        extraClaims.put("email", email);
        extraClaims.put("workspaceId", workspaceId);
        return this.generateInvitationToken(extraClaims, email);
    }

    private String generateInvitationToken(Map<String, Object> extraClaims, String email) {
        return Jwts
                .builder()
                .setClaims(extraClaims)
                .setSubject(email)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24))
                .signWith(getSignInKey())
                .compact();
    }
    public boolean isInvitationTokenValid(String token) {
        final Date tokenExpirationDate = extractClaim(token, Claims::getExpiration);
        boolean tokenIsExpired = tokenExpirationDate.before(new Date(System.currentTimeMillis()));

        return  !tokenIsExpired;
    }
    public String extractWorkspaceId(String token) {
        return extractClaim(token, claims -> claims.get("workspaceId", String.class));
    }
    public String extractCompanyId(String token) {
        return extractClaim(token, claims -> claims.get("companyId", String.class));
    }
    public String generateToken(UserDetails userDetails) {
        return this.generateToken(new HashMap<>(), userDetails);
    }

    public String generateToken(Map<String, Object> extraClaims, UserDetails userDetails) {
        return Jwts
                .builder()
                .claims(extraClaims)
                .subject(userDetails.getUsername())
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24 * 14))
                .signWith(getSignInKey())
                .compact();
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts
                .parser()
                .verifyWith(getSignInKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    private SecretKey getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(JWT_SECRET);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
