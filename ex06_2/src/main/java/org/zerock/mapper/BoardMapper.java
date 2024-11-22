package org.zerock.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
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
	
	//댓글 개수 변경  amount:한번에 수정하는 갯수
	public void updateReplyCnt(@Param("bno") Long bno, @Param("amount") int amount);
	
	//test용
	public List<BoardVO> searchTest(Map<String, Map<String,String>> map);

	
}
