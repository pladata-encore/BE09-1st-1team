package com.bookmark.library.view;

import com.bookmark.library.Main;
import com.bookmark.library.auth.LoginContext;
import com.bookmark.library.model.Book;
import com.bookmark.library.model.Member;
import com.bookmark.library.model.Review;
import com.bookmark.library.service.ReviewService;
import com.bookmark.library.service.Services;
import com.bookmark.library.util.IO;

import java.util.ArrayList;
import java.util.Date;

import static com.bookmark.library.util.IO.pressEnterToProceed;

public class WriteReviewView {
    private final ReviewService reviewService = Services.resolve(ReviewService.class);

    /***
     * BOOK-008: 리뷰 작성 페이지
     * @param book 리뷰를 작성할 도서 객체
     */
    public void writeReview(Book book) {
        Member user = LoginContext.getCurrentUser();
        if (user != null) {
            System.out.println("로그인한 사용자 ID : " + user.id());

            System.out.println("=== [📝 리뷰 작성] ===\n");
            System.out.println("내용을 입력하세요:");
            System.out.print("> ");
            String content = IO.scanner.nextLine().trim();
            System.out.println();

            int rating = 0;
            while (true) {
                System.out.print("별점 선택 (1 ~ 5): ");
                try {
                    rating = IO.scanner.nextInt();
                    IO.scanner.nextLine(); // 버퍼 비우기

                    if (rating >= 1 && rating <= 5) break;
                    System.out.println("⚠ 1에서 5 사이의 숫자를 입력해주세요.");
                } catch (Exception e) {
                    System.out.println("⚠ 유효한 숫자를 입력해주세요.");
                    IO.scanner.nextLine(); // 잘못된 입력 제거
                }
            }

            Review review = new Review();
            review.setMemberId(user.id()); // 로그인한 사용자 ID 설정
            review.setIsbn(book.isbn());
            review.setContent(content);
            review.setRating(rating);
            review.setReviewDate(new Date());

            boolean success = reviewService.writeReview(review);
            if (success) {
                System.out.println("✅ 리뷰가 등록되었습니다! 감사합니다.");
            } else {
                System.out.println("⚠ 리뷰 등록에 실패했습니다. 다시 시도해주세요.");
            }

        } else {
            System.out.println("⚠ 리뷰를 작성하려면 로그인이 필요합니다.");
        }
        pressEnterToProceed();
    }
}
