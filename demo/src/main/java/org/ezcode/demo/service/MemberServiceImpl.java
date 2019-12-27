package org.ezcode.demo.service;

import org.ezcode.demo.domain.AuthVO;
import org.ezcode.demo.domain.MemberVO;
import org.ezcode.demo.mapper.MemberMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.extern.slf4j.Slf4j;

/**
 * MemberServiceImpl
 */
@Service
@Slf4j
public class MemberServiceImpl implements MemberService {

    @Autowired
    private MemberMapper memberMapper;

    @Autowired
    private BCryptPasswordEncoder encoder;

    // @Setter(onMethod_ = {@Autowired})
    // private PasswordEncoder encoder;

    @Transactional
    @Override
    public boolean join(MemberVO vo) {

        log.info("join service ------------------");
        
        AuthVO authVO = new AuthVO();
        authVO.setUserid(vo.getUserid());
        authVO.setAuth("ROLE_MEMBER");

        vo.setUserpw(encoder.encode(vo.getUserpw()));

        int im = memberMapper.insertMember(vo);
        int ia = memberMapper.insertAuth(authVO);

        return (im + ia) == 2 ? true : false;
    }

    @Override
    public MemberVO read(String userid) {

        return memberMapper.read(userid);
    }

    @Override
    public MemberVO findByIdAndPw(String userid, String userpw) {

        return memberMapper.selectOneMember(userid, userpw);
    }


    

}