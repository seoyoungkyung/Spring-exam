package project.invitation.service;

import java.util.List;

import project.invitation.domain.MessageVO;

public interface MessageService {

	// 메시지 등록
	public void register(MessageVO messageVO);
	
	// 메시지 조회
	public MessageVO get(Long mno);
	
	 // 메시지 수정
	public boolean modify(MessageVO messageVO);
	
	// 메시지 삭제
	public boolean remove(Long mno);
	
	// 메시지 목록 조회
	public List<MessageVO> getList();
	
	// 비밀번호 확인
	public boolean checkpw(MessageVO messageVO);
	
	// 특정 mno에 해당하는 비밀번호 가져오기
	public String getGuestPwByMno(Long mno);
	


}
