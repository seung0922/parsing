package org.ezcode.demo.controller;

import java.security.Principal;

import javax.servlet.http.HttpSession;

import org.ezcode.demo.domain.MemberVO;
import org.ezcode.demo.security.CustomOAuth2User;
import org.ezcode.demo.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.web.bind.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import lombok.extern.slf4j.Slf4j;

@Controller
@RequestMapping("/mypage")
@Slf4j
public class MyPageController {

	@Autowired
	private MemberService service;

	@GetMapping("/mypage")
	public void myPage() {
		
		log.info("MyPage....");

	}

	// @GetMapping("/mypage-sample")
    // public String sample() {
    //     return "/mypage/mypage-sample";
    // }

	@GetMapping({"/myinfo", "/mypw", "/quit"})
	public void myInfoGET(Principal principal, @AuthenticationPrincipal CustomOAuth2User customUser, Model model) { 

		// 소셜으로 로그인했는지, 그냥 회원으로 로그인했는지 구별해줘야 함
		String username = "";

		if(customUser != null) { // 소셜 로그인

			log.info("sns login!");	

			username = customUser.getMember().getUserid();

		} else { // 그냥 회원으로 로그인

			username = principal.getName();
		}
		
		log.info("username............." + username);

		log.info("" + service.findById(username));

		if(service.findById(username) != null) {
			model.addAttribute("memberInfo", service.findById(username));
		}

	}

	@PostMapping("/myinfo")
	public String myInfoPOST(MemberVO vo) {

		log.info("myinfo post...........................");
		log.info("" + vo);

		log.info("" + service.ModifyMember(vo));

		return "redirect:/mypage/myinfo";
	}

	// @GetMapping("/mypw")
    // public void mypwGET() {
        
    //     log.info("my pw modify get.................");
	// }
	
	@PostMapping("/mypw")
    public String mypwPOST(MemberVO vo) {
        
		log.info("my pw modify post.................");
		
		service.ModifyPw(vo);

		return "redirect:/mypage/mypw";
    }

	@PostMapping("/quit")
	public String quitMember(String userid, HttpSession session) {

		// db에서 해당 멤버 삭제
		service.quitMember(userid);

		// 세션 삭제
		session.invalidate();

		return "redirect:/";
	}

	@PostMapping("/checkpw")
	public ResponseEntity<String> checkPwPOST(@RequestBody MemberVO vo) {

		log.info("" + vo);

		// id와 비밀번호를 받아서 db에 있는 정보와 일치하는지 확인한다.
		boolean result = service.checkByIdAndPw(vo.getUserid(), vo.getUserpw());

		return result == true ? new ResponseEntity<>("success", HttpStatus.OK) :
			new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
	}

}
