package study.cb.security.security.provider;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import study.cb.security.security.common.FormWebAuthenticationDetails;
import study.cb.security.security.service.AccountContext;
import study.cb.security.security.token.AjaxAuthenticationToken;

public class AjaxAuthenticationProvider implements AuthenticationProvider {
    // ajax를 이용한다고 해도 form 인증방식과 동일하게 동작
    @Autowired
    private UserDetailsService userDetailsService;
    @Autowired
    private PasswordEncoder passwordEncoder;

    // 검증을 위한 구현
    // 입력한 ID, Password 정보가 있음
    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {

        String username = authentication.getName();
        String password = (String) authentication.getCredentials();

        AccountContext accountContext = (AccountContext) userDetailsService.loadUserByUsername(username);

        FormWebAuthenticationDetails details = (FormWebAuthenticationDetails) authentication.getDetails();
        String secretKey = details.getSecretKey();

        if (secretKey == null || !"secret".equals(secretKey)) {
            throw new InsufficientAuthenticationException("InsufficientAuthenticationException");
        }

        if (passwordEncoder.matches(password, accountContext.getAccount().getPassword())) {

            return new AjaxAuthenticationToken(accountContext.getAccount(), null, accountContext.getAuthorities());
        }

        throw new BadCredentialsException("BadCredentialException");
    }

    @Override
    public boolean supports(Class<?> authentication) {
        // 인증 Token만 ajax를 사용
        return AjaxAuthenticationToken.class.isAssignableFrom(authentication);
    }
}
