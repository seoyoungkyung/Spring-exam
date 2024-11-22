package org.zerock.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Select;
import org.zerock.domain.BoardVO;
import org.zerock.domain.Criteria;

public interface BoardMapper {

	public List<BoardVO> getList();
	
	public List<BoardVO> getListWithPage(Criteria cri);
	
	public void insert(BoardVO boardVO);
	
	public void insertSelectKey(BoardVO vo);
	
	public BoardVO read(Long bno);
	
	public int delete(Long bno);
	
	public int update(BoardVO boardVO);
	
	public int getTotalCount(Criteria cri);	//전체 데이터 개수
	
	//test용
	public List<BoardVO> searchTest(Map<String, Map<String,String>> map);
}
