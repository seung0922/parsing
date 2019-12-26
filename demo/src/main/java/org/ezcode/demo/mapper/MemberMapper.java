package org.ezcode.demo.mapper;

import org.ezcode.demo.domain.AuthVO;
import org.ezcode.demo.domain.MemberVO;

/**
 * MemberMapper
 */
public interface MemberMapper {

    public int insertMember(MemberVO vo);

    public int insertAuth(AuthVO vo);

    public MemberVO read(String userid);

}