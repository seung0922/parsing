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
		
		subDirList2(saveDir, "연습용");
		
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
		
		log.info(mapper.selectAll(dto));
	}
	
	@Test
	public void ddtest() {
		log.info("sss");
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
						if (lang.equals("java") || lang.equals("py") || lang.equals("sql")) { // 언어 조건

							boolean isComment = false; // 여러줄 주석
							boolean isCode = false; // 검색에 해당되는 코드
							boolean isBrace = false; // 브레이스

							boolean tenline = false; // 열줄만 출력하는 변수
							boolean flag = false; // 한줄에 브레이스 짝이 맞을 때.

							Stack<String> checkBrace = new Stack<String>(); // 브레이스 개수 체크

							String code = "";

							int tbrace = 0; // { } 한쌍 일때 +1 해주려는 변수

							int k = 0;

							// 한줄씩 읽어온다
							for (int i = 1; (line = br.readLine()) != null; i++) {

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

								// -----------------------------------------------------------------------------------------------------------

								// 2. 한줄 주석
								if (line.contains("//")) {

									// 주석인데 해당 키워드 존재할때
									if (!(!line.trim().startsWith("//") && line.contains("\"")
											&& line.contains("//"))) {

										for (int n = 0; n < keywords.length; n++) {
											if (line.contains(keywords[n])) {
												vo = new ParseVO();

//														System.out.println("====================================================");
//														System.out.println("검색 키워드 <" + keywords[n] + ">");
//														System.out.println("해당 주석: " + line);
//														System.out.println("----------------------------------------------------");

												code = "";

												vo.setKeyword(keywords[n]);
												vo.setComment(line);
												vo.setPath(fpath);

												isCode = true; // 코드 시작을 알려줌

												k = i + 10; // 아래 10줄만 출력하기 위한 변수

											}

										} // end of for
									}

								} // 한줄 주석 끝
//					-----------------------------------------------------------------------------------------------------------
								// 브레이스 유무
								if (isCode) {
									isBrace = (line.contains("{") || line.contains("{")) ? true : isBrace;
								}

								if (isCode && tenline) {
									if (i <= k) {
										code += i + ":" + line + "\n";
										isBrace = false;
									}
									if (i == k + 1) {

										vo.setCode(code);
										vo.setLang(lang);
										result.add(vo);

//											System.out.println(code);
										tenline = false;
										code = "";
										flag = false;
										isBrace = false;
										isCode = false;
									}
								}

								// 브레이스 있을 때
								else if (isCode && isBrace) {
									code += i + ":" + line + "\n";

									if (flag && tbrace >= 2) {
										tenline = true;
										tbrace = 0;
									}
									else if (isCode && !flag) {
										// 브레이스 개수 체크
										if (line.contains("{"))
											checkBrace.push("{");
										if (line.contains("}"))
											checkBrace.pop();

										// 브레이스 체크 스택 비었으면 코드 출력안함
										if (checkBrace.empty()) {
											vo.setCode(code);
											vo.setLang(lang);
											result.add(vo);
											
											isBrace = false;
											isCode = false;
											continue;
										}
									}

									isBrace = false;
								}
								
								else if(isCode && !isBrace) {
									code += i + ":" + line + "\n";
									
									if(i == k + 1) {
										vo.setCode(code);
										vo.setLang(lang);
										result.add(vo);
										
										code = "";
										tenline = false;
										isCode = false;
									}
								}
								
								continue;
//					-----------------------------------------------------------------------------------------------------------
							} // line출력 for문 끝
							isComment = false;
							isCode = false;
							isBrace = false;
							
							tenline = false;
							flag = false;
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
