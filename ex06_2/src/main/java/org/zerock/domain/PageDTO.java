package org.zerock.domain;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class PageDTO {

	private int startPage;
	private int endPage;
	private boolean prev,next;
	
	private int total;
	private Criteria cri;
	
	public PageDTO(Criteria cri,int total) {
		
		this.cri = cri;
		this.total = total;
		
		System.out.println(cri.getPageNum());
		System.out.println(cri.getAmount());
		
		this.endPage = (int)(Math.ceil(cri.getPageNum()/(double)cri.getAmount()) * cri.getAmount());
		startPage = this.endPage -9;
		
		int realEnd = (int)(Math.ceil((total*1.0)/cri.getAmount()));
		
		if(realEnd < this.endPage) {
			this.endPage = realEnd;
		}
		
		this.prev = startPage>1;
		this.next = this.endPage < realEnd;
	}
}
