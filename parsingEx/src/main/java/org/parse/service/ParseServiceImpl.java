package org.parse.service;

import java.util.List;

import org.parse.domain.ParseVO;
import org.parse.mapper.ParseMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.Setter;
import lombok.extern.log4j.Log4j;

@Service
@Log4j
public class ParseServiceImpl implements ParseService {

	@Setter(onMethod_ = {@Autowired})
	private ParseMapper mapper;
	
	@Override
	public List<ParseVO> selectAll(String keyword) {
		// TODO Auto-generated method stub
		return mapper.selectAll(keyword);
	}


	@Override
	public boolean insertCode(ParseVO vo) {
		log.info("insert Code........................");
		return mapper.insertCode(vo) == 1 ? true : false;
	}
	
}
