package com.bookmark.library.util;

import java.util.Scanner;

public class IO {
    public static final Scanner scanner = new Scanner(System.in);
    public static final String RED = "\u001B[31m";
    public static final String RESET = "\u001B[0m";

    public static int readIntLine(String prompt) {
        while (true) {
            System.out.print(prompt);
            try {
                return scanner.nextInt();
            } catch (RuntimeException ignored) {
            } finally {
                scanner.nextLine();
            }
        }
    }

    /**
     * 사용자로부터 메뉴 선택 번호를 입력받습니다.
     * 유효하지 않은 번호가 입력되면 재입력을 요구합니다.
     * @param max 유효한 메뉴 번호의 최대값
     * @return 입력된 메뉴 번호
     */
    public static int selectMenu(int max) {
        return selectMenu(">>> ", max);
    }

    public static int selectMenu(String prompt, int max) {
        while (true) {
            int menu = readIntLine(prompt);
            if (0 <= menu && menu <= max) {
                return menu;
            }
        }
    }

    public static boolean confirm(String message) {
        System.out.print(message + " (y/n) ");
        String input = scanner.nextLine();
        return input.equalsIgnoreCase("y");
    }

    public static void pressEnterToProceed() {
        System.out.print("\n(확인: ENTER)");
        scanner.nextLine();
    }

    public static void error(String message) {
        System.out.println(RED + message + RESET);
    }
}
