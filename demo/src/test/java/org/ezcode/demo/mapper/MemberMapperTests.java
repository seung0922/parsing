package org.ezcode.demo.mapper;

import org.ezcode.demo.domain.MemberVO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import lombok.extern.slf4j.Slf4j;

/**
 * MemberMapperTests
 */
@Slf4j
@SpringBootTest
public class MemberMapperTests {

    @Autowired
    private MemberMapper mapper;

    @Test
    public void insertMemberTest() {
        
        MemberVO vo = new MemberVO();

        vo.setUserid("manLong");
        vo.setUserpw("12345");
        vo.setUsername("김승원");
        vo.setEmail("dltdlt1995@naver.com");
        vo.setTel("010-9022-4101");
        vo.setMlang("java");

        log.info("" + mapper.insertMember(vo));

    }

    @Test
    public void selectOneMemTest() {

        log.info("" + mapper.selectOneMember("dltdlt", "1234"));
    }

}