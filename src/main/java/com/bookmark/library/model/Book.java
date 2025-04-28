package com.bookmark.library.model;

import java.time.LocalDate;

public record Book(
        String isbn,            // 도서번호
        String title,           // 도서명
        int categoryId,         // 카테고리 코드
        String author,          // 저자
        String publisher,       // 출판사
        LocalDate publishDate,  // 출판일
        int totalStock,         // 재고수량
        String introduction,    // 한줄소개
        int ageLimit            // 연령제한
) {
}
