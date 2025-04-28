package com.bookmark.library.model;

import java.time.LocalDate;

public record LoanInfo(
        LocalDate loanDate,
        LocalDate dueDate,
        int currentLoanCount,
        int remainingLoanCount
) {
}
