package com.bookmark.library.view.loginview;

import com.bookmark.library.auth.LoginContext;
import com.bookmark.library.model.Member;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class LoginPageTests {

    @Test
    public void testLogin_Success() {
        // given
        String validId = "1234"; // 테스트용 유효 아이디
        String validPw = "1234";   // 테스트용 유효 비밀번호

        // when
        boolean result = LoginContext.login(validId, validPw);

        // then
        Assertions.assertTrue(result, "로그인이 실패했습니다.");
        Member user = LoginContext.getCurrentUser();
        Assertions.assertNotNull(user, "로그인한 사용자 정보를 가져올 수 없습니다.");
        Assertions.assertEquals(validId, user.getId());
    }

    @Test
    public void testLogin_Fail() {
        // given
        String wrongId = "wronguser";
        String wrongPw = "wrongpass";

        // when
        boolean result = LoginContext.login(wrongId, wrongPw);

        // then
        Assertions.assertFalse(result, "유효하지 않은 계정으로 로그인에 성공하면 안 됩니다.");
        Assertions.assertNull(LoginContext.getCurrentUser(), "잘못된 로그인 후 사용자 정보가 남아 있으면 안 됩니다.");
    }
}
