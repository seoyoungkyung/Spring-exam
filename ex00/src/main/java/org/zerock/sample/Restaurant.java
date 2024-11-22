package org.zerock.sample;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import lombok.Data;

//@Controller
//@Repository
//@Service
@Component
@Data
public class Restaurant {
	
	@Autowired
	private Chef chef;
	
}
