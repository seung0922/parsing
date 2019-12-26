package org.ezcode.demo.controller;

import org.ezcode.demo.domain.MemberVO;
import org.ezcode.demo.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

/**
 * MemberController
 */
@Slf4j
@RequestMapping("/member")
@Controller
public class MemberController {

    @Setter(onMethod_ = @Autowired)
    private MemberService memberService;

    @GetMapping("/login")
	public void loginPage() {
		log.info("login...........................");
	}

	@GetMapping("/join")
	public void joinGET() {
		log.info("join..........................");
    }
    
    @PostMapping("/join")
    public String joinPOST(MemberVO vo) {
        
        log.info("join post......................");
        
        log.info("" + memberService.join(vo));

        return "redirect:/oauth_login";
    }

//----------------------------------------------------------------------------
    @GetMapping("/all")
    public void doAll() {
        log.info("all...........");
    }

    @GetMapping("/member")
    public void doMember() {
        log.info("member...........");
    }

    @GetMapping("/admin")
    public void doAdmin() {
        log.info("admin...........");
    }

    @PreAuthorize("hasAnyRole('ROLE_MEMBER')")
    @GetMapping("/authTest")
    public void doAuthTest() {
        log.info("auth test...........");
    }

    @Secured({"ROLE_ADMIN"})
    @GetMapping("/authTest2")
    public void doAuthTest2() {
        log.info("auth test...........");
    }


    
}