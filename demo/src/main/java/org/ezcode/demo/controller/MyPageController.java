package org.ezcode.demo.controller;

import java.security.Principal;

import org.ezcode.demo.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import lombok.extern.slf4j.Slf4j;

@Controller
@RequestMapping("/myPage")
@Slf4j
public class MyPageController {

	@Autowired
	private MemberService service;

	@GetMapping("/myPage")
	public void myPage() {

		log.info("MyPage....");
	}

	@GetMapping("/myInfo")
	public void myInfo(Principal principal, Model model) { 

		String username = principal.getName();
		
		log.info("username............." + username);

		log.info("" + service.findById(username));

		if(service.findById(username) != null) {
			model.addAttribute("memberInfo", service.findById(username));
		}

		log.info("My info............................");
	}
}
