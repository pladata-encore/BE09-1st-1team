package com.bookmark.library.view.userinfoview;

import com.bookmark.library.auth.LoginContext;
import com.bookmark.library.model.Member;
import com.bookmark.library.service.MemberService;
import com.bookmark.library.service.Services;
import com.bookmark.library.util.IO;

public class InfoEditPage {
    private final MemberService memberService = Services.resolve(MemberService.class);

    public void run() {
        System.out.println("""
                
                ==== BOOKMARK ====
                ✏️ 개인정보 수정
                """);

        // 현재 로그인한 사용자 정보 가져오기
        Member user = LoginContext.getCurrentUser();
        if (user == null) {
            System.out.println("❌ 로그인 상태가 아닙니다. 먼저 로그인해주세요.");
            return;
        }

        String memberId = user.id();

        String password;
        while (true) {
            System.out.print("새 비밀번호 \u001B[31m*\u001B[0m : ");
            password = IO.scanner.nextLine().trim();
            if (!password.isBlank()) break;
            System.out.println("⚠️ 비밀번호는 필수 입력 항목입니다. 다시 입력해주세요.");
        }

        String newUsername;
        while (true) {
            System.out.print("새 사용자명 \u001B[31m*\u001B[0m : ");
            newUsername = IO.scanner.nextLine().trim();
            if (!newUsername.isBlank()) break;
            System.out.println("⚠️ 사용자명은 필수 입력 항목입니다. 다시 입력해주세요.");
        }

        System.out.print("새 전화번호: ");
        String phoneNumber = IO.scanner.nextLine();

        System.out.print("새 이메일: ");
        String email = IO.scanner.nextLine();

        boolean result = memberService.updateUser(
                memberId,
                password,
                newUsername,
                phoneNumber,
                email
        );

        if (result) {
            // 수정에 성공하면 업데이트된 현재 세션 정보를 갱신하기 위해 다시 로그인
            LoginContext.logout();
            Member updateUser = LoginContext.login(memberId, password);

            if (updateUser != null) {
                System.out.println("\n✅ 개인정보가 성공적으로 수정되었습니다!");

            } else {
                System.out.println("\n✅ 개인정보는 수정되었으나 세션 갱신에 실패했습니다. 다시 로그인해주세요.");
            }
        } else {
            System.out.println("\n❌ 수정에 실패했습니다. 다시 시도해주세요.");
        }
    }
}
