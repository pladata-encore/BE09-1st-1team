package com.bookmark.library.view.userinfoview;

import com.bookmark.library.auth.LoginContext;
import com.bookmark.library.model.Member;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class UserInfoPageTests {

    @Test
    public void testGetCurrentUser_Success() {
        // given
        String memberId = "testuser123";  // 실제 존재하는 사용자 ID
        String password = "testpass";      // 실제 비밀번호

        boolean loginResult = LoginContext.login(memberId, password);
        Assertions.assertTrue(loginResult, "로그인에 실패했습니다.");

        // when
        Member user = LoginContext.getCurrentUser();

        // then
        Assertions.assertNotNull(user, "로그인 후 사용자 정보가 null입니다.");
        Assertions.assertEquals(memberId, user.getId(), "사용자 ID가 일치하지 않습니다.");
        System.out.println("사용자명: " + user.getUsername());
    }

    @Test
    public void testGetCurrentUser_Fail_NotLoggedIn() {
        // given: 강제로 로그아웃한 상태로 만들기
//        LoginContext.logout(); // logout() 메서드가 LoginContext에 정의되어 있어야 함

        // when
        Member user = LoginContext.getCurrentUser();

        // then
        Assertions.assertNull(user, "로그인하지 않은 상태인데 사용자 정보가 반환되었습니다.");
    }
}
