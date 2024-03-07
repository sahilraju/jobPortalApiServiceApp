package com.restapi.jobPortal.auth;

import io.jsonwebtoken.Claims;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

import javax.crypto.SecretKey;
import javax.xml.bind.DatatypeConverter;

import org.springframework.stereotype.Service;

import java.util.Date;
import java.security.*;

@Service
public class JwtUtil implements JwtUtilInterface {
	
    // Secret key for signing JWTs
    private static String SECRET_KEY; // This should be securely stored and not hard-coded
    
    private static final char[] HEX_ARRAY = "0123456789ABCDEF".toCharArray();

    @Override
	public void setSECRET_KEY(String sECRET_KEY) { 
		SECRET_KEY = sECRET_KEY; 
	}

    @Override
	//Secret key generate
    public String generateSecretKeyAsString() { 
        byte[] keyBytes = new byte[32]; // 256 bits
        try {
            SecureRandom secureRandom = SecureRandom.getInstanceStrong(); // Use a strong random number generator
            secureRandom.nextBytes(keyBytes);
            return bytesToHex(keyBytes);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null; 
        }
    }

    @Override
    public String bytesToHex(byte[] bytes) {
        char[] hexChars = new char[bytes.length * 2];
        for (int j = 0; j < bytes.length; j++) {
            int v = bytes[j] & 0xFF;
            hexChars[j * 2] = HEX_ARRAY[v >>> 4];
            hexChars[j * 2 + 1] = HEX_ARRAY[v & 0x0F];
        }
        return new String(hexChars);
    }
    
    @Override
    // Generate a JWT token
    public String generateToken(String username) {
        // Set expiration time
        Date expirationDate = new Date(System.currentTimeMillis() + 3600000); // 1 hour
 
        // Create JWT token
        SecretKey key = Keys.hmacShaKeyFor(DatatypeConverter.parseBase64Binary(SECRET_KEY));
        String token = Jwts.builder()
                .setSubject(username)
                .setExpiration(expirationDate)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
        return token;
    }

    @Override
    // Validate JWT token
    public boolean validateToken(String token) {
        try {
            // Parse the token
            SecretKey key = Keys.hmacShaKeyFor(DatatypeConverter.parseBase64Binary(SECRET_KEY));
            Claims claims = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();

            // Token is valid
            return true;  
        } catch (Exception e) {
            // Token is invalid 
            return false; 
        } 
    }
     
    @Override
    public String extractTokenFromHeader(String authorizationHeader) {
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            // Extract the JWT token
            return authorizationHeader.substring(7); // Remove 'Bearer ' prefix
        } else {
            return null;
        }
    }
}
