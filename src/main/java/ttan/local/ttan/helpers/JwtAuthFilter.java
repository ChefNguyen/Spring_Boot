package ttan.local.ttan.helpers;

import org.springframework.stereotype.Component;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import ttan.local.ttan.modules.users.services.impl.UserService;
import org.springframework.web.filter.OncePerRequestFilter;
import ttan.local.ttan.services.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import ttan.local.ttan.modules.users.services.impl.CustomUserDetailsService;
import org.springframework.lang.NonNull;

@Component
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {
    private final JwtService jwtService;
    private final CustomUserDetailsService userDetailsService;
    private final Logger logger = LoggerFactory.getLogger(JwtAuthFilter.class);

    @Override
    public void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain) throws ServletException, IOException {
        logger.info("Filter is running for request: {}", request.getRequestURI());
        final String authHeader = request.getHeader("Authorization");
        final String jwt;
        final String userId;

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            logger.info("test");
            filterChain.doFilter(request, response);
            return;
        }

        jwt = authHeader.substring(7);
        // TODO: Handle exceptions typically thrown by JWT parsing (ExpiredJwtException
        // etc)
        userId = jwtService.getUserIdFromToken(jwt);
        if( userId != null && SecurityContextHolder.getContext().getAuthentication() == null ){
            UserDetails userDetails = this.userDetailsService.loadUserByUsername(userId);
            logger.info("User details: {}", userDetails.getUsername());

            // Cấp quyền cho user vào SecurityContext
            UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                    userDetails,
                    null,
                    userDetails.getAuthorities()
            );
            authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(authToken);
        }

        // QUAN TRỌNG: Luôn phải gọi dòng này để chuyển request đến Controller
        filterChain.doFilter(request, response);
    }
}
