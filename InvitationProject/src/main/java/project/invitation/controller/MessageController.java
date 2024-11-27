package project.invitation.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j;
import project.invitation.domain.MessageVO;
import project.invitation.service.MessageService;

@Controller
@Log4j
@RequestMapping("/invitation/*")
@RequiredArgsConstructor
public class MessageController {
	
	private final MessageService service;
	
	@GetMapping("/list")
	public void list(Model model) {
		log.info("list");
		model.addAttribute("list", service.getList());
	}
	
	@GetMapping("/register")
	public void register() {}
	
	@PostMapping("/register")
	public String register(MessageVO messageVO , RedirectAttributes rttr) {
		
		log.info("register.......: "+messageVO);
		service.register(messageVO);
		
		rttr.addFlashAttribute("result", messageVO.getMno());
		return "redirect:/invitation/list";
	}
	
	@GetMapping("/get")
	public void get(@RequestParam("mno") Long mno, Model model) {
		log.info("/get");
		model.addAttribute("messageVO", service.get(mno));
	}

	
	@PostMapping("/remove")
    @ResponseBody
    public String remove(@RequestParam Long mno, @RequestParam String enteredPassword) {
        // DB에서 게시글 정보 가져오기
        MessageVO messageVO = service.get(mno);

        // DB에 저장된 비밀번호와 입력된 비밀번호 비교
        if (messageVO != null && messageVO.getGuestpw().equals(enteredPassword)) {
            // 비밀번호 일치하면 삭제 처리
            boolean isRemoved = service.remove(mno);
            log.info("*************");
            log.info(messageVO);
            log.info(isRemoved);
            log.info("*************");
            if (isRemoved) {
            	
                return "success";  // 삭제 성공
            }
        }

        return "fail";  // 비밀번호 불일치 또는 삭제 실패
    }
	
	
	 // 비밀번호 확인 API
    @PostMapping("/checkPassword")
    @ResponseBody
    public String checkPassword(@RequestParam("mno") Long mno, @RequestParam("enteredPassword") String enteredPassword) {
        // 해당 mno의 비밀번호 조회
        String correctPassword = service.getGuestPwByMno(mno);
        
        // 비밀번호 일치 여부 확인
        if (correctPassword != null && correctPassword.equals(enteredPassword)) {
            return "valid";  // 비밀번호가 맞으면 "valid"
        }
        return "invalid";  // 틀리면 "invalid"
    }
	
 // 메시지 수정 API
    @PostMapping("/update")
    public ResponseEntity<String> update(@RequestParam("mno") 
    Long mno, @RequestParam("content") String content) {
        log.info("수정 요청: 게시글 번호 = " + mno + ", 수정된 내용 = " + content);

        // 1. 게시글 번호(mno)에 해당하는 메시지를 찾기
        MessageVO messageVO = service.get(mno);
        if (messageVO == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("게시글을 찾을 수 없습니다.");
        }

        // 2. 수정된 내용을 저장
        messageVO.setContent(content);

        // 3. 수정 처리
        boolean isUpdated = service.modify(messageVO);
        if (isUpdated) {
            return ResponseEntity.ok("success");
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("수정 실패");
        }
    }

	@PostMapping("/invitation/validatePassword")
    public String validatePassword(@RequestParam("mno") Long mno, @RequestParam("enteredPassword") String enteredPassword, Model model) {
        // DB에서 guestpw 가져오기
        String guestpw =service.getGuestPwByMno(mno);

        // 입력된 비밀번호와 DB 비밀번호 비교
        if (guestpw != null && guestpw.equals(enteredPassword)) {
            // 비밀번호가 맞으면 수정 페이지로 이동
            return "redirect:/invitation/edit?mno=" + mno;
        } else {
            // 비밀번호가 틀리면 에러 메시지
            model.addAttribute("errorMessage", "비밀번호가 일치하지 않습니다.");
            return "errorPage";  // 에러 페이지로 이동
        }
    }
	
	@GetMapping("/getGuestPwByMno")
    @ResponseBody
    public String getGuestPwByMno(@RequestParam("mno") Long mno) {
        // mno에 해당하는 guestpw를 가져오는 로직
        String guestpw = service.getGuestPwByMno(mno);
        return guestpw;
    }
}
