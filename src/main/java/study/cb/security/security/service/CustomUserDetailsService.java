package study.cb.security.security.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import study.cb.security.domain.Account;
import study.cb.security.repository.UserRepository;

import java.util.ArrayList;
import java.util.List;

@Service("userDetailsService")
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
    private final UserRepository userRepository;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Account account = userRepository.findByUsername(username);

        if (account == null) {
            throw new UsernameNotFoundException("UsernameNotFoundException " + username);
        }

        List<GrantedAuthority> roles = new ArrayList<>();
        roles.add(new SimpleGrantedAuthority(account.getRole()));

        AccountContext accountContext = new AccountContext(account, roles);

        return accountContext;
    }
}
