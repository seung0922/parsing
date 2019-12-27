package org.ezcode.demo.domain;

import java.util.Date;
import java.util.List;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

import lombok.Data;

/**
 * MemberVO
 */
@Data
public class MemberVO {

	@Size(min = 5)
    private String userid;
	private String userpw;
	private String username;
	private boolean enabled;

	private String email;
	private String tel;
	private String mlang;
	
	private Date regDate;
	private Date updateDate;
	
	private List<AuthVO> authList;
}