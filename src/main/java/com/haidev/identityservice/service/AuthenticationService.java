package com.haidev.identityservice.service;

import com.haidev.identityservice.dto.request.AuthenticationRequest;
import com.haidev.identityservice.dto.request.IntrospectRequest;
import com.haidev.identityservice.dto.response.AuthenticationResponse;
import com.haidev.identityservice.dto.response.IntrospectResponse;
import com.haidev.identityservice.exception.AppException;
import com.haidev.identityservice.exception.ErrorCode;
import com.haidev.identityservice.repository.UserRepository;
import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthenticationService {

    UserRepository userRepository;

    @NonFinal
    @Value("${jwt.signerKey}")
    String SIGNER_KEY;

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        var user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);
        boolean authenticated = passwordEncoder.matches(request.getPassword(), user.getPassword());
        if (!authenticated) {
            throw new AppException(ErrorCode.UNAUTHENTICATED);
        }

        var token = generateToken(request.getUsername());

        return AuthenticationResponse.builder()
                .token(token)
                .authenticated(true)
                .build();
    }

    private String generateToken(String username) {
        // header chứa thông tin thuật toán dùng để hash
        JWSHeader header = new JWSHeader(JWSAlgorithm.HS512);
        // payload chứa nội dung thông tin gửi đi (claim set) trong token: subject username, userid, ...
        // có thể gọi là payload thô
        JWTClaimsSet jwtClaimsSet = new JWTClaimsSet.Builder()
                .subject(username)
                .issuer("haidev.com")  // định danh ai là người tạo token
                .issueTime(new Date())
                .expirationTime(new Date(
                        Instant.now().plus(1, ChronoUnit.HOURS).toEpochMilli()
                ))
                .claim("customClaim", "Custom")
                .build();
        // từ claim set thành json object và bọc thành object Payload theo định dạng mà JWSObject yêu cầu
        Payload payload = new Payload(jwtClaimsSet.toJSONObject());
        // Ghép Header + Payload lại thành một object chuẩn để chuẩn bị ký
        JWSObject jwsObject = new JWSObject(header, payload);
        try {
            // ký: hash content với key
            // MACSigner là class của Nimbus để tạo chữ ký HMAC:
            //      cần một secret key ở dạng byte để hoạt động
            jwsObject.sign(new MACSigner(SIGNER_KEY.getBytes()));
            return jwsObject.serialize(); // chuyển thành string
        } catch (JOSEException e) {
            log.error("Error when generate token", e);
            throw new AppException(ErrorCode.UNCATEGORIZED_EXCEPTION);
        }
    }

    public IntrospectResponse introspect(IntrospectRequest request)
                                throws JOSEException, ParseException {
        var token = request.getToken();

        JWSVerifier verifier = new MACVerifier(SIGNER_KEY.getBytes());
        SignedJWT signedJWT = SignedJWT.parse(token);

        Date expirationTime = signedJWT.getJWTClaimsSet().getExpirationTime();

        var verified = signedJWT.verify(verifier);

        return IntrospectResponse.builder()
                .isValid(verified && expirationTime.after(new Date()))
                .build();

    }

}
