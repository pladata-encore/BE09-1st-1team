package com.bookmark.library.view;

import com.bookmark.library.model.Book;
import com.bookmark.library.model.Review;
import com.bookmark.library.service.LoanService;
import com.bookmark.library.service.ReviewService;
import com.bookmark.library.service.Services;
import com.bookmark.library.util.IO;

import java.util.List;

public class ShowBookDetailView {
    private final ReviewService reviewService = Services.resolve(ReviewService.class);
    private final LoanService loanService = Services.resolve(LoanService.class);

    /***
     *  BOOK-005: 도서 상세 정보 표시
     * @param book
     */
    public void showBookDetail(Book book) {
        boolean continueLoop = true;
        while (continueLoop) {
            System.out.println("\n==== [도서 상세 정보] ====\n");
            showBookInfo(book);
            System.out.println();
            showBookReviews(book);

            System.out.println();
            System.out.println("1. 대출하기");
            System.out.println("2. 리뷰 작성하기");
            System.out.println("0. 뒤로");

            // 사용자 입력 처리 - 메뉴 최대값은 2
            int choice = IO.selectMenu(2);
            switch (choice) {
                case 0 -> continueLoop = false;
                case 1 -> {
                    // 대출하기
                    var loanView = new LoanView();
                    loanView.showLoanPage(book);
                }
                case 2 -> {
                    // 리뷰 작성 페이지로 이동 코드
                    new WriteReviewView().writeReview(book);
                    // 리뷰 작성 후에는 다음 반복에서 새로운 리뷰 목록을 불러옴
                }
            }
        }
    }

    private void showBookReviews(Book book) {
        System.out.println("💬 리뷰");
        List<Review> reviews = reviewService.getReviewsByISbn(book.isbn());
        if (reviews.isEmpty()) {
            System.out.println("아직 등록된 리뷰가 없습니다.");
        } else {
            for (Review review : reviews) {
                System.out.println("사용자 ID: " + review.getMemberId());
                System.out.println(" \"" + review.getContent() + "\"");
                System.out.println(" 별점 : " + "★".repeat(Math.max(0,review.getRating())) + "☆".repeat(5 - review.getRating()));
                System.out.println();
            }
        }
    }

    private void showBookInfo(Book book) {
        System.out.println("📘 도서명: " + book.title());
        System.out.println(" ✍ 저자: " + book.author());
        System.out.println("🏢 출판사: " + book.publisher());
        System.out.println("📅 출간일: " + book.publishDate());

        System.out.print("📦 재고 현황: ");
        int loanCount = loanService.getLoanCountByBook(book.isbn());
        int remaining = Math.max(0, book.totalStock() - loanCount);
        if (remaining > 0) {
            System.out.println(remaining + "/" + book.totalStock() + " (대출 가능)");
        } else {
            System.out.println(IO.RED + remaining + "/" + book.totalStock() + " (대출 불가)" + IO.RESET);
        }

        System.out.println("📖 책 소개:\n" + book.introduction());
    }
}
