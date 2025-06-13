package com.example.template.auth.login.controller;
import com.example.template.api.apiResponse;
import com.example.template.auth.login.service.loginService;
import com.example.template.auth.login.vo.LoginRequest;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseCookie;
import java.util.Map;

import com.example.template.auth.JwtUtil;

@Slf4j
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class loginController {

    private final JwtUtil jwtUtil;

    private final loginService loginService;



    @PostMapping("/login")
    public ResponseEntity<apiResponse<?>> login(@RequestBody LoginRequest loginRequest, HttpServletResponse response) {

        String accessToken = loginService.login(loginRequest.getId(), loginRequest.getPassword());

        if (accessToken != null) {
            // ✅ Refresh Token 생성
            String refreshToken = jwtUtil.generateRefreshToken(loginRequest.getId());

            // ✅ Access Token 쿠키
            ResponseCookie accessCookie = ResponseCookie.from("accessToken", accessToken)
                    .httpOnly(true)
                    .secure(false) // 운영에서는 true
                    .path("/")
                    .sameSite("Lax")
                    .maxAge(60 * 60) // 1시간
                    .build();

            // ✅ Refresh Token 쿠키
            ResponseCookie refreshCookie = ResponseCookie.from("refreshToken", refreshToken)
                    .httpOnly(true)
                    .secure(false) // 운영에서는 true
                    .path("/")
                    .sameSite("Lax")
                    .maxAge(7 * 24 * 60 * 60) // 7일
                    .build();

            // ✅ 쿠키 헤더로 설정
            response.addHeader("Set-Cookie", accessCookie.toString());
            response.addHeader("Set-Cookie", refreshCookie.toString());

            // ✅ ApiResponse 형식으로 성공 메시지 반환
            return ResponseEntity.ok(apiResponse.success("로그인 성공"));
        }

        // ✅ 실패 시 ApiResponse 에러 형식으로 반환
        return ResponseEntity.status(401).body(apiResponse.error("아이디 또는 비밀번호가 잘못되었습니다.", 401));
    }

    @GetMapping("/check")
    public ResponseEntity<?> checkLoginStatus(HttpServletRequest request) {
        String token = jwtUtil.extractTokenFromCookie(request);
        if (token != null && jwtUtil.validateToken(token) != null) {
            return ResponseEntity.ok(apiResponse.success("토큰 유효"));
        } else {
            return ResponseEntity.status(401).body(apiResponse.error("토큰 만료됨", 401));
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<apiResponse<?>> logout(HttpServletResponse response) {
        // ✅ accessToken 제거
        ResponseCookie accessCookie = ResponseCookie.from("accessToken", "")
                .httpOnly(true)
                .secure(false) // 운영 시 true
                .path("/")
                .sameSite("Lax")
                .maxAge(0) // 즉시 삭제
                .build();

        // ✅ refreshToken 제거도 함께
        ResponseCookie refreshCookie = ResponseCookie.from("refreshToken", "")
                .httpOnly(true)
                .secure(false)
                .path("/")
                .sameSite("Lax")
                .maxAge(0)
                .build();

        // ✅ 쿠키 제거
        response.addHeader("Set-Cookie", accessCookie.toString());
        response.addHeader("Set-Cookie", refreshCookie.toString());

        // ✅ 통일된 응답 포맷으로 반환
        return ResponseEntity.ok(apiResponse.success("로그아웃 완료"));
    }
    
//    리프레쉬 토큰
@PostMapping("/refresh")
public ResponseEntity<apiResponse<String>> refreshToken(HttpServletRequest request) {
    String refreshToken = jwtUtil.extractRefreshTokenFromCookie(request);

    if (refreshToken == null) {
        return ResponseEntity.status(401).body(apiResponse.error("Refresh Token 없음", 401));
    }

    try {
        Claims claims = jwtUtil.validateToken(refreshToken).getBody();
        String username = claims.getSubject();

        // 실제로는 DB에서 권한을 다시 불러오는 것이 안전함
        String role = claims.get("role", String.class); // 없으면 직접 조회
        String newAccessToken = jwtUtil.generateToken(username, role);

        return ResponseEntity.ok(apiResponse.success(newAccessToken));

    } catch (JwtException e) {
        return ResponseEntity.status(401).body(apiResponse.error("Refresh Token 유효하지 않음", 401));
    }
}
}
