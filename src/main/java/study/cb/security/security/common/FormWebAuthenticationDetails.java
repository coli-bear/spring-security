package study.cb.security.security.common;

import org.springframework.security.web.authentication.WebAuthenticationDetails;

import javax.servlet.http.HttpServletRequest;


// 사용자가 전달하는 추가적인 파라미터를 받는 클래스
public class FormWebAuthenticationDetails extends WebAuthenticationDetails {

    private String secretKey;

    public FormWebAuthenticationDetails(HttpServletRequest request) {
        super(request);
        secretKey = request.getParameter("secret_key");
    }

    public String getSecretKey() {
        return secretKey;
    }
}
