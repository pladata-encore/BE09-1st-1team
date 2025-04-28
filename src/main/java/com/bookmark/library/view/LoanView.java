package com.bookmark.library.view;

import com.bookmark.library.auth.LoginContext;
import com.bookmark.library.exception.LoanFailureException;
import com.bookmark.library.exception.ReturnToHomeException;
import com.bookmark.library.model.Book;
import com.bookmark.library.common.LoanFailureReason;
import com.bookmark.library.model.LoanInfo;
import com.bookmark.library.model.Member;
import com.bookmark.library.service.LoanService;
import com.bookmark.library.service.Services;
import com.bookmark.library.util.IO;

/**
 * 도서 대출 페이지 뷰
 */
public class LoanView {
    private final LoanService loanService = Services.resolve(LoanService.class);

    public void showLoanPage(Book book) {
        Member member = LoginContext.getCurrentUser();
        if (member == null) {
            loginRequired();
            return;
        }

        var reason = loanService.canLoan(member, book);
        if (reason != null) {
            showNotAvailable(reason);
            return;
        }

        LoanInfo info = loanService.getLoanInfo(member);
        System.out.println();
        System.out.println("반납 예정일: " + info.dueDate());
        System.out.println("현재 " + info.remainingLoanCount() + "권까지 더 대출하실 수 있습니다.");
        System.out.println();

        if (IO.confirm("[" + book.title() + "] 대출하시겠습니까?")) {
            try {
                loanService.loanBook(member, book, info.loanDate(), info.dueDate());
                System.out.println();
                System.out.print("대출이 완료되었습니다. (확인: ENTER)");
                IO.scanner.nextLine();
            } catch (LoanFailureException e) {
                showNotAvailable(e.getReason());
            }
        }
    }

    private void loginRequired() {
        System.out.print("""
                로그인이 필요합니다.
                1. 홈으로
                0. 뒤로
                """);
        if (IO.selectMenu(1) == 1) {
            throw new ReturnToHomeException();
        }
    }

    private void showNotAvailable(LoanFailureReason reason) {
        System.out.println("대출이 불가능합니다.");
        System.out.println(reason.getMessage());
        IO.pressEnterToProceed();
    }
}
