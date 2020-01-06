package org.ezcode.demo.service;

import java.util.List;

import org.ezcode.demo.domain.FriendVO;

/**
 * MemberService
 */
public interface FriendService {

    public boolean makeFriend(String sender, String receiver);

    public List<FriendVO> findFriends(String userid);

    public List<FriendVO> findRequestFriends(String userid);

    public boolean deleteFriend(int mateno);

    public boolean ModifyFriend(int mateno);

    public int checkFriend(String userid, String fid);

}