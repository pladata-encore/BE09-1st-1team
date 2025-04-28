package com.bookmark.library.service;

import com.bookmark.library.common.LoanFailureReason;
import com.bookmark.library.dao.LoanDAO;
import com.bookmark.library.model.Book;
import com.bookmark.library.model.Member;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.*;

class LoanServiceTest {

    @Mock
    private LoanDAO loanDAO;

    private LoanService loanService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        loanService = new LoanService(loanDAO);
    }

    @Test
    void canLoan_whenMemberIsNull_shouldReturnMemberNotFound() {
        Book book = mock(Book.class);

        LoanFailureReason result = loanService.canLoan(null, book);

        assertEquals(LoanFailureReason.MEMBER_NOT_FOUND, result);
    }

    @Test
    void canLoan_whenBookIsNull_shouldReturnBookNotFound() {
        Member member = mock(Member.class);

        LoanFailureReason result = loanService.canLoan(member, null);

        assertEquals(LoanFailureReason.BOOK_NOT_FOUND, result);
    }

    @Test
    void canLoan_whenBookNotAvailable_shouldReturnBookNotAvailable() {
        Member member = mock(Member.class);
        Book book = mock(Book.class);
        when(book.isAvailable()).thenReturn(false);

        LoanFailureReason result = loanService.canLoan(member, book);

        assertEquals(LoanFailureReason.BOOK_NOT_AVAILABLE, result);
    }

    @Test
    void canLoan_whenMemberHasOverdueLoans_shouldReturnMemberHasOverdue() {
        Member member = mock(Member.class);
        Book book = mock(Book.class);
        when(book.isAvailable()).thenReturn(true);
        when(member.getId()).thenReturn("member-id");
        when(loanDAO.hasOverdueLoans("member-id")).thenReturn(true);

        LoanFailureReason result = loanService.canLoan(member, book);

        assertEquals(LoanFailureReason.MEMBER_HAS_OVERDUE, result);
    }

    @Test
    void canLoan_whenMemberReachedLoanLimit_shouldReturnMemberReachedLimit() {
        Member member = mock(Member.class);
        Book book = mock(Book.class);
        when(book.isAvailable()).thenReturn(true);
        when(member.getId()).thenReturn("member-id");
        when(loanDAO.getLoanCountByMember("member-id")).thenReturn(LoanService.MAX_LOAN_COUNT);

        LoanFailureReason result = loanService.canLoan(member, book);

        assertEquals(LoanFailureReason.MEMBER_REACHED_LIMIT, result);
    }

    @Test
    void canLoan_whenMemberAgeRestricted_shouldReturnMemberAgeRestricted() {
        Member member = mock(Member.class);
        Book book = mock(Book.class);
        when(member.getAge()).thenReturn(15);
        when(book.getAgeLimit()).thenReturn(18);
        when(book.isAvailable()).thenReturn(true);

        LoanFailureReason result = loanService.canLoan(member, book);

        assertEquals(LoanFailureReason.MEMBER_AGE_RESTRICTED, result);
    }

    @Test
    void canLoan_whenAllConditionsMet_shouldReturnNull() {
        Member member = mock(Member.class);
        Book book = mock(Book.class);
        when(member.getId()).thenReturn("member-id");
        when(member.getAge()).thenReturn(25);
        when(loanDAO.hasOverdueLoans("member-id")).thenReturn(false);
        when(loanDAO.getLoanCountByMember("member-id")).thenReturn(0);
        when(book.isAvailable()).thenReturn(true);
        when(book.getAgeLimit()).thenReturn(18);

        LoanFailureReason result = loanService.canLoan(member, book);

        assertNull(result);
    }
}