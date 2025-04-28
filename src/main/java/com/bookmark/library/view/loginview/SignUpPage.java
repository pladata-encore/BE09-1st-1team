package com.bookmark.library.view.loginview;

import com.bookmark.library.auth.LoginContext;
import com.bookmark.library.service.MemberService;
import com.bookmark.library.service.Services;
import com.bookmark.library.util.IO;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.function.Function;

public class SignUpPage {
    private final MemberService signUpService = Services.resolve(MemberService.class);
    private static final String MANDATORY = IO.RED + "*" + IO.RESET;

    public void run() {
        System.out.println("\n====[ 회원가입 ]====");

        // ID 입력
        String member_id = read("아이디", true, s -> {
            if (signUpService.isDuplicateId(s)) {
                throw new InvalidInputException("사용할 수 없는 아이디입니다. 다른 아이디를 입력해 주세요.");
            }
            return s;
        });

        // PW 입력
        String password = read("비밀번호", true, s -> {
            if (s.length() < 8) {
                throw new InvalidInputException("비밀번호가 너무 짧습니다. 8자 이상 입력해주세요.");
            }
            return s;
        });

        // 사용자명 입력
        String username = read("이름", true);

        // birth_date 입력
        LocalDate birth_date = read("생년월일 (yyyy-MM-dd)", true, s -> {
            try {
                return LocalDate.parse(s);
            } catch (DateTimeParseException e) {
                throw new InvalidInputException("⚠️ 형식이 잘못되었습니다. 예: 1999-04-25");
            }
        });

        String phone_number = read("전화번호");
        String email = read("이메일");

        if (!IO.confirm("회원가입 하시겠습니까?")) {
            return;
        }

        boolean result = signUpService.saveUser(
                member_id, password, username, birth_date, phone_number, email
        );

        if (result) {
            LoginContext.login(member_id, password);
            System.out.println("\n🎉 회원가입이 완료되었습니다!");
            System.out.println("환영합니다, " + username + "님!");
            IO.pressEnterToProceed();
        }
    }

    private String read(String label) {
        return read(label, false);
    }

    private String read(String label, boolean isMandatory) {
        return read(label, isMandatory, s -> s);
    }

    private <T> T read(String label, boolean isMandatory, Function<String, T> converter) {
        while (true) {
            System.out.print(label + (isMandatory ? MANDATORY : "") + " : ");
            String input = IO.scanner.nextLine().trim();
            if (input.isBlank() && isMandatory) {
                IO.error("필수 정보입니다. 입력해주세요.");
                continue;
            }
            try {
                return converter.apply(input);
            } catch (InvalidInputException e) {
                IO.error(e.getMessage());
            }
        }
    }

    private static class InvalidInputException extends RuntimeException {
        public InvalidInputException(String message) {
            super(message);
        }
    }
}