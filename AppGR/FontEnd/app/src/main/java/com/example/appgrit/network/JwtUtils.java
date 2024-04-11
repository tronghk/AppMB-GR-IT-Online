//package com.example.appgrit.network;
//
//import io.jsonwebtoken.Claims;
//import io.jsonwebtoken.Jws;
//import io.jsonwebtoken.Jwts;
//import io.jsonwebtoken.SignatureAlgorithm;
//import io.jsonwebtoken.security.Keys;
//
//import java.security.Key;
//
//public class JwtUtils {
//
//    // Đoạn mã key
//    private static final Key key = Keys.secretKeyFor(SignatureAlgorithm.HS256);
//
//    public static String getUserIdFromToken(String accessToken) {
//        try {
//            Jws<Claims> claimsJws = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(accessToken);
//            Claims body = claimsJws.getBody();
//            return body.get("userId", String.class);
//        } catch (Exception e) {
//            e.printStackTrace();
//            return null;
//        }
//    }
//}
