package com.github.chaunguyentruongan.warehouse_cdnsg.auth;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.github.chaunguyentruongan.warehouse_cdnsg.auth.dto.LoginRequest;
import com.github.chaunguyentruongan.warehouse_cdnsg.auth.dto.LoginResponse;
import com.github.chaunguyentruongan.warehouse_cdnsg.modules.user.User;
import com.github.chaunguyentruongan.warehouse_cdnsg.modules.user.UserService;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.JWSSigner;
import com.nimbusds.jose.JWSVerifier;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class JwtService {

    @Value("${app.secret-key}")
    private String secretKey;

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    public LoginResponse login(LoginRequest request) throws JOSEException {
        User user = userService.findByEmailOrUsername(request.getRequest());
        if (user == null) {
            return null;
        }

        String dbPassword = user.getPassword();

        if (!passwordEncoder.matches(request.getPassword(), dbPassword)) {
            return new LoginResponse();
        }

        JWSSigner signer = new MACSigner(secretKey.getBytes());

        JWTClaimsSet claimsSet = new JWTClaimsSet.Builder()
                .subject(user.getEmail())
                .issuer("cdnsg-qlk")
                .expirationTime(new Date(System.currentTimeMillis() + 5 * 60 * 1000))
                .claim("role", "USER")
                .claim("permission", List.of(user.getPermissions().stream().map(p -> p.getName())))
                .build();

        SignedJWT signedJWT = new SignedJWT(new JWSHeader(JWSAlgorithm.HS256), claimsSet);

        signedJWT.sign(signer);

        String token = signedJWT.serialize();

        LoginResponse response = new LoginResponse();
        response.setToken(token);

        return response;

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
