package org.ezcode.demo.mapper;

import org.ezcode.demo.domain.MemberVO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import lombok.extern.slf4j.Slf4j;

/**
 * MemberMapperTests
 */
@Slf4j
@SpringBootTest
public class MemberMapperTests {

    @Autowired
    private MemberMapper mapper;

    @Autowired
    private BCryptPasswordEncoder encoder;

    @Test
    public void updatepw() {

        MemberVO vo = mapper.selectOneMember("7");

        log.info("" + encoder.matches("7", vo.getUserpw()));

        vo.setUserpw("77");

        log.info("" + encoder.matches("77", vo.getUserpw()));

    }

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

        // log.info("" + mapper.selectOneMember("dltdlt", "1234"));
    }

}