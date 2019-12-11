package org.parse.mapper;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.parse.domain.ParseVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.StringUtils;

import lombok.extern.log4j.Log4j;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("file:src/main/webapp/WEB-INF/spring/root-context.xml")
@Log4j
public class ParseMapperTests {

	@Autowired
	private ParseMapper mapper;
	
	private static List<ParseVO> result = new ArrayList<ParseVO>();
	
	@Test
	public void insertTest() {
		
		String saveDir = "C:\\ezcode";
		
		subDirList2(saveDir, "별찍기, 메소드, asdfdsfsd, 이런");
		
		result.forEach(r -> {
			log.info(r);
			log.info(mapper.insertCode(r));
		});
	}
	
	@Test
	public void selectAll() {
		log.info(mapper.selectAll("헹헹"));
	}
	
	
	// 검색 메소드
	public static List<ParseVO> subDirList2(String path, String keyword) {
		
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
						
						int idx = f.getName().lastIndexOf('.');

						String lang = f.getName().substring(idx + 1);
						
						/*
						 * if 조건문
						 */
						if (lang.equals("java")) { // java 파일일때
							System.out.println("------------------------------------------------------");

							// 파일이름 출력
							//System.out.println("[" + f.getName() + "]");
							
//								vo.setLanguage(lang);

							boolean isComment = false; 	// 여러줄 주석
							boolean isCode = false; 	// 검색에 해당되는 코드
							boolean isBrace = false; 	// 브레이스
							
							boolean tenline = false;	// 열줄만 출력하는 변수
							boolean flag = false;		// 한줄에 브레이스 짝이 맞을 때.
							boolean check = false;
							int count = 0;
							
							Stack<String> checkBrace = new Stack<String>(); // 브레이스 개수 체크
							
							
							String code = "";
							
							int tbrace = 0;
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
												
												System.out.println("====================================================");
												System.out.println("검색 키워드 <" + keywords[n] + ">");
												System.out.println("해당 주석: " + line);
												System.out.println("----------------------------------------------------");
												
												code = "";
												
												vo.setKeyword(keywords[n]);
												vo.setComment(line);
												
												isCode = true;
												
											}
										}
										
										// 주석 끝 (여러줄 주석)
										if (line.trim().endsWith("*/")) {
											isComment = false;
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
													
													System.out.println("====================================================");
													System.out.println("검색 키워드 <" + keywords[n] + ">");
													System.out.println("해당 주석: " + line);
													System.out.println("----------------------------------------------------");
											
													code = "";
													
													vo.setKeyword(keywords[n]);
													vo.setComment(line);
													
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
										//result.add(vo);
										
										System.out.println(code);
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
										
										if(!check) {
											
											vo.setCode(code);
											vo.setLanguage(lang);
											result.add(vo);
											System.out.println(vo);
										}
//											System.out.println(result);
										
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
										System.out.println(vo);
										
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
