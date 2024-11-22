package org.zerock.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class Criteria {

	private int pageNum;		//페이지번호
	private int amount;			//화면당 레코드 갯수
	
	private String type;		//제목 or 내용 or 저자
	private String keyword;		//검색어
	
	public Criteria() {
		this(1,10);
	}
	
	public Criteria(int pageNum,int amount) {
		this.pageNum = pageNum;
		this.amount = amount;
	}
	
	
	/* 검색 옵션
	 * T : 제목
	 * C : 내용
	 * W : 저자
	 * 
	 * ex) TCW(제목+내용+저자), TW(제목+저자)
	 * 
	 * type >> TCW --> T|C|W (하나문자열을 개별 문자열로 분리)
	 */
	public 	String[] getTypeArr() {
		return type == null? new String[] {}: type.split("");
 	}
}
