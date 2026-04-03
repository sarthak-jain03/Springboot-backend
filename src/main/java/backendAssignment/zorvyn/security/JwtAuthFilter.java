package backendAssignment.zorvyn.security;

import backendAssignment.zorvyn.entity.Role;
import backendAssignment.zorvyn.entity.User;
import backendAssignment.zorvyn.repository.UserRepository;
import backendAssignment.zorvyn.utils.AuthUtil;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

import java.io.IOException;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class JwtAuthFilter extends OncePerRequestFilter {

    private final UserRepository userRepository;
    private final AuthUtil authUtil;
    private final HandlerExceptionResolver handlerExceptionResolver;

    public JwtAuthFilter(UserRepository userRepository,
                         AuthUtil authUtil,
                         HandlerExceptionResolver handlerExceptionResolver) {
        this.userRepository = userRepository;
        this.authUtil = authUtil;
        this.handlerExceptionResolver = handlerExceptionResolver;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        try {
            String authHeader = request.getHeader("Authorization");

            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                filterChain.doFilter(request, response);
                return;
            }

            String token = authHeader.substring(7);
            Claims claims = authUtil.getAllClaims(token);

            String username = claims.getSubject();
            Long userId = claims.get("userId", Long.class);

            List<String> roles = claims.get("roles", List.class);

            Set<Role> roleSet = roles.stream()
                    .map(Role::valueOf)
                    .collect(Collectors.toSet());

            UserPrincipal principal = new UserPrincipal(
                    User.builder()
                            .id(userId)
                            .username(username)
                            .roles(roleSet)
                            .build()
            );

            List<GrantedAuthority> authorities = roleSet.stream()
                    .map(r -> (GrantedAuthority) new SimpleGrantedAuthority("ROLE_" + r.name()))
                    .collect(Collectors.toList());

            System.out.println(authorities);

            UsernamePasswordAuthenticationToken authentication =
                    new UsernamePasswordAuthenticationToken(
                            principal,
                            null,
                            authorities
                    );

            SecurityContextHolder.getContext().setAuthentication(authentication);

            filterChain.doFilter(request, response);

        } catch (Exception ex) {
            handlerExceptionResolver.resolveException(request, response, null, ex);
        }
    }
}