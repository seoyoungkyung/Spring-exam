package org.zerock.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor	
public class SampleVO {

	private Integer mno;		//int-x(꼭 값이 있어야함) Integer-o null값허용여부
	private String firstName;
	private String lastName;
}
