package org.zerock.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.zerock.domain.ReplyVO;

import lombok.extern.log4j.Log4j;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("file:src/main/webapp/WEB-INF/spring/root-context.xml")
@Log4j
public class ReplyServiceImplTests {

	@Autowired
	private ReplyService replyService;
	
	@Test
	public void testInsert() {
		ReplyVO vo = ReplyVO.builder()
				.bno(45072L)
				.reply("한글로 작성해라")
				.replyer("ac")
				.build();
		
		log.info(replyService.register(vo));
	}
	
	@Test
	public void testDelete() {
		replyService.remove(74L);
	}
}
