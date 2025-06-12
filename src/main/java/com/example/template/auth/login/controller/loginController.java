package com.example.template.auth.login.controller;
import com.example.template.auth.login.service.loginService;
import com.example.template.auth.login.vo.LoginRequest;
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
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest, HttpServletResponse response) {

        String token = loginService.login(loginRequest.getId(), loginRequest.getPassword());
asdasd
        if (token != null) {
            // ✅ JWT를 HttpOnly 쿠키로 내려보냄
            ResponseCookie cookie = ResponseCookie.from("accessToken", token)
                    .httpOnly(true)              // JS 접근 차단
                    .secure(false)               // HTTPS 사용 시 true
                    .path("/")                   // 모든 경로에 쿠키 적용
                    .sameSite("Lax")             // CORS 대응 시 "None" 사용 가능
                    .maxAge(60 * 60)             // 1시간
                    .build();

            response.addHeader("Set-Cookie", cookie.toString());

            return ResponseEntity.ok("로그인 성공"); // Body에 토큰은 없음
        }


        return ResponseEntity.status(401).body("Unauthorized");
    }


    @GetMapping("/check")
    public ResponseEntity<?> checkLoginStatus(HttpServletRequest request) {
        String token = jwtUtil.extractTokenFromCookie(request);
        if (token != null && jwtUtil.validateToken(token) != null) {
            return ResponseEntity.ok().body("authenticated");
        } else {
            return ResponseEntity.status(401).body("unauthorized");
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletResponse response) {
        ResponseCookie cookie = ResponseCookie.from("accessToken", "")
                .httpOnly(true)
                .secure(false)
                .path("/")
                .sameSite("Lax")
                .maxAge(0) // 쿠키 제거
                .build();

        response.setHeader("Set-Cookie", cookie.toString());

        return ResponseEntity.ok("로그아웃 완료");
    }
}
