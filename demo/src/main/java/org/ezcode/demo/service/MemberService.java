package org.ezcode.demo.service;

import org.ezcode.demo.domain.MemberVO;

/**
 * MemberService
 */
public interface MemberService {

    public boolean join(MemberVO vo);

    public MemberVO read(String userid);

    public MemberVO findById(String userid);
}