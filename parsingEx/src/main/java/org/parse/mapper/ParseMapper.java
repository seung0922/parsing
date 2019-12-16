package org.parse.mapper;

import java.util.List;

import org.parse.domain.ParseVO;
import org.parse.dto.SearchDTO;

public interface ParseMapper {

//	public List<ParseVO> selectAll(@Param("keyword")String keyword, @Param("lang") String lang);
	
	public List<ParseVO> selectAll(SearchDTO dto);
	
	public int insertCode(ParseVO vo);
	
}
