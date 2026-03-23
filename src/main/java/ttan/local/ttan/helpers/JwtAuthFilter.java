package ttan.local.ttan.helpers;
import org.springframework.stereotype.Component;
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
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;


@Component
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {
    private final JwtService jwtService;
    private final CustomUserDetailsService userDetailsService;
    private final ObjectMapper objectMapper;
    private static final Logger logger = LoggerFactory.getLogger(JwtAuthFilter.class);

    @Override
    protected boolean shouldNotFilter(
        @NonNull HttpServletRequest request
    ){
        String path = request.getServletPath();
        return path.startsWith("/api/v1/auth/login");
    }

    @Override
    public void doFilterInternal(
        @NonNull HttpServletRequest request,
        @NonNull HttpServletResponse response,
        @NonNull FilterChain filterChain) throws ServletException, IOException {

        final String authHeader = request.getHeader("Authorization");
        final String jwt;
        final String userId;

        // Bỏ qua nếu không có token, để Spring Security tự chặn theo config (nếu endpoint bị bảo vệ)
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        jwt = authHeader.substring(7);

        try {
            // Lấy userId sẽ kiểm tra luôn tính hợp lệ, định dạng, hết hạn, và issuer
            userId = jwtService.getUserIdFromToken(jwt);
            
            if (userId != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                UserDetails userDetails = this.userDetailsService.loadUserByUsername(userId);

                // Kiểm tra xem token có khớp với username không
                if (!jwtService.isValidToken(jwt, userDetails)) {
                    sendErrorResponse(request, response, HttpServletResponse.SC_UNAUTHORIZED, "Xác thực không thành công", "Token không khớp với thông tin người dùng");
                    return;
                }

                logger.info("User details loaded: {}", userDetails.getUsername());

                // Cấp quyền cho user vào SecurityContext
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        userDetails,
                        null,
                        userDetails.getAuthorities()
                );
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }

            // Chuyển request đi tiếp
            filterChain.doFilter(request, response);

        } catch (io.jsonwebtoken.security.SignatureException e) {
            sendErrorResponse(request, response, HttpServletResponse.SC_UNAUTHORIZED, "Xác thực không thành công", "Chữ ký Token không hợp lệ");
        } catch (io.jsonwebtoken.MalformedJwtException | IllegalArgumentException e) {
            sendErrorResponse(request, response, HttpServletResponse.SC_UNAUTHORIZED, "Xác thực không thành công", "Token không đúng định dạng JWT");
        } catch (io.jsonwebtoken.ExpiredJwtException e) {
            sendErrorResponse(request, response, HttpServletResponse.SC_UNAUTHORIZED, "Xác thực không thành công", "Token đã hết hạn sử dụng");
        } catch (io.jsonwebtoken.IncorrectClaimException e) {
            sendErrorResponse(request, response, HttpServletResponse.SC_UNAUTHORIZED, "Xác thực không thành công", "Token có payload (issuer/audience) không hợp lệ");
        } catch (io.jsonwebtoken.UnsupportedJwtException e) {
            sendErrorResponse(request, response, HttpServletResponse.SC_UNAUTHORIZED, "Xác thực không thành công", "Token không được hỗ trợ");
        } catch (Exception e) {
            sendErrorResponse(request, response, HttpServletResponse.SC_UNAUTHORIZED, "Xác thực không thành công", "Xảy ra lỗi khi xác thực Token");
        }
    }

    private void sendErrorResponse(
            HttpServletRequest request,
            HttpServletResponse response,
            int statusCode,
            String message,
            String details) throws IOException {
        
        response.setStatus(statusCode);
        response.setContentType("application/json;charset=UTF-8");
        java.util.Map<String, Object> errorDetails = new java.util.HashMap<>();
        errorDetails.put("status", statusCode);
        errorDetails.put("message", message);
        errorDetails.put("details", details);
        errorDetails.put("path", request.getServletPath());
        
        response.getWriter().write(objectMapper.writeValueAsString(errorDetails));
    }
}
