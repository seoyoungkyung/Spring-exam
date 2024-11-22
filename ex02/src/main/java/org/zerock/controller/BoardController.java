package org.zerock.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.zerock.domain.BoardVO;
import org.zerock.domain.Criteria;
import org.zerock.domain.PageDTO;
import org.zerock.service.BoardService;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j;

@Controller
@Log4j
@RequestMapping("/board/*")
@RequiredArgsConstructor
public class BoardController {

	private final BoardService service;
	
	/*
	 * @GetMapping("/list") public void list(Model model) { log.info("list.......");
	 * 
	 * List<BoardVO> list = service.getList();
	 * 
	 * model.addAttribute("list", list); //view화면 찾아가기 ->
	 * WEB-INF/views/board/list.jsp }
	 */	
	
	@GetMapping("/list")
	public void list(Criteria cri,Model model) {
		log.info("list......." + cri);
		List<BoardVO> list = service.getList(cri);
		
		model.addAttribute("list", list);		//view화면 찾아가기 -> WEB-INF/views/board/list.jsp
//		model.addAttribute("list", service.getList(cri));
		int total = service.getTotal(cri);
		model.addAttribute("pageMaker",new PageDTO(cri,total));
		
	}
	
	@GetMapping("/register")
	public void register() {
		
	}
	
	@PostMapping("/register")
	//@RequestMapping(value = "/register", method = RequestMethod.POST)
	public String register(BoardVO vo, RedirectAttributes rttr) {
		log.info("register...." + vo);
		
		service.register(vo);
		
		rttr.addFlashAttribute("result", vo.getBno());
		
		return "redirect:/board/list";
	}
	
	@GetMapping({"/get","/modify"})
	public void get(@RequestParam("bno") Long bno, @ModelAttribute("cri") Criteria cri,Model model) {
		log.info("get or modify......"+bno+" : "+cri);
		
		BoardVO vo = service.get(bno);
		model.addAttribute("board", vo);
	}
	
	
	
	@PostMapping("/remove")
	public String remove(@RequestParam("bno") Long bno,@ModelAttribute("cri") Criteria cri,RedirectAttributes rttr) {
		log.info("remove......" + bno);
		if(service.remove(bno)) {
			rttr.addFlashAttribute("result","sucess");
		};
		
		rttr.addAttribute("pageNum",cri.getPageNum());
		rttr.addAttribute("amount",cri.getAmount());
		rttr.addAttribute("type",cri.getType());
		rttr.addAttribute("keyword",cri.getKeyword());		
		return "redirect:/board/list";
	}
	
	@PostMapping("/modify")
	public String modify(BoardVO vo,@ModelAttribute("cri") Criteria cri ,RedirectAttributes rttr) {
		log.info("modify..........." + vo);
		if(service.modify(vo)) {
			rttr.addFlashAttribute("result", "success");
		}
		
		rttr.addAttribute("pageNum",cri.getPageNum());
		rttr.addAttribute("amount",cri.getAmount());
		rttr.addAttribute("type",cri.getType());
		rttr.addAttribute("keyword",cri.getKeyword());
		return "redirect:/board/list";
	}
}