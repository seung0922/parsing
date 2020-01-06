package org.ezcode.demo.service;

import java.util.List;

import org.ezcode.demo.domain.FriendVO;
import org.ezcode.demo.mapper.FriendMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

/**
 * MemberServiceImpl
 */
@Service
@Slf4j
public class FriendServiceImpl implements FriendService {

    @Autowired
    private FriendMapper friendMapper;

    @Override
    public List<FriendVO> findFriends(String userid) {

        log.info("find friends..........................");

        return friendMapper.selectFriends(userid);
    }

    @Override
    public List<FriendVO> findRequestFriends(String userid) {

        log.info("find request friends...........................");

        return friendMapper.selectRequestFriends(userid);
    }

    @Override
    public boolean deleteFriend(int mateno) {
        
        log.info("delete friend......................");
        log.info("" + mateno);

        return friendMapper.deleteFriend(mateno) == 1 ? true : false;
    }

    @Override
    public boolean ModifyFriend(int mateno) {

        log.info("modify friend......................");
        log.info("" + mateno);

        return friendMapper.updateFriend(mateno) == 1 ? true : false;
    }

    @Override
    public int checkFriend(String userid, String fid) {

        // 0 : 친구아님, 1 : 친구요청중, 2 : 친구임
        int result = 0;

        FriendVO vo = friendMapper.checkFriend(userid, fid);

        if(vo != null) { // 친구 테이블에 있을 때
            if(vo.getFriendcheck() == 0) { // 친구요청중
                result = 1;
            } else if(vo.getFriendcheck() == 1) { // 친구상태
                result = 2;
            }
        }

        return result;
    }

    @Override
    public boolean makeFriend(String sender, String receiver) {
        
        return friendMapper.insertFriend(sender, receiver) == 1 ? true : false;
    }

}