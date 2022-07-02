package study.cb.security.security.provider;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import study.cb.security.security.common.FormWebAuthenticationDetails;
import study.cb.security.security.service.AccountContext;

public class CustomAuthenticationProvider implements AuthenticationProvider {

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

            return new UsernamePasswordAuthenticationToken(accountContext.getAccount(), null, accountContext.getAuthorities());
        }

        throw new BadCredentialsException("BadCredentialException");
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
    }
}
