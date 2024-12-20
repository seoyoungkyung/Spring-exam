package org.zerock.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.zerock.domain.Criteria;
import org.zerock.domain.ReplyPageDTO;
import org.zerock.domain.ReplyVO;
import org.zerock.service.ReplyService;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j;

@RestController
@RequestMapping("/replies/")
@Log4j
@RequiredArgsConstructor
public class ReplyController {

	private final ReplyService service;
	
	
	// http://localhost:8080/replies/new ->json데이터를 replyVO 객체로 convter 후 DB에 저장
	// 저장 성공이면 ResponseEntity에 "success"문자열과 상태코드(200)을 응답
	// 저장 실패하면 ResponseEntity에 상태코드(500) 응답
	@PostMapping(value="/new", 
			consumes = MediaType.APPLICATION_JSON_VALUE,	//요청(json) 
			produces = MediaType.TEXT_PLAIN_VALUE)			//응답(String)
	public ResponseEntity<String> create(@RequestBody ReplyVO vo){
		log.info("create......"+vo);
		
		int insertCount = service.register(vo);
		
		return insertCount == 1 
				? new ResponseEntity<String>("success",HttpStatus.OK)
				: new ResponseEntity<String>(HttpStatus.INTERNAL_SERVER_ERROR);
	};
	
	
	//http://localhost:8080/replies/pages/16/1 => reply테이블에서 bno가 16번 전체 레코드중 1page 해당하는 10개 조회
	@GetMapping(value="/pages/{bno}/{page}",produces = {MediaType.APPLICATION_JSON_VALUE})
	public ResponseEntity<ReplyPageDTO> getList(
									@PathVariable("bno") Long bno,
									@PathVariable("page") int page){
		
		log.info("getList....bno: "+ bno + ",page : "+page);
		
		Criteria cri = new Criteria(page, 10);
//		List<ReplyVO> list = service.getList(cri,bno);
		ReplyPageDTO list = service.getListPage(cri,bno);
		
		log.info("---------");
		log.info(list);
		
		return new ResponseEntity<ReplyPageDTO>(list,HttpStatus.OK);
	}
	
	
	//http://localhost:8080/replies/42  --> rno:42번 (삭제)
	// 저장 성공이면 ResponseEntity에 "success"문자열과 상태코드(200)을 응답
	// 저장 실패하면 ResponseEntity에 상태코드(500) 응답
	@DeleteMapping(value = "/{rno}", produces = {MediaType.TEXT_PLAIN_VALUE})
	public ResponseEntity<String> remove(@PathVariable("rno") Long rno){
		log.info("remove........."+rno);
		
		return service.remove(rno) ==1
				? new ResponseEntity<String>("success",HttpStatus.OK)
				: new ResponseEntity<String>(HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
	
	//http://localhost:8080/replies/19 -> rno:19번(json으로 응답)
	//rno에 없는 번호를 입력하면 null로 응답
	@GetMapping(value = "/{rno}", produces = {MediaType.APPLICATION_JSON_VALUE})
	public ResponseEntity<ReplyVO> get(@PathVariable("rno")Long rno){
		log.info("get........." + rno);
		
		ReplyVO replyVO = service.get(rno);
		
		return new ResponseEntity<ReplyVO>(replyVO,HttpStatus.OK);
	}
	
	
	
	//http://localhost:8080/replies/33 + json 데이터 => rno(33)번 수정한다.
	@RequestMapping(method = {RequestMethod.PUT,RequestMethod.PATCH},
					value = "/{rno}",
					consumes = {MediaType.APPLICATION_JSON_VALUE},
					produces = {MediaType.TEXT_PLAIN_VALUE})
	public ResponseEntity<String> modify(@RequestBody ReplyVO vo,
										@PathVariable("rno") Long rno){
		vo.setRno(rno);
		log.info("modify...... rno: "+rno);
		log.info("reply: "+vo);
		 
		return service.modify(vo) ==1
				? new ResponseEntity<String>("success",HttpStatus.OK)
				: new ResponseEntity<String>(HttpStatus.INTERNAL_SERVER_ERROR);
	}
}
