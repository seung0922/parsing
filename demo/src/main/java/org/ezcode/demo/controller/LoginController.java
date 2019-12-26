package org.ezcode.demo.controller;

import java.util.Locale;

import org.ezcode.demo.domain.MemberVO;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/member")
public class LoginController {
	
	@GetMapping("/login")
	public void loginPage() {
		
		
	}

	@GetMapping("/join")
	public void joinGET() {
		
	}

}
