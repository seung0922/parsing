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

    public MemberVO selectOneMemberById(String userid);

    // public MemberVO selectOneMemberByIdAndPw(String userid, String userpw);

    public int updateMember(MemberVO vo);

    public int updatePw(MemberVO vo);

    public int deleteMember(String userid);

    public int deleteMemberAuth(String userid);

}