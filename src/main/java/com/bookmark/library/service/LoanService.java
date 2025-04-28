package com.bookmark.library.service;

import com.bookmark.library.dao.LoanDAO;
import com.bookmark.library.exception.LoanFailureException;
import com.bookmark.library.model.Book;
import com.bookmark.library.common.LoanFailureReason;
import com.bookmark.library.model.Loan;
import com.bookmark.library.model.LoanInfo;
import com.bookmark.library.model.Member;

import java.time.LocalDate;
import java.util.List;

public class LoanService {
    private static final int MAX_LOAN_COUNT = 3;
    private static final int LOAN_DURATION_DAYS = 7;

    private final LoanDAO loanDAO;

    public LoanService(LoanDAO loanDAO) {
        this.loanDAO = loanDAO;
    }

    public LoanInfo getLoanInfo(Member member) {
        int current = getLoanCountByMember(member.id());
        LocalDate loanDate = LocalDate.now();
        LocalDate dueDate = loanDate.plusDays(LOAN_DURATION_DAYS);
        return new LoanInfo(loanDate, dueDate, current, MAX_LOAN_COUNT - current);
    }

    /**
     * 책을 빌립니다.
     * @param member 책을 빌릴 회원
     * @param book 빌릴 책
     */
    public void loanBook(Member member, Book book, LocalDate loanDate, LocalDate dueDate) throws LoanFailureException {
        var reason = canLoan(member, book);
        if (reason != null) {
            throw new LoanFailureException(reason);
        }

        loanDAO.createLoan(member.id(), book.isbn(), loanDate, dueDate);
    }

    /**
     * 회원이 해당 도서를 대출할 수 있는지 확인합니다.
     * 대출이 불가능한 경우 그 사유를 반환하며, 가능할 경우 {@code null}을 반환합니다.
     * @param member 대출을 시도하는 회원
     * @param book   대출하려는 도서
     * @return 대출 불가 사유가 담긴 {@link LoanFailureReason}, 또는 대출 가능 시 {@code null}
     */
    public LoanFailureReason canLoan(Member member, Book book) {
        if (member == null) return LoanFailureReason.MEMBER_NOT_FOUND;
        if (book == null) return LoanFailureReason.BOOK_NOT_FOUND;

        if (getLoanCountByBook(book.isbn()) >= book.totalStock()) {
            return LoanFailureReason.BOOK_NOT_AVAILABLE;
        }

        if (loanDAO.hasOverdueLoans(member.id())) {
            return LoanFailureReason.MEMBER_HAS_OVERDUE;
        }

        if (getLoanCountByMember(member.id()) >= MAX_LOAN_COUNT) {
            return LoanFailureReason.MEMBER_REACHED_LIMIT;
        }

        if (book.ageLimit() > 0 && member.getAge() < book.ageLimit()) {
            return LoanFailureReason.MEMBER_AGE_RESTRICTED;
        }

        return null; // 대출 가능
    }

    public void returnBook(int loanId) {
        loanDAO.returnBook(loanId);
    }

    public int getLoanCountByBook(String isbn) {
        return loanDAO.getLoanCountByBook(isbn);
    }

    public int getLoanCountByMember(String memberId) {
        return loanDAO.getLoanCountByMember(memberId);
    }

    public List<Loan> getCurrentLoans(String memberId) {
        return loanDAO.getCurrentLoans(memberId);
    }
}
