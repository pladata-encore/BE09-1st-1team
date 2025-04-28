package com.bookmark.library.exception;

import com.bookmark.library.common.LoanFailureReason;

public class LoanFailureException extends RuntimeException {
    private final LoanFailureReason reason;

    public LoanFailureException(LoanFailureReason reason) {
        super(reason.getMessage());
        this.reason = reason;
    }

    public LoanFailureReason getReason() { return reason; }
}
