package com.bookmark.library.dao;

import com.bookmark.library.model.Book;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class BookDAO {
    private final Connection conn;
    
    public BookDAO(Connection conn){
        this.conn = conn;
    }

    public List<Book> getBooksByCategory(int categoryId) {
        String sql = "SELECT * FROM books WHERE category_id = ?";
        List<Book> books = new ArrayList<>();
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, categoryId);
            try (var rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    books.add(createBook(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to get books by category", e);
        }
        return books;
    }

    public List<Book> searchBooks(String keyword) {
        String sql = "SELECT * FROM books WHERE LOWER(title) LIKE ? OR LOWER(author) LIKE ?";
        List<Book> books = new ArrayList<>();
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            String searchPattern = "%" + keyword.trim().toLowerCase() + "%";
            pstmt.setString(1, searchPattern);
            pstmt.setString(2, searchPattern);

            try (var rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    books.add(createBook(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to search books", e);
        }
        return books;
    }

    public static Book createBook(ResultSet rs) throws SQLException {
        return new Book(
                rs.getString("isbn"),
                rs.getString("title"),
                rs.getInt("category_id"),
                rs.getString("author"),
                rs.getString("publisher"),
                rs.getDate("publish_date").toLocalDate(),
                rs.getInt("stock_quantity"),
                rs.getString("introduction"),
                rs.getInt("age_limit")
        );
    }
}
