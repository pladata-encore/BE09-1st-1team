package com.bookmark.library.view.loginview;

import com.bookmark.library.auth.LoginContext;
import com.bookmark.library.exception.ReturnToHomeException;
import com.bookmark.library.model.Member;
import com.bookmark.library.service.MemberService;
import com.bookmark.library.util.IO;

public class LoginPage {
    public static void run() {
        while (true) {
            System.out.print("아이디: ");
            String username = IO.scanner.nextLine();

            System.out.print("비밀번호: ");
            String password = IO.scanner.nextLine();

            var user = LoginContext.login(username, password);
            if (user != null) {
                System.out.println("✅ 로그인 성공!");
                System.out.println("환영합니다, " + user.username() + "님!");
                break;
            } else if (!IO.confirm("❌ 로그인 실패. 다시 시도하시겠습니까?")){
                break;
            }
        }
    }
}
