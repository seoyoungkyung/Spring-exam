package org.zerock.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.zerock.mapper.Sample2Mapper;
import org.zerock.mapper.SampleMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j;

@Service
@Log4j
@RequiredArgsConstructor
public class SampleTxServiceImpl implements SampleTxService{

	private  final SampleMapper mapper1;
	private  final Sample2Mapper mapper2;
	
	@Transactional
	@Override
	public void addData(String value) {
		
		log.info("mapper1.........");
		mapper1.insertCol1(value);

		log.info("mapper2.........");
		mapper2.insertCol2(value);
		
		log.info("end..................");
		
	}

}
