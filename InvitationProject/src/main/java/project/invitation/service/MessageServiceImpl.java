package project.invitation.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j;
import project.invitation.domain.MessageVO;
import project.invitation.mapper.MessageMapper;

@Log4j
@Service
@AllArgsConstructor
public class MessageServiceImpl implements MessageService{
	
	

	@Autowired
	private MessageMapper mapper;
	
	@Override
	public void register(MessageVO messageVO) {
		log.info("register........"+ messageVO);
		
		mapper.insert(messageVO);
		
	}

	@Override
	public MessageVO get(Long mno) {
		log.info("Get..........."+mno);
		return mapper.read(mno);
	}

	 @Override
    public boolean modify(MessageVO messageVO) {
        int updateCount = mapper.update(messageVO);  // DB에서 메시지 수정
        return updateCount == 1;  // 1개 행이 수정되었으면 성공
    }

	@Override
	public boolean remove(Long mno) {
		log.info("remove........."+mno);
		
		
		return mapper.delete(mno) == 1;
	}

	@Override
	public List<MessageVO> getList() {
		log.info("get List...........");
		return mapper.getList();
	}

	@Override
	public boolean checkpw(MessageVO messageVO) {
		
		return mapper.checkguestpw(messageVO);
	}

	
    @Override
    public String getGuestPwByMno(Long mno) {
        return mapper.getGuestPwByMno(mno);
    }

 

}
