package org.parse.contoller;

import java.util.stream.IntStream;

public class JavaEx {
	public static void main(String[] args) {

		// 메소드 주석 문자열 테스트
		String str = "//문자열";
		String str2 = "//메소드 주석으로 인식되면 안됨";
		
		//String str4 = "강아지";

		/*
		 *
		 * 찍기 반복문 for문
		 *
		 */
		for (int i = 1; i <= 5; i++) {
			for (int j = 1; j <= 5; j++) {
				System.out.print("*");
			}
			System.out.println();
			
		}
		
		
		// asdfdsfsd{dsfd}
		String aaa = "aaaa";
		IntStream intStream2 = IntStream.of(new int[]{1,2,3,4,5,6});

		
		// 별찍기 피라미드 반복문 for문
		for (int i = 1; i <= 5; i++) {
			for (int j = 1; j <= i + 4; j++) {
				if (i+j >= 6) {
					System.out.print("*");
				} else {
					System.out.print(" ");
				}

			}
			System.out.println();
		}

		// 연습용
		for (int i = 1; i <= 9; i++) { // 이런
			for (int j = 1; j <= 3; j++) { // 고민
				int x = j + 1 + (i - 1) / 3 * 3;
				int y = i % 3 == 0 ? 3 : i % 3;

				if (x > 9)
					break;

				System.out.print(x + "*" + y + "=" + x * y + "\t");
			}
			System.out.println();
			if (i % 3 == 0)
				System.out.println();
		}


		/*
		 *
		 * 구구단 반복문 for문
		 *
		 */
		// 연습용
		for (int i = 1; i <= 9; i++) { // 짝수
			for (int j = 1; j <= 3; j++) { // 고민
				int x = j + 1 + (i - 1) / 3 * 3;
				int y = i % 3 == 0 ? 3 : i % 3;

				if (x > 9)
					break;

				System.out.print(x + "*" + y + "=" + x * y + "\t");
			}
			System.out.println();
			if (i % 3 == 0)
				System.out.println();
		}

		// {{ 2개,  }} 2개 -> 끝

		/*
		 *
		 * 숫자 배열 스트림 예제 IntStream
		 *
		 */
		// 별찍기~~~~~
		IntStream intStream = IntStream.of(new int[]{1,2,3,4,5,6});


		/*
		 *
		 * 예제 IntStream
		 *
		 */
		
		// 별찍기ㅠㅠㅠ
		IntStream ex1 = IntStream.of();
		IntStream ex2 = IntStream.of();
		IntStream ex3 = IntStream.of();

		
		String str3 = "//메소드 출력되면 안됨";

		/*
		 *
		 * 여러 줄 주석안에
		 * // 주석있는 경우
		 *
		 * */
	}
}
