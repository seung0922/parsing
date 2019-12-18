package org.ezcode.demo.domain;

import lombok.Data;

/**
 * SearchDTO
 */
@Data
public class SearchDTO {

	private Integer pno;
    private String keyword;
    private String lang;
    private String siteLink;
	private String comment;
	
	public String[] getKeywords() {
		if(keyword == null || keyword.trim().length() == 0) {
			return null;
		}
		
		return keyword.split(" ");
	}

	public String[] getLangs() {
		if(lang == null || lang.trim().length() == 0) {
			return null;
		}
		
		return lang.split(" ");
    }
}