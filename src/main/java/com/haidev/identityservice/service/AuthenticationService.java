package com.haidev.identityservice.service;

import com.haidev.identityservice.dto.request.AuthenticationRequest;
import com.haidev.identityservice.dto.request.IntrospectRequest;
import com.haidev.identityservice.dto.response.AuthenticationResponse;
import com.haidev.identityservice.dto.response.IntrospectResponse;
import com.haidev.identityservice.entity.User;
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
import java.util.StringJoiner;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthenticationService {

    UserRepository userRepository;
    PasswordEncoder passwordEncoder;

    @NonFinal
    @Value("${jwt.signerKey}")
    String SIGNER_KEY;

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        var user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
        boolean authenticated = passwordEncoder.matches(request.getPassword(), user.getPassword());
        if (!authenticated) {
            throw new AppException(ErrorCode.UNAUTHENTICATED);
        }

        var token = generateToken(user);

        return AuthenticationResponse.builder()
                .token(token)
                .authenticated(true)
                .build();
    }

    private String generateToken(User user) {
        // header chứa thông tin thuật toán dùng để hash
        JWSHeader header = new JWSHeader(JWSAlgorithm.HS512);
        // payload chứa nội dung thông tin gửi đi (claim set) trong token: subject username, userid, ...
        // có thể gọi là payload thô
        JWTClaimsSet jwtClaimsSet = new JWTClaimsSet.Builder()
                .subject(user.getUsername())
                .issuer("haidev.com")  // định danh ai là người tạo token
                .issueTime(new Date())
                .expirationTime(new Date(
                        Instant.now().plus(1, ChronoUnit.HOURS).toEpochMilli()
                ))
                .claim("scope", buildScope(user))
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

    private String buildScope(User user) {
        StringJoiner joiner = new StringJoiner(" ");
//        if (!user.getRoles().isEmpty()) {
//            user.getRoles().forEach(joiner::add);
//        }
        return joiner.toString();
    }

    public IntrospectResponse introspect(IntrospectRequest request)
                                throws JOSEException, ParseException {
        var token = request.getToken();

        // MACVerifier là class của Nimbus để verify Token mà ký bằng HMAC
        JWSVerifier verifier = new MACVerifier(SIGNER_KEY.getBytes());
        // từ token chuyển thành object để dễ xử lý
        SignedJWT signedJWT = SignedJWT.parse(token);

        Date expirationTime = signedJWT.getJWTClaimsSet().getExpirationTime();
        // lấy header + payload trong JWT, chạy qua thuật toán (ví dụ HS256 = HMAC-SHA256) cùng với secret key.
        //Sau đó so sánh kết quả hash đó với signature có sẵn trong token.
        var verified = signedJWT.verify(verifier);

        return IntrospectResponse.builder()
                .isValid(verified && expirationTime.after(new Date()))
                .build();

    }

}
