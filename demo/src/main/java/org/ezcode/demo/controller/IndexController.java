package org.ezcode.demo.controller;

import org.ezcode.demo.domain.SearchDTO;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import lombok.extern.slf4j.Slf4j;

/**
 * IndexController
 */
@Controller
@Slf4j
public class IndexController {

    @PostMapping("/")
    public String search(SearchDTO dto, RedirectAttributes rttr) {
        log.info("post.............................");

		log.info("keyword................. " + dto.getKeyword());
		log.info("languge................. " + dto.getLang());
		log.info("comment?....................... " + dto.getComment());

		rttr.addAttribute("keyword", dto.getKeyword());
        rttr.addAttribute("lang", dto.getLang());
        rttr.addAttribute("siteLink", dto.getSiteLink());
		rttr.addAttribute("comment", dto.getComment());

		return "redirect:/search/list";
    }
    
}