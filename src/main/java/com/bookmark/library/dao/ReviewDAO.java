package com.bookmark.library.dao;

import com.bookmark.library.model.Review;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ReviewDAO {
    private final Connection conn;

    public ReviewDAO(Connection conn) {
        this.conn = conn;
    }

    // 리뷰 추가
    public boolean insertReview(Review review) {
        String sql = "INSERT INTO reviews (isbn, member_id, content, rating, review_date) VALUES (?, ?, ?, ?, ?)";

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, review.getIsbn());
            pstmt.setString(2, review.getMemberId());
            pstmt.setString(3, review.getContent());
            pstmt.setInt(4, review.getRating());
            pstmt.setTimestamp(5, new Timestamp(review.getReviewDate().getTime()));

            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            System.out.println("리뷰 작성 중 오류 발생 : " + e.getMessage());
            return false;
        }
    }

    // ISBN으로 리뷰 조회
    public List<Review> getReviewsByIsbn(String isbn) {
        List<Review> result = new ArrayList<>();
        String sql = "SELECT * FROM reviews WHERE isbn = ?";

        try(PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, isbn);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                Review review = new Review();
                review.setId(rs.getInt("review_id"));
                review.setMemberId(rs.getString("member_id"));
                review.setIsbn(rs.getString("isbn"));
                review.setContent(rs.getString("content"));
                review.setRating(rs.getInt("rating"));
                review.setReviewDate(rs.getTimestamp("review_date"));
                result.add(review);
            }
        } catch (SQLException e) {
            System.out.println("리뷰 조회 중 오류 발생 : " + e.getMessage());
        }
        return result;
    }

}
