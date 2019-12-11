package org.parse.mapper;

import java.util.List;

import org.parse.domain.ParseVO;

public interface ParseMapper {

	public List<ParseVO> selectAll(String keyword);
	
	public int insertCode(ParseVO vo);
	
}
