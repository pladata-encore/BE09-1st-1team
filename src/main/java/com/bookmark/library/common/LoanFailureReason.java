package com.bookmark.library.common;

public enum LoanFailureReason {
    BOOK_NOT_AVAILABLE("현재 모든 도서가 대출 중입니다."),
    MEMBER_NOT_FOUND("해당 회원이 존재하지 않습니다."),
    BOOK_NOT_FOUND("해당 도서가 존재하지 않습니다."),
    MEMBER_HAS_OVERDUE("연체 중인 대출이 있어 대출이 불가능합니다."),
    MEMBER_REACHED_LIMIT("대출 가능 권수를 초과했습니다."),
    MEMBER_AGE_RESTRICTED("회원의 연령으로 인해 대출이 제한됩니다."),
    UNKNOWN_ERROR("알 수 없는 오류로 대출이 불가능합니다.");

    private final String message;

    LoanFailureReason(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
