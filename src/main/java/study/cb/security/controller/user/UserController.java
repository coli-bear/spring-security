package study.cb.security.controller.user;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import study.cb.security.domain.Account;
import study.cb.security.domain.AccountDto;
import study.cb.security.service.UserService;

@Controller
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    // 회원 가입 페이지 이동
    @GetMapping("/users")
    public String createUser() {
        return "user/login/register";
    }

    @PostMapping("/users")
    public String createUser(AccountDto accountDto) {

        ModelMapper modelMapper = new ModelMapper();

        Account account = modelMapper.map(accountDto, Account.class);
        account.setPassword(passwordEncoder.encode(account.getPassword()));
        if (!StringUtils.hasText(accountDto.getRole())) {
            account.setRole("ROLE_USER");
        }

        userService.createUser(account);

        return "redirect:/";
    }
}
