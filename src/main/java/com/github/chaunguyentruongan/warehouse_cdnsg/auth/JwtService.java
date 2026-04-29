package com.github.chaunguyentruongan.warehouse_cdnsg.auth;

import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.JWSSigner;
import com.nimbusds.jose.JWSVerifier;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;


@Service
public class JwtService {

    @Value("${app.secret-key}")
    private String secretKey;

    public String generateToken(String username) throws JOSEException {
        JWSSigner singer = new MACSigner(secretKey.getBytes());

        // tìm kiếm user và lấy role

        JWTClaimsSet claimsSet = new JWTClaimsSet.Builder()
                .subject(username)
                .issuer("cdnsg-qlk")
                .expirationTime(new Date(System.currentTimeMillis() + 5 * 60 * 1000))
                .claim("role", "USER") // để tạm user, lấy role từ USER nhé
                .build();

        SignedJWT signedJWT = new SignedJWT(new JWSHeader(JWSAlgorithm.HS256), claimsSet);

        signedJWT.sign(singer);

        return signedJWT.serialize();

    }

    public JWTClaimsSet validateToken(String token) throws Exception {
        SignedJWT signedJWT = SignedJWT.parse(token);

        JWSVerifier verifier = new MACVerifier(secretKey.getBytes());

        if (!signedJWT.verify(verifier)) {
            throw new RuntimeException("Invalid signature");
        }

        if (signedJWT.getJWTClaimsSet().getExpirationTime().before(new Date())) {
            throw new RuntimeException("Token expired");
        }

        return signedJWT.getJWTClaimsSet();
    }
}
