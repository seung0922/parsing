package org.parse.mapper;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
import java.util.stream.Stream;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.parse.domain.ParseVO;
import org.parse.dto.SearchDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import lombok.extern.log4j.Log4j;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("file:src/main/webapp/WEB-INF/spring/root-context.xml")
@Log4j
public class ParseMapperTests {

	@Autowired
	private ParseMapper mapper;
	
	private List<ParseVO> result = new ArrayList<ParseVO>();
	
	@Test
	public void insertTest() {
		
		String saveDir = "C:\\ezcode";
		
		subDirList2(saveDir, "int[]", "all");
		
		result.forEach(r -> {
			log.info(r);
			log.info(mapper.insertCode(r));
		});
	}
	
	@Test
	public void selectAll() {
		
		SearchDTO dto = new SearchDTO();
		
		dto.setKeyword("별찍기,찍기,연습용");
		dto.setLang("java");
		
		mapper.selectAll(dto).forEach(i -> {
			log.info(i.getCode().replace("\r\n", "<br>"));
		});
		
//		log.info(mapper.selectAll(dto));
	}
	
	@Test
	public void ddtest() {
		log.info("sss");
	}
	
	// 검색 메소드
	public List<ParseVO> subDirList2(String path, String keyword, String comment) {

		ParseVO vo = new ParseVO();

		boolean keywordOn = false;
		boolean allKeyword = false;			// 모든 키워드 찾는 변수
		
		if(comment.equals("comment"))  {
			keywordOn = true;
		} else {
			allKeyword = true;
		}

		String[] keywords = keyword.split(",");
		
		Stream.of(keywords).forEach(y -> {
			System.out.println("개발 끝났져??  " +  y);
		});

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
						if (lang.equals("java") || lang.equals("py") || lang.equals("sql")) { // 언어 조건

							boolean isComment = false; // 여러줄 주석
							boolean isCode = false; // 검색에 해당되는 코드
							boolean isBrace = false; // 브레이스

							boolean tenline = false; // 열줄만 출력하는 변수
							boolean flag = false; // 한줄에 브레이스 짝이 맞을 때
							int count = 0;

							Stack<String> checkBrace = new Stack<String>(); // 브레이스 개수 체크

							String code = "";

							int tbrace = 0; // { } 한쌍 일때 +1 해주려는 변수

							int k = 0;

							// 한줄씩 읽어온다
							for (int i = 1; (line = br.readLine()) != null; i++) {
								
								if(!line.startsWith("import ")) {
									count++; // 세줄 이내로 브레이스가 없다면 10줄만 뿌려주는 변수.

									int open = 0;
									int close = 0;
	
									if (line.contains("{")) {
										open++;
									}
									if (line.contains("}")) {
										close++;
									}
	
									// { 이 한개 이상이고, { 와 } 쌍이 맞을 때
									if (open > 0 && open == close) {
										
										flag = true;
										tbrace++;
										
									} else {
										
										flag = false;
										tbrace = 0;
										
									}
									
									if (allKeyword) {	//키워드 전체검색
										for (int n = 0; n < keywords.length; n++) {
											if (line.indexOf(keywords[n]) != -1 && keywordOn == false) {
												
												vo = new ParseVO();
												
												keywordOn = true;
												
												System.out.println(
														"====================================================");
												System.out.println("검색 키워드 <" + keywords[n] + ">");
												System.out.println("해당 주석: " + line);
												System.out.println(
														"----------------------------------------------------");
												
												code = "";
												vo.setComment(0);
												vo.setKeyword(keywords[n]);
												vo.setPath(fpath);

												isCode = true; // 코드 시작을 알려줌

												k = i + 11; // 혹시나 열줄만 출력할거면 아래 10줄만 출력하기 위한 변수
											}
										}
									} // if(allKeyword)
									
									
									if(!allKeyword) {
									
										// 1. 여러줄 주석
										if (line.trim().startsWith("/*")) {
											isComment = true;
										}
		
										// 주석일때
										if (isComment) {
											for (int n = 0; n < keywords.length; n++) {
												if (line.contains(keywords[n])) {
													vo = new ParseVO();
		
//													System.out.println("====================================================");
//													System.out.println("검색 키워드 <" + keywords[n] + ">");
//													System.out.println("해당 주석: " + line);
//													System.out.println("----------------------------------------------------");
		
													code = "";
													vo.setComment(1);
													vo.setKeyword(keywords[n]);
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
										
									} //if(!allKeyword)

								// -----------------------------------------------------------------------------------------------------------

								// 2. 한줄 주석
								if (line.contains("//") && !allKeyword) {	// 라인에 //가 포함되어있다.

									// 주석인데 해당 키워드 존재할때
									if (line.trim().startsWith("//")) {

										for (int n = 0; n < keywords.length; n++) {
											if (line.contains(keywords[n])) {
												vo = new ParseVO();

//													System.out.println("====================================================");
//													System.out.println("검색 키워드 <" + keywords[n] + ">");
//													System.out.println("해당 주석: " + line);
//													System.out.println("----------------------------------------------------");

												code = "";

												vo.setComment(1);
												vo.setKeyword(keywords[n]);
												vo.setPath(fpath);

												isCode = true; // 코드 시작을 알려줌

												k = i + 10; // 아래 10줄만 출력하기 위한 변수

											}

										} // end of for
									}

								} // 한줄 주석 끝
//				-----------------------------------------------------------------------------------------------------------
								// 브레이스 유무
								if (isCode) {
									isBrace = (line.contains("{") || line.contains("{")) ? true : isBrace;
								}

								if (isCode && tenline) {	//열 줄만 나오게 하는 코드이다.
									if (i <= k) {
										code += i + ":" + line + "\n";
										isBrace = false;
									}
									if (i == k + 1) {

//										if(keywordOn) {
//											vo.setComment(1);
//										} else if(allKeyword) {
//											vo.setComment(0);
//										}
										
										vo.setCode(code);
										vo.setLang(lang);
										result.add(vo);

//										System.out.println(code);
										flag = false;
										isBrace = false;
										isCode = false;
										tenline = false;
										keywordOn = false;

										code = "";
										
										count = 0;
									}
								}

								// 브레이스 있을 때
								else if (isCode && isBrace) {
									code += i + ":" + line + "\n";

									//코드는 시작했다.
									//브레이스가 열려있다.
									//flag = 한줄에 { } 둘 다 있을때 true.
									if(flag && tbrace >= 2) {		// 두 줄 연속으로 {} 있으면?
										tenline = true;		// 열줄만 출력하게 tenline이 트루가 됩니다.
										tbrace = 0;			// 어차피 열 줄만 출력하도록 마음먹었으니 tbrace는 0으로.
									}
									
									else if (isCode && !flag) {
										// 브레이스 개수 체크
										
										//라인에 {가 있으면 스택에 하나 추가
										if (line.contains("{")) checkBrace.push("{");
										
										//라인에 }가 있으면 스택 하나 팝팝
										if (line.contains("}")) checkBrace.pop();

										// 브레이스 체크 스택 비었으면 코드 출력안함
										if (checkBrace.empty()) {
											
//											if(keywordOn) {
//												vo.setComment(1);
//											} else if(allKeyword) {
//												vo.setComment(0);
//											}
											
											vo.setCode(code);
											vo.setLang(lang);
											result.add(vo);
											
											code = "";
											isBrace = false;
											isCode = false;
											keywordOn = false;
											continue;
										}
										
									}	// end if(!flag)

								}	// end if(isCode && isBrace)
								
								else if(isCode && !isBrace) {
									code += i + ":" + line + "\n";
									
									if(i == k + 1) {
										
//										if(keywordOn) {
//											vo.setComment(1);
//										} else if(allKeyword) {
//											vo.setComment(0);
//										}
										
										vo.setCode(code);
										vo.setLang(lang);
										result.add(vo);
										
										code = "";
										count = 0;

										tenline = false;
										isCode = false;
										keywordOn = false;
									}
								}
								
								continue;
//				-----------------------------------------------------------------------------------------------------------
								}
							} // line출력 for문 끝
							isComment = false; // 여러줄 주석
							isCode = false; // 검색에 해당되는 코드
							isBrace = false; // 브레이스

							tenline = false; // 열줄만 출력하는 변수
							flag = false; // 한줄에 브레이스 짝이 맞을 때.
							count = 0;
							keywordOn = false;
						}

						br.close();

					} catch (Exception e) {
						// TODO: handle exception
					}

				} else if (f.isDirectory()) {
					// 하위 디렉토리 존재 시 다시 탐색
					subDirList2(f.getCanonicalPath().toString(), keyword, comment);
				}

			}

		} catch (IOException e) {

		}

		return result;

	} // subDirList2()
}
