package org.zerock.service;

import java.util.List;


import org.springframework.stereotype.Service;
import org.zerock.domain.BoardVO;
import org.zerock.domain.Criteria;
import org.zerock.mapper.BoardMapper;


import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j;

@Log4j
@Service
@RequiredArgsConstructor  //생성자 주입하기위한 어노테이션
public class BoardServiceImpl implements BoardService{

	
	private final BoardMapper mapper;
	
	@Override
	public void register(BoardVO vo) {
		log.info("register.........." + vo);
		mapper.insertSelectKey(vo);
	}

	@Override
	public BoardVO get(Long bno) {
		log.info("get...");
		return mapper.read(bno);
	}

	@Override
	public boolean modify(BoardVO vo) {
		log.info("modify...");
		return mapper.update(vo) == 1;
	}

	@Override
	public boolean remove(Long bno) {
		log.info("remove.....");
		
		return mapper.delete(bno) ==1;
	}

//	@Override
//	public List<BoardVO> getList() {
		
//		log.info("List........");
//		List<BoardVO> list = mapper.getList();
//		return list;
		
//		return mapper.getList();
//	}

	@Override
	public List<BoardVO> getList(Criteria cri) {
		log.info("get List with criteria: " + cri);
		
		return mapper.getListWithPage(cri);
	}

	@Override
	public int getTotal(Criteria cri) {
		log.info("get total count");
		return mapper.getTotalCount(cri);
	}

}
