package org.ezcode.demo.controller;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
import java.util.stream.Stream;

import org.ezcode.demo.domain.PageMaker;
import org.ezcode.demo.domain.ParseVO;
import org.ezcode.demo.domain.SearchDTO;
import org.ezcode.demo.service.ParseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Controller
@RequestMapping("/search")
@Slf4j
public class SearchController {

	@Setter(onMethod_ = @Autowired)
	private ParseService parseService;

	private List<ParseVO> result = new ArrayList<ParseVO>();

	@GetMapping("/index")
	public void search() {
		log.info("get index....");
	}

	@GetMapping("/expage")
	public void expage() {
		log.info("get index....");
	}

	@GetMapping("/list")
	public void searchList(@ModelAttribute("dto") SearchDTO dto, Model model) {
		
		// 1. github 검색일 때

		if(dto.getKeyword().equals("")) {

			model.addAttribute("count", 0);
			model.addAttribute("list", new ParseVO());

		} else {

			log.info("parse get..........................");
			log.info("searchDTO: " + dto);
			
			String kk = dto.getKeyword();

			log.info("kk??????????????????....." + kk);

			// 키워드를 하나씩 찾아서 없으면 db에 넣는다
			String[] keywordArr = dto.getKeywords();

			Stream.of(keywordArr).forEach(k -> {

				// 키워드 한개로 지정해놓는다.
				dto.setKeyword(k);

				// 파싱한 결과 저장하는 리스트
				result = new ArrayList<ParseVO>();

				// 키워드 하나 지정한 걸로 검색했는데 null 뜨면
				if (parseService.find(dto).isEmpty()) {

					log.info("널임");

					// 1파일 저장된 경로에서 해당키워드로 파싱한 다음 DB에 저장시킴
					final String saveDir = "C:\\ezcode";

					log.info("keyword..........................." + k);

					result = playParsing(saveDir, k, dto.getComment());

					log.info("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@");
					log.info("" + result);
					log.info("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@");

					result.forEach(r -> {
						log.info("" + r);
						parseService.insertCode(r);
					});

				} 

			});

			// 총 결과 개수
			dto.setKeyword(kk);
			
			model.addAttribute("list", parseService.findAll(dto));
			model.addAttribute("pm", new PageMaker(parseService.getCount(dto), dto));
		}

		//----------------------------------------------------------------
		// 2. google 검색일때


	}

	@GetMapping("/detail")
	public void searchDetail(@ModelAttribute("dto") SearchDTO dto,  Model model) {

		log.info("get index...............................");

		log.info("" + dto);

		ParseVO result = parseService.findByPno(dto.getPno());

		model.addAttribute("result", result);
	}

