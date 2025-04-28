package com.bookmark.library.dao;

import com.bookmark.library.model.Book;
import com.bookmark.library.model.Loan;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class LoanDAO {
    private final Connection conn;
    
    public LoanDAO(Connection conn) {
        this.conn = conn;
    }

    public boolean hasOverdueLoans(String memberId) {
        String sql = "SELECT COUNT(*) FROM loans WHERE member_id = ? AND due_date < CURRENT_DATE AND return_date IS NULL";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, memberId);
            var rs = pstmt.executeQuery();
            return rs.next() && rs.getInt(1) > 0;
        } catch (SQLException e) {
            throw new RuntimeException("Failed to check overdue loans", e);
        }
    }

    public int getLoanCountByMember(String memberId) {
        String sql = "SELECT COUNT(*) FROM loans WHERE member_id = ? AND return_date IS NULL";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, memberId);
            var rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
            return 0;
        } catch (SQLException e) {
            throw new RuntimeException("Failed to get loan count", e);
        }
    }
    
    public int getLoanCountByBook(String isbn) {
        String sql = "SELECT COUNT(*) FROM loans WHERE isbn = ? AND return_date IS NULL";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, isbn);
            var rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
            return 0;
        } catch (SQLException e) {
            throw new RuntimeException("Failed to get loan count for book", e);
        }
    }

    public void createLoan(String memberId, String isbn, LocalDate loanDate, LocalDate dueDate) {
        String sql = "INSERT INTO loans (member_id, isbn, loan_date, due_date) VALUES (?, ?, ?, ?)";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, memberId);
            pstmt.setString(2, isbn);
            pstmt.setObject(3, loanDate);
            pstmt.setObject(4, dueDate);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Failed to create loan record", e);
        }
    }

    public List<Loan> getCurrentLoans(String memberId) {
        String sql = """
                SELECT loan_id, member_id, loan_date, due_date, return_date, books.*
                FROM loans
                JOIN books ON loans.isbn = books.isbn
                WHERE member_id = ? AND return_date IS NULL
                """;
        List<Loan> loans = new ArrayList<>();
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, memberId);
            var rs = pstmt.executeQuery();
            while (rs.next()) {
                loans.add(new Loan(
                        rs.getInt("loan_id"),
                        rs.getString("member_id"),
                        BookDAO.createBook(rs),
                        rs.getObject("loan_date", LocalDate.class),
                        rs.getObject("due_date", LocalDate.class),
                        rs.getObject("return_date", LocalDate.class)
                ));
            }
            return loans;
        } catch (SQLException e) {
            throw new RuntimeException("Failed to get current loans", e);
        }
    }
    
    public void returnBook(int loanId) {
        String sql = "UPDATE loans SET return_date = CURRENT_DATE WHERE loan_id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, loanId);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Failed to return book", e);
        }
    }
}
