package org.ezcode.demo.service;

import java.util.List;

import org.ezcode.demo.domain.FriendVO;
import org.ezcode.demo.domain.MemberVO;

/**
 * MemberService
 */
public interface MemberService {

    public boolean join(MemberVO vo);

    public MemberVO read(String userid);

    public MemberVO findById(String userid);

    public boolean checkByIdAndPw(String userid, String userpw);

    public boolean ModifyMember(MemberVO vo);

    public boolean ModifyPw(MemberVO vo);

    public boolean quitMember(String userid);

    public List<FriendVO> findFriends(String userid);

    public List<FriendVO> findRequestFriends(String userid);

    public boolean deleteFriend(int mateno);

    public boolean ModifyFriend(int mateno);

}