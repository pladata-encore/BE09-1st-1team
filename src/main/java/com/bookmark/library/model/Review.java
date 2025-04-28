package com.bookmark.library.model;

import java.util.Date;

public class Review {
    private int id;                 // 리뷰 자체 id
    private String memberId;        // 작성자 id
    private String isbn;            // 도서번호
    private int rating;             // 별점
    private Date reviewDate;        // 리뷰 작성 날짜
    private String content;         // 리뷰 내용

    public Review() {}

    public Review(int id, String memberId, String isbn, int rating, Date reviewDate, String content) {
        this.id = id;
        this.memberId = memberId;
        this.isbn = isbn;
        this.rating = rating;
        this.reviewDate = reviewDate;
        this.content = content;
    }

    // getter
    public int getId() {
        return id;
    }

    public String getMemberId() {
        return memberId;
    }

    public String getIsbn() {
        return isbn;
    }

    public int getRating() {
        return rating;
    }

    public Date getReviewDate() {
        return reviewDate;
    }

    public String getContent() {
        return content;
    }

    // setter
    public void setId(int id) {
        this.id = id;
    }

    public void setMemberId(String memberId) {
        this.memberId = memberId;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public void setReviewDate(Date reviewDate) {
        this.reviewDate = reviewDate;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return "Review{" +
                "id=" + id +
                ", memberId='" + memberId + '\'' +
                ", isbn='" + isbn + '\'' +
                ", rating=" + rating +
                ", reviewDate=" + reviewDate +
                ", content='" + content + '\'' +
                '}';
    }
}
