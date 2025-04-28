package com.bookmark.library.model;

import java.time.LocalDate;

public record Loan(
        int id,
        String memberId,
        Book book,
        LocalDate loanDate,
        LocalDate dueDate,
        LocalDate returnDate
) {
}
