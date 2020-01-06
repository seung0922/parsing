package org.ezcode.demo.mapper;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import lombok.extern.slf4j.Slf4j;

/**
 * FriendMapper
 */
@Slf4j
@SpringBootTest
public class FriendMapperTests {

    @Autowired
    private FriendMapper mapper;

    @Test
    public void insertFriendTest() {

        int r = mapper.insertFriend("909090", "789789");

        log.info("" + r);
    }

    @Test
    public void checkFriendTest() {

        log.info("" + mapper.checkFriend("789789", "123123"));
    }
}