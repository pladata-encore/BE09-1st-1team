package com.bookmark.library.view.userinfoview;

import com.bookmark.library.auth.LoginContext;
import com.bookmark.library.model.Member;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class InfoEditPageTests {

    @Test
    public void testUpdateUser_Success() {
        // given
        String memberId = "12";   // DB에 실제 존재하는 ID로 바꾸세요
        String password = "12";       // 해당 ID의 올바른 비밀번호

        // 로그인 먼저 수행
        boolean loginResult = LoginContext.login(memberId, password);
        Assertions.assertTrue(loginResult, "로그인 실패 - 테스트 불가");

        Member user = LoginContext.getCurrentUser();
        Assertions.assertNotNull(user, "로그인된 사용자 정보가 null입니다.");

        // when: 개인정보 수정
        boolean result = UserEditService.updateUser(
                user.getId(),
                "newpass123",             // 새 비밀번호
                "테스트수정",              // 새 사용자명
                "010-9999-9999",         // 새 전화번호
                "updated@example.com"    // 새 이메일
        );

        // then: 수정 성공 여부
        Assertions.assertTrue(result, "개인정보 수정에 실패했습니다.");
    }

    @Test
    public void testUpdateUser_Fail_InvalidId() {
        // 존재하지 않는 ID로 실행
        boolean result = UserEditService.updateUser(
                "no_such_user",
                "irrelevant",
                "FakeName",
                "000-0000-0000",
                "fail@example.com"
        );

        Assertions.assertFalse(result, "존재하지 않는 사용자 ID로 수정에 성공하면 안 됩니다.");
    }
}