	// ----------------------------------------------------------------------------
	// 지정 폴더에서 해당 키워드 찾아 파싱하는 메서드
	public List<ParseVO> playParsing(String path, String keyword, String comment) {

		ParseVO vo = new ParseVO();

		int maxLine = 0;
		boolean keywordOn = false;
		boolean allKeyword = false; // 모든 키워드 찾는 변수

		if(comment.equals("c")) {
			keywordOn = true;
		} else {
			allKeyword = true;
		}

		final String[] keywords = keyword.split(",");

		// Stream.of(keyword).forEach(y -> {
		// System.out.println("개발 끝났져?? " + y);
		// });

		final File file = new File(path);

		final File[] fileList = file.listFiles();

		try {
			for (final File f : fileList) {
				if (f.isFile()) { // 파일일때
					/*
					 * 예외처리
					 */
					try {
						final FileReader fr = new FileReader(f.getCanonicalPath());
						final BufferedReader br = new BufferedReader(fr);

						String line = "";

						final String fpath = f.getPath();

						// 확장자 얻어오기
						final int langIdx = f.getName().lastIndexOf('.');
						final String lang = f.getName().substring(langIdx + 1);

						// 파일 저장된 경로와 파일명 얻어오기
						final int idx = fpath.lastIndexOf("\\");
						final String fname = fpath.substring(idx + 1);
						final String fnamePath = fpath.substring(0, idx);

						/*
						 * if 조건문
						 */
						if (lang.equals("java") || lang.equals("py") || lang.equals("sql")) { // 언어 조건

							boolean isComment = false; // 여러줄 주석
							boolean isCode = false; // 검색에 해당되는 코드
							boolean isBrace = false; // 브레이스
							boolean blank = false;

							boolean tenline = false; // 열줄만 출력하는 변수
							boolean flag = false; // 한줄에 브레이스 짝이 맞을 때

							final Stack<String> checkBrace = new Stack<String>(); // 브레이스 개수 체크

							String code = "";

							int tbrace = 0; // { } 한쌍 일때 +1 해주려는 변수

							int k = 0;

							// 한줄씩 읽어온다
							for (int i = 1; (line = br.readLine()) != null; i++) {

								if (!line.trim().startsWith("import ")) {

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

									if (allKeyword && !keywordOn) { // 키워드 전체검색
										for (int n = 0; n < keywords.length; n++) {
											if ((line.toLowerCase().contains(keywords[n].toLowerCase()) || line.toUpperCase().contains(keywords[n].toUpperCase()))) {

												vo = new ParseVO();

												keywordOn = true;

												code = "";

												maxLine = 0;

												vo.setComment(0);
												vo.setKeyword(keywords[n]);
												vo.setPath(fnamePath);
												vo.setFname(fname);

												isCode = true; // 코드 시작을 알려줌
												vo.setStart(i);

												k = i + 11; // 혹시나 열줄만 출력할거면 아래 10줄만 출력하기 위한 변수
											}
										}
									} // if(allKeyword)

									if (!allKeyword) {

										// 1. 여러줄 주석
										if (line.trim().startsWith("/*")) {
											isComment = true;
										}

										// 주석일때
										if (isComment) {
											for (int n = 0; n < keywords.length; n++) {
												if ((line.toLowerCase().contains(keywords[n].toLowerCase()) || line.toUpperCase().contains(keywords[n].toUpperCase()))) {
													vo = new ParseVO();

													code = "";

													maxLine = 0;

													vo.setComment(1);
													vo.setKeyword(keywords[n]);
													vo.setPath(fnamePath);
													vo.setFname(fname);
													vo.setStart(i);

													isCode = true;

												}
											}

											// 주석 끝 (여러줄 주석)
											if (line.trim().endsWith("*/")) {
												isComment = false;
												continue;
											}

										} // if(iscomment)

									} // if(!allKeyword)

									// -----------------------------------------------------------------------------------------------------------

									// 2. 한줄 주석
									if (line.contains("//") && !allKeyword) { // 라인에 //가 포함되어있다.

										// 주석인데 해당 키워드 존재할때
										if (line.trim().startsWith("//")) {

											for (int n = 0; n < keywords.length; n++) {
												if ((line.toLowerCase().contains(keywords[n].toLowerCase()) || line.toUpperCase().contains(keywords[n].toUpperCase()))) {
													vo = new ParseVO();

													code = "";

													maxLine = 0;

													vo.setComment(1);
													vo.setKeyword(keywords[n]);
													vo.setPath(fnamePath);
													vo.setFname(fname);
													vo.setStart(i);

													isCode = true; // 코드 시작을 알려줌

													k = i + 10; // 아래 10줄만 출력하기 위한 변수

												}

											} // end of for
										}

									} // 한줄 주석 끝
									// -----------------------------------------------------------------------------------------------------------
									// 브레이스 유무
									// if (maxLine >= 30){

									// vo.setCode(code);
									// vo.setLang(lang);
									// result.add(vo);
									// maxLine = 0;
									// continue;
									// }

									maxLine++;

										if (isCode) {
											isBrace = (line.contains("{") || line.contains("{")) ? true : isBrace;
										}

										if (isCode && tenline) { // 열 줄만 나오게 하는 코드이다.
											if (i <= k) {
												code += line + "\r\n";
												isBrace = false;
											}
											if (i == k + 1 || maxLine >= 30) {

												vo.setCode(code);
												vo.setLang(lang);
												result.add(vo);

												// System.out.println(code);
												flag = false;
												isBrace = false;
												isCode = false;
												tenline = false;
												keywordOn = false;

												code = "";

											}
										}

										// 브레이스 있을 때
										else if (isCode && isBrace) {
											code += line + "\r\n";

											// 코드는 시작했다.
											// 브레이스가 열려있다.
											// flag = 한줄에 { } 둘 다 있을때 true.
											if (flag && tbrace >= 2) { // 두 줄 연속으로 {} 있으면?
												tenline = true; // 열줄만 출력하게 tenline이 트루가 됩니다.
												tbrace = 0; // 어차피 열 줄만 출력하도록 마음먹었으니 tbrace는 0으로.
											}

											else if (isCode && !flag) {
												// 브레이스 개수 체크

												// 라인에 {가 있으면 스택에 하나 추가
												if (line.contains("{")) {
													checkBrace.push("{");
													blank = false;
												}

												// 라인에 }가 있으면 스택 하나 팝팝
												if (line.contains("}")) {
													checkBrace.pop();
													if (checkBrace.empty())
														blank = true;
												}

												// 브레이스 체크 스택 비었으면 코드 출력안함
												if ((checkBrace.empty() && blank) || maxLine >= 30) {

													vo.setCode(code);
													vo.setLang(lang);
													result.add(vo);

													code = "";
													isBrace = false;
													isCode = false;
													keywordOn = false;
													continue;
												}

											} // end if(!flag)

										} // end if(isCode && isBrace)

										else if (isCode && !isBrace) {
											code += line + "\r\n";

											if (i == k + 1 || maxLine >= 30) {

												vo.setCode(code);
												vo.setLang(lang);
												result.add(vo);

												code = "";

												tenline = false;
												isCode = false;
												keywordOn = false;
											}
										}

									// -----------------------------------------------------------------------------------------------------------
								}
							} // line출력 for문 끝
							isComment = false; // 여러줄 주석
							isCode = false; // 검색에 해당되는 코드
							isBrace = false; // 브레이스

							tenline = false; // 열줄만 출력하는 변수
							flag = false; // 한줄에 브레이스 짝이 맞을 때.
							keywordOn = false;
						}

						br.close();

					} catch (final Exception e) {
						// TODO: handle exception
					}

				} else if (f.isDirectory()) {
					// 하위 디렉토리 존재 시 다시 탐색
					playParsing(f.getCanonicalPath().toString(), keyword, comment);
				}

			}

		} catch (final IOException e) {

		}

		return result;

	} // subDirList2()

}
