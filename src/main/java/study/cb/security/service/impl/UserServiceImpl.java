package study.cb.security.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import study.cb.security.domain.Account;
import study.cb.security.repository.UserRepository;
import study.cb.security.service.UserService;

@Service("userService")
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Transactional
    @Override
    public void createUser(Account account) {
        userRepository.save(account);
    }
}
