package org.ezcode.demo.domain;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import lombok.Data;

/**
 * ParseVO
 */
@Data
public class ParseVO {

    private Integer pno;
	private String keyword;
	private String code;
	private String lang;
	private String path;
	private String fname;
	
	private Integer comment;
	private Integer start;

}