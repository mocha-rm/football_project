package com.side.football_project.global.security.auth;

import com.side.football_project.domain.user.entity.User;
import com.side.football_project.domain.user.type.UserRole;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SessionAuthenticationService {
    private final AuthenticationManager authenticationManager;

    /**
     * 사용자 인증 및 세션 저장 (세션 만료시간 30분)
     * @param username 사용자 이름
     * @param password 비밀번호
     * @param request HTTP 요청 객체
     * @return 인증된 UserDetails 객체
     */
    public UserDetails authenticate(String username, String password, HttpServletRequest request) {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
        SecurityContextHolder.getContext().setAuthentication(authentication);

        HttpSession session = request.getSession(true);
        session.setAttribute("SPRING_SECURITY_CONTEXT", SecurityContextHolder.getContext());
        session.setMaxInactiveInterval(1800);

        return (UserDetails) authentication.getPrincipal();
    }

    public UserRole getUserRole() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof User user) {
            return user.getRole();
        }

        return null;
    }
}
