package org.parse.service;

import java.util.List;

import org.parse.domain.ParseVO;

public interface ParseService {

	public List<ParseVO> selectAll(String keyword);
	
	public boolean insertCode(ParseVO vo);
}
