package com.bookmark.library.auth;

import com.bookmark.library.model.Member;
import com.bookmark.library.service.MemberService;
import com.bookmark.library.service.Services;

public class LoginContext {
    private static LoginContext context;

    private final String memberId;
    private final String password;

    private LoginContext(String memberId, String password) {
        this.memberId = memberId;
        this.password = password;
    }

    // 로그인 처리
    public static Member login(String memberId, String password) {
        var service = Services.resolve(MemberService.class);
        var member = service.getUserInfo(memberId, password);
        if (member != null) {
            context = new LoginContext(memberId, password);
            return member;
        }
        return null;
    }

    // 로그아웃 처리
    public static void logout() {
        context = null;
    }

    // 현재 로그인한 사용자 정보 가져오기
    public static Member getCurrentUser() {
        if (context == null) return null;

        var service = Services.resolve(MemberService.class);
        var member = service.getUserInfo(context.memberId, context.password);
        if (member == null) {
            // 로그인 세션 정보가 유효하지 않으면 로그아웃 처리
            logout();
            return null;
        }

        return member;
    }
}
