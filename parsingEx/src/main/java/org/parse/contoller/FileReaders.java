package org.parse.contoller;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

// /**/ 주석 파싱
// 1.깃허브에 파일 준비 (코멘트 작성된)
//
// ..자바
// 2.링크 통해서 다운로드
// 3.다운로드 받은 zip 파일 압축 해제
// 4.파일 파싱 (주석)

public class FileReaders {

//		C:\Users\5CLASS-184\Spring\codeEx\src\codeEx
//		System.getProperty("user.dir")

	public static void main(String[] args) {

		// 실행하고 있는 현재 위치
		subDirList(System.getProperty("user.dir"));

	}

	/*
	 * 파일 탐색
	 *
	 */
	public static void subDirList(String path) {
		String[] keywords = { "구구단", "별찍기", "메소드" };
//		String[] keywords = { "한줄", "별찍기" };

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

						/*
						 * if 조건문
						 */
						if (f.getName().indexOf("JavaEx.java") != -1) {	// java 파일일때
							System.out.println("------------------------------------------------------");
							
							// 파일이름 출력
							System.out.println("[" + f.getName() + "]");

							boolean isComment = false;		// 주석
							boolean isCode = false;			// 코드
							boolean isBraceCode = false;	// { } 괄호

							int start = 0;
							int end = 0;
							int k = 0;
							

							// 한줄씩 읽어온다
							for (int j = 1; (line = br.readLine()) != null; j++) {

								// 주석 시작 (여러줄 주석)
								if (line.trim().startsWith("/*")) {
									isComment = true;
									start = 0;
									end = 0;
								}

								// 주석일때
								if (isComment) {
									for (int n = 0; n < keywords.length; n++) {
										// 해당 키워드 있으면
										if (line.indexOf(keywords[n]) != -1) {
											// 키워드와 해당하는 주석 내용 한줄 출력
											System.out.println(keywords[n] + ": " + line);
											System.out.println("============================================================================================");

											isBraceCode = true; 	// 코드 시작
										}
									}

									// 주석 끝 (여러줄 주석)
									if (line.trim().endsWith("*/")) {
										isComment = false; // 주석 끝나면
									}
								} // if(iscomment)
//	-----------------------------------------------------------------------------------------------------------
								// 주석 일 때 (한줄)
								// 한 줄 주석일 때 브레이스 안걸림......
								if (line.indexOf("//") != -1) {
									if (!(!line.trim().startsWith("//") && line.contains("\"")
											&& line.contains("//"))) { // 주석일 때 "" X
										for (int n = 0; n < keywords.length; n++) {
											if (line.indexOf(keywords[n]) != -1) {
												System.out.println(
														"============================================================================================");
												isCode = true;
//												isComment = true;
												k = j + 11;
											}
										}
									}
								}

//	------------------------------------------------------------------------------------------------------------

								// 코드일때
								if(isCode) {

									// 브레이스로 시작할때 -> 출력
									// start 가 1이상 일때


									// 브레이스로 시작안할때 -> 출력
									// start 가 1이하 일때

								}


								// 검색 결과 코드 출력
								// 브레이스로 구분 가능
								if (isBraceCode) { // 코드일때

									// 해당 라인 출력
									System.out.println(j + ": " + line);

									// {} 짝맞는지 확인하고 마지막 } 나오면 출력 멈춤
									start = line.indexOf("{") != -1 ? start + 1 : start;
									end = line.indexOf("}") != -1 ? end + 1 : end;
									// {개수랑 }개수 같으면 isCode false
									if (start != 0) {
										isBraceCode = start == end ? false : true;
									}

								}

								// 브레이스로 구분 불가
								if (isCode && j < k) {
									System.out.println(j + ": " + line);
								}
							}
						}
						br.close();

					} catch (Exception e) {
						// TODO: handle exception
					}

				} else if (f.isDirectory()) {
					// 하위 디렉토리 존재 시 다시 탐색
					subDirList(f.getCanonicalPath().toString());
				}

			}

		} catch (IOException e) {

		}

	} // subDirList()

}