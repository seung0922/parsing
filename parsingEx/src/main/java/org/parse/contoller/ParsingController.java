package org.parse.contoller;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import org.parse.domain.ParseVO;
import org.parse.service.ParseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import lombok.Setter;
import lombok.extern.log4j.Log4j;

@Controller
@Log4j
public class ParsingController {

	@Setter(onMethod_ = @Autowired)
	private ParseService service;
	
	private List<ParseVO> result = new ArrayList<ParseVO>();
	
	
	@GetMapping("/main")
	public void mainGET() {
		log.info("main.......................get");
	}
	
	@PostMapping("/main")
	public String mainPOST(String keyword, RedirectAttributes rttr) {
		
		log.info("main.......................post");
		
		rttr.addAttribute("keyword", keyword);
		
		return "redirect:/parse";
		
	}

	
	@GetMapping("/parse")
	public void parseGet(@ModelAttribute("keyword") String keyword, Model model) {
		
		result = new ArrayList<ParseVO>();
		
		log.info("parse get.........");
		log.info("keyword: " + keyword);
		
		// 키워드로 검색을 함
		List<ParseVO> list = service.selectAll(keyword);
		
		log.info("list...................." + list);
		
		// 1. 검색했는데 null 뜨면
		if(list.isEmpty()) {
			log.info("널임");
			// 1-1. 파일 저장된 경로에서 해당키워드로 파싱한 다음 DB에 저장시킴
			String saveDir = "C:\\ezcode";
			
			result = subDirList2(saveDir, keyword);
			
//			log.info("-----------------------------------------------");
//			log.info(result);
//			log.info("-----------------------------------------------");
			
			result.forEach(r -> {
//				log.info(r);
				log.info(service.insertCode(r));
			});
			
			// 1-2. 해당 키워드로 다시 DB에서 찾은 뒤 보내줌
			List<ParseVO> list2 = service.selectAll(keyword);
			
			log.info(list2);
			
			model.addAttribute("list", list2);
			
		} else {
			// 2. 검색했는데 결과 나옴
			// 2-1. 보내줌
			log.info("널아냐");
			log.info(list);
			model.addAttribute("list", list);
			
		}
		
	}
	
	
	// 검색 메소드
	public List<ParseVO> subDirList2(String path, String keyword) {
		
		ParseVO vo = new ParseVO();
		
		String[] keywords = keyword.split(",");

		File file = new File(path);

		File[] fileList = file.listFiles();

		try {
			for (File f : fileList) {
				if (f.isFile()) { // 파일일때
					/*
					 * 예외처리
					 */
					try {
						FileReader fr = new FileReader(f.getCanonicalPath());
						BufferedReader br = new BufferedReader(fr);

						String line = "";
						
						String fpath = f.getPath();
						
						int idx = f.getName().lastIndexOf('.');

						String lang = f.getName().substring(idx + 1);
						
						/*
						 * if 조건문
						 */
						if (lang.equals("java")) { // java 파일일때

							boolean isComment = false; 	// 여러줄 주석
							boolean isCode = false; 	// 검색에 해당되는 코드
							boolean isBrace = false; 	// 브레이스
							
							boolean tenline = false;	// 열줄만 출력하는 변수
							boolean flag = false;		// 한줄에 브레이스 짝이 맞을 때.
							boolean check = false;
							
							int count = 0;
							
							Stack<String> checkBrace = new Stack<String>(); // 브레이스 개수 체크
							
							String code = "";
							
							int tbrace = 0;		// { } 한쌍 일때 +1 해주려는 변수
							
							int k = 0;

							// 한줄씩 읽어온다
							for (int i = 1; (line = br.readLine()) != null; i++) {
								
								count++;		//세줄 이내로 브레이스가 없다면 10줄만 뿌려주는 변수.
								
								int open = 0;
								int close = 0;
								
								
								if(line.contains("{")) {
									open++;
								}
								if(line.contains("}")) {
									close++;
								}
								

								// { 이 한개 이상이고, { 와 } 쌍이 맞을 때 
								if(open > 0 && open == close) {
									flag = true;
									tbrace++;
								} else {
									flag = false;
									tbrace = 0;
								}
								
								
								if(!tenline) {
									// 1. 여러줄 주석
									if (line.trim().startsWith("/*")) {
										isComment = true;
									}
	
									// 주석일때
									if (isComment) {
										for (int n = 0; n < keywords.length; n++) {
											if (line.contains(keywords[n])) {
												vo = new ParseVO();
												
//												System.out.println("====================================================");
//												System.out.println("검색 키워드 <" + keywords[n] + ">");
//												System.out.println("해당 주석: " + line);
//												System.out.println("----------------------------------------------------");
												
												code = "";
												
												vo.setKeyword(keywords[n]);
												vo.setComment(line);
												vo.setPath(fpath);
												
												isCode = true;
												
											}
										}
										
										// 주석 끝 (여러줄 주석)
										if (line.trim().endsWith("*/")) {
											isComment = false;
											continue;
										}
										
									} // if(iscomment)
									
	//			-----------------------------------------------------------------------------------------------------------								
									
									// 2. 한줄 주석
									if (line.contains("//")) {
	
										// 주석인데 해당 키워드 존재할때
										if ( !(!line.trim().startsWith("//") && line.contains("\"")
												&& line.contains("//")) ) {
											
											for (int n = 0; n < keywords.length; n++) {
												if (line.contains(keywords[n])) {
													vo = new ParseVO();
													
//													System.out.println("====================================================");
//													System.out.println("검색 키워드 <" + keywords[n] + ">");
//													System.out.println("해당 주석: " + line);
//													System.out.println("----------------------------------------------------");
											
													code = "";
													
													vo.setKeyword(keywords[n]);
													vo.setComment(line);
													vo.setPath(fpath);
													
													isCode = true; 	// 코드 시작을 알려줌
													
													k = i + 10;		// 아래 10줄만 출력하기 위한 변수
													
													
												}
												
											} // end of for
										}
										
									} // 한줄 주석 끝
								}
//				-----------------------------------------------------------------------------------------------------------
								// 브레이스 유무
								if(isCode && !tenline) {
									isBrace = line.contains("{") ? true : isBrace;
								}
								
								if(tenline) {
									if (i <= k) {
										code += i+ ":" + line + "\n";
									} if(i == k + 1) {
										
										vo.setCode(code);
										vo.setLanguage(lang);
										result.add(vo);
										
//										System.out.println(code);
										tenline = false;
										count = 0;
									}
								}
								
								// 브레이스 있을 때
								else if (isCode && isBrace) {
									
									count = 0;
									
									code += i+ ":" + line + "\n";
									
									if(flag && tbrace < 3) {
										checkBrace.push("-");
										////////////////
										flag = false;
										tenline = true;
										check = true;
									}
									if (!flag) {
										check = false;
										for(int s = 0; s < checkBrace.size(); s++) {
											if(checkBrace.search("-") != -1) {
												checkBrace.pop();
											}
										}
										
									}
									
									// 브레이스 개수 체크
									if (line.contains("{")) checkBrace.push("{");
									if (line.contains("}")) checkBrace.pop();

									// 브레이스 체크 스택 비었으면 코드 출력안함
									if (checkBrace.empty()) {
										
										isBrace = false;
										isCode = false;
										
										System.out.println("코@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@드ㄸ@ㄸ@ㄸ@ㄸ@ㄸ@ㄸ" + code);
										if(!check && !tenline) {
											
											vo.setCode(code);
											vo.setLanguage(lang);
											result.add(vo);
											log.info("333333333@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@");
//											System.out.println(vo);
										}
										
										//System.out.println(result);
										
									}

								} else if(isCode) { // 브레이스 없을 때 ( 10줄 출력 해주기.... )
									
									if(count == 3) {
										tenline = true;
									}
									
									if (i <= k) {
										code += i+ ":" + line + "\n";
									} if(i == k+1) {
										vo.setCode(code);
										vo.setLanguage(lang);
										result.add(vo);
										log.info("11$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$");
//										System.out.println(vo);
										tenline = true;
										
									}
								}
								
//				-----------------------------------------------------------------------------------------------------------
							} // line출력 for문 끝
							
						}

						br.close();

					} catch (Exception e) {
						// TODO: handle exception
					}

				} else if (f.isDirectory()) {
					// 하위 디렉토리 존재 시 다시 탐색
					subDirList2(f.getCanonicalPath().toString(), keyword);
				}

			}

		} catch (IOException e) {

		}
		
		return result;

	} // subDirList2()
}
