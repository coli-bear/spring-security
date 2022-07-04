package study.cb.security.security.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import study.cb.security.domain.AccountDto;
import study.cb.security.security.token.AjaxAuthenticationToken;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component("ajaxLoginProcessingFilter")
public class AjaxLoginProcessingFilter extends AbstractAuthenticationProcessingFilter {
    public static final String AJAX_HEADER = "X-Requested-With";

    private ObjectMapper objectMapper = new ObjectMapper();

    public AjaxLoginProcessingFilter() {
        // url 설정
        super(new AntPathRequestMatcher("/api/login"));
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
            throws AuthenticationException, IOException, ServletException {
        if (isAjax(request)) {
            throw new IllegalStateException("Authentication is not supported");
        }

        AccountDto accountDto = objectMapper.readValue(request.getReader(), AccountDto.class);
        if (!StringUtils.hasText(accountDto.getUsername()) || !StringUtils.hasText(accountDto.getPassword())) {
            throw new IllegalStateException("Username or Password is empty");
        }

        // Ajax용 토큰 생성
        AjaxAuthenticationToken authenticationToken =  new AjaxAuthenticationToken(
                accountDto.getUsername(),
                accountDto.getPassword()
        );

        return getAuthenticationManager().authenticate(authenticationToken);
    }

    private boolean isAjax(HttpServletRequest request) {
        if ("XMLHttpRequest".equals(request.getHeader(AJAX_HEADER))) {
            return true;
        }
        return false;
    }
}
