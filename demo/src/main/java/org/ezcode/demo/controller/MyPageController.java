package org.ezcode.demo.controller;

import java.security.Principal;
import java.util.List;

import org.ezcode.demo.domain.FriendVO;
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

	@GetMapping({ "/myinfo", "/mypw", "/quit" })
	public void myInfoGET(Principal principal, @AuthenticationPrincipal CustomOAuth2User customUser, Model model) {

		// 소셜으로 로그인했는지, 그냥 회원으로 로그인했는지 구별해줘야 함
		String username = "";

		if (customUser != null) { // 소셜 로그인

			log.info("sns login!");

			username = customUser.getMember().getUserid();

		} else { // 그냥 회원으로 로그인

			username = principal.getName();
		}

		log.info("username............." + username);

		log.info("" + service.findById(username));

		if (service.findById(username) != null) {
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

	@PostMapping("/mypw")
	public String mypwPOST(MemberVO vo) {

		log.info("my pw modify post.................");

		service.ModifyPw(vo);

		return "redirect:/mypage/mypw";
	}

	@GetMapping("/mypartner")
	public void mypartnerGET(Principal principal, @AuthenticationPrincipal CustomOAuth2User customUser, Model model) {

		log.info("my partner page get....................");

		// 소셜으로 로그인했는지, 그냥 회원으로 로그인했는지 구별해줘야 함
		String username = "";

		if (customUser != null) { // 소셜 로그인

			log.info("sns login!");

			username = customUser.getMember().getUserid();

		} else { // 그냥 회원으로 로그인

			username = principal.getName();
		}

		log.info("username............." + username);

		model.addAttribute("memberInfo", service.findById(username));
		model.addAttribute("requestList", service.findRequestFriends(username));
		model.addAttribute("friendList", service.findFriends(username));
		
	}

	@PostMapping("/delmate")
	public String delMatePOST(int mateno) {

		log.info("" + mateno);

		boolean result = service.deleteFriend(mateno);

		log.info("" + result);

		return "redirect:/mypage/mypartner";
	}

	@PostMapping("/updatemate")
	public String updateMatePOST(int mateno) {

		log.info("" + mateno);

		boolean result = service.ModifyFriend(mateno);

		log.info("" + result);

		return "redirect:/mypage/mypartner";
	}

	@GetMapping("/pdetail")
	public void partnerDetailGET(String userid, Model model) {

		log.info("userid.........." + userid);

		// userid 로 정보 가져옴
		MemberVO mem = service.findById(userid);
		log.info("" + mem);
		model.addAttribute("detail", mem);

		// userid 로 친구 목록 가져옴
		List<FriendVO> fvo = service.findFriends(userid);
		log.info("" + fvo);
		model.addAttribute("flist", fvo);

		log.info("partner detail....................");
	}

// -------------------------------------------------------------------------------------

	@PostMapping("/quit")
	public ResponseEntity<String> checkPwPOST(String userid) {

		log.info(userid);

		boolean result = service.quitMember(userid);

		log.info("" + result);

		return result ? new ResponseEntity<>("success", HttpStatus.OK)
				: new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@PostMapping("/checkpw")
	public ResponseEntity<String> checkPwPOST(String userid, String userpw) {

		log.info(userid);
		log.info(userpw);

		// id와 비밀번호를 받아서 db에 있는 정보와 일치하는지 확인한다.
		boolean result = service.checkByIdAndPw(userid, userpw);

		log.info("" + result);

		return result ? new ResponseEntity<>("success", HttpStatus.OK)
				: new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
	}

}