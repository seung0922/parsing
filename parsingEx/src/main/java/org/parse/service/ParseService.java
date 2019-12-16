package org.parse.service;

import java.util.List;

import org.parse.domain.ParseVO;
import org.parse.dto.SearchDTO;

public interface ParseService {

	public List<ParseVO> selectAll(SearchDTO dto);
	
	public boolean insertCode(ParseVO vo);
}
