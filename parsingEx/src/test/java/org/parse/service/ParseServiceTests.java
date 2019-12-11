package org.parse.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.parse.domain.ParseVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import lombok.extern.log4j.Log4j;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("file:src/main/webapp/WEB-INF/spring/root-context.xml")
@Log4j
public class ParseServiceTests {

	@Autowired
	private ParseService service;
	
	@Test
	public void insertTest() {
		log.info("insert Test..................");
		
		ParseVO vo = new ParseVO();
		
		log.info(service.insertCode(vo));
	}
	
	@Test
	public void selectAllTest() {
		log.info(service.selectAll("별찍기"));
	}
	
}
