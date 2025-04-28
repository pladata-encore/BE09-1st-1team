package com.bookmark.library.view.userinfoview;

import com.bookmark.library.auth.LoginContext;
import com.bookmark.library.exception.ReturnToHomeException;
import com.bookmark.library.model.Book;
import com.bookmark.library.model.Loan;
import com.bookmark.library.model.Member;
import com.bookmark.library.Main;
import com.bookmark.library.service.LoanService;
import com.bookmark.library.service.Services;
import com.bookmark.library.util.IO;
import com.bookmark.library.dao.LoanDAO;

import java.sql.Connection;
import java.time.LocalDate;
import java.util.List;

public class UserInfoPage {
    private final LoanService loanService = Services.resolve(LoanService.class);
    private List<Loan> loans;

    public void run() {
        while (true) {
            Member user = LoginContext.getCurrentUser();
            loans = loanService.getCurrentLoans(user.id());

            System.out.println();
            System.out.println("==== 마이페이지 ====");
            System.out.println("아이디: " + user.id());
            System.out.println("이름: " + user.username());
            System.out.println("생년월일: " + user.birthDate());
            System.out.println("전화번호: " + user.phoneNumber());
            System.out.println("이메일: " + user.email());

            showLoans();

            System.out.println();
            System.out.print("""
                1. 개인정보 수정
                2. 반납
                0. 뒤로가기
                """);

            int choice = IO.selectMenu(2);
            switch (choice) {
                case 1 -> new InfoEditPage().run(); // 개인정보 수정 화면 이동
                case 2 -> showReturn();  // 반납 메뉴 이동
                case 0 -> { return; } // 뒤로가기
            }
        }
    }

    private void showLoans() {
        try {
            System.out.println("\n📚 대출 중인 도서 목록:");
            if (loans.isEmpty()) {
                System.out.println(" - 현재 대출 중인 도서가 없습니다.");
            } else {
                for (int i = 0; i < loans.size(); i++) {
                    printLoan(loans.get(i), i + 1);
                }
            }
        } catch (RuntimeException e) {
            System.out.println("⚠️ 대출 목록을 불러오는 중 오류가 발생했습니다.");
            e.printStackTrace();
        }
    }

    private void showReturn() {
        if (loans.isEmpty()) {
            System.out.println("반납할 도서가 없습니다.");
            return;
        }

        int choice = IO.selectMenu("반납할 도서 (위에서 선택): ", loans.size());
        Loan loan = loans.get(choice - 1);
        Book book = loan.book();

        if (IO.confirm("[" + book.title() + "] 반납하시겠습니까?")) {
            loanService.returnBook(loan.id());
            System.out.println("반납되었습니다. 이용해주셔서 감사합니다.");
            IO.pressEnterToProceed();
        }
    }

    private void printLoan(Loan loan, int no) {
        Book book = loan.book();
        boolean overdue = LocalDate.now().isAfter(loan.dueDate());

        System.out.print(" - [" + no + "] " + book.title());

        if (overdue) System.out.print(IO.RED);
        System.out.print(" [기한: " + loan.dueDate() + "]");
        if (overdue) System.out.print(IO.RESET);

        System.out.println();
    }
}
