package com.main_project.api_gateway.configuration.filter;

import com.main_project.api_gateway.client.AuthServiceClient;
import com.main_project.api_gateway.exceptions.enums.ErrorCode;
import com.nimbusds.jose.JWSVerifier;
import com.nimbusds.jose.crypto.RSASSAVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.experimental.NonFinal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.KeyFactory;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.Date;
import java.util.Enumeration;
import java.util.List;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private AuthServiceClient authServiceClient;

    @NonFinal
    @Value("${publicKeyFileName}")
    String publicKeyFileName;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

//        System.out.println("GATEWAY");
//
//        System.out.println("---- Request Info ----");
//        System.out.println("Method: " + request.getMethod());
//        System.out.println("URI: " + request.getRequestURI());
//        System.out.println("Query String: " + request.getQueryString());
//        System.out.println("Remote Addr: " + request.getRemoteAddr());
//        System.out.println("Content Type: " + request.getContentType());
//
//        Enumeration<String> headerNames = request.getHeaderNames();
//        while (headerNames.hasMoreElements()) {
//            String headerName = headerNames.nextElement();
//            String headerValue = request.getHeader(headerName);
//            System.out.println(headerName + ": " + headerValue);
//        }

//        String path = request.getRequestURI();

//        if ("/login".equals(path)) {
//            filterChain.doFilter(request, response);
//            return;
//        }

        String authHeader = request.getHeader("Authorization");

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);

            if(!this.validate(token)) {
                sendError(response, ErrorCode.UNAUTHENTICATED);
                return;
            }

        } else {
            // Skip prelight request

//            if (!"OPTIONS".equalsIgnoreCase(request.getMethod())) {
//                sendError(response, ErrorCode.UNAUTHENTICATED);
//                return;
//            }
        }

        filterChain.doFilter(request, response);
    }

    private void sendError(HttpServletResponse response, ErrorCode errorCode)
            throws IOException {
        String body = String.format(
                "{\"code\": %d, \"message\": \"%s\"}",
                errorCode.getCode(),
                errorCode.getMessage());

        response.setStatus(errorCode.getHttpStatusCode().value());
        response.setContentType("application/json");
        response.getWriter().write(body);
    }

    public Boolean validate(String token) {
        try {
            SignedJWT signedJWT = SignedJWT.parse(token);
            JWSVerifier verifier = new RSASSAVerifier(loadPublicKey());

            Date expriredTime = signedJWT.getJWTClaimsSet().getExpirationTime();
            if (!(signedJWT.verify(verifier) && expriredTime.after(new Date()))) {
                return false;
            }
            return true;

        } catch (Exception e) {
            return false;
        }
    }

    private RSAPublicKey loadPublicKey() throws Exception {
        String publicKeyFilePath = System.getProperty("user.dir")+ "\\" + publicKeyFileName;

        String key = new String(Files.readAllBytes(Path.of(publicKeyFilePath)))
                .replace("-----BEGIN PUBLIC KEY-----", "")
                .replace("-----END PUBLIC KEY-----", "")
                .replaceAll("\\s", "");

        byte[] encoded = Base64.getDecoder().decode(key);

        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(encoded);

        return (RSAPublicKey) keyFactory.generatePublic(keySpec);
    }
}

