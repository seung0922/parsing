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

    public MemberVO selectOneMember(String userid);

    public int updateMember(MemberVO vo);

    public int deleteMember(String userid);

    public int deleteMemberAuth(String userid);
}