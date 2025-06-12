package com.example.template.auth.login.service;


import com.example.template.auth.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class loginService {

//    private final UserMapper userMapper;
    private final JwtUtil jwtUtil;

    public String login(String id, String password) {
//        User user = userMapper.findByUsername(username);

// 나중에 password 권한으로 바꾸기

        if (id != null && !id.isEmpty() && password != null && !password.isEmpty()) {
            return jwtUtil.generateToken(id, password);
        }

        return null; // 로그인 실패
    }
}
