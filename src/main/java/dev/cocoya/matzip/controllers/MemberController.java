package dev.cocoya.matzip.controllers;

import dev.cocoya.matzip.entities.member.UserEntity;
import dev.cocoya.matzip.services.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@Controller(value = "dev.cocoya.matzip.controllers.MemberController")
@RequestMapping(value = "member")
public class MemberController {
    private final MemberService memberService;

    @Autowired
    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    @GetMapping(value = "kakao", produces = MediaType.TEXT_PLAIN_VALUE)
    public ModelAndView getKakao(@RequestParam(value = "code") String code,
                                 @RequestParam(value = "error", required = false) String error,
                                 @RequestParam(value = "error_description", required = false) String errorDescription,
                                 HttpServletRequest request,
                                 HttpSession session) throws
            IOException {
        String redirectUri = String.format("%s://%s/member/kakao",
                request.getScheme(),
                request.getServerName());
        String accessToken = this.memberService.getKakaoAccessToken(code,redirectUri);
        UserEntity user = this.memberService.getKakaoUserInfo(accessToken);
        session.setAttribute("user", user);
        return new ModelAndView("member/kakao");
    }

    @GetMapping(value = "logout")
    public ModelAndView getLogout(HttpSession session) {
        session.setAttribute("user", null);
        session.invalidate();
        return new ModelAndView("redirect:/");
    }
}