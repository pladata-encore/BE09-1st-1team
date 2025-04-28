package com.bookmark.library.dao;

import com.bookmark.library.model.Member;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDate;

public class MemberDAO {
    private final Connection conn;

    public MemberDAO(Connection conn) {
        this.conn = conn;
    }

    // 유저 정보 조회
    public Member getUserInfo(String memberId, String password) {
        String sql = "SELECT * FROM members WHERE member_id = ? AND password = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, memberId);
            stmt.setString(2, password);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new Member(
                            rs.getString("member_id"),
                            rs.getString("username"),
                            rs.getDate("birth_date").toLocalDate(),
                            rs.getString("phone_number"),
                            rs.getString("email")
                    );
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    // 회원 가입 저장
    public boolean insertUser(String memberId, String password, String username,
                              LocalDate birthDate, String phoneNumber, String email) {
        String sql = """
            INSERT INTO members (member_id, password, username, birth_date, phone_number, email)
            VALUES (?, ?, ?, ?, ?, ?)
            """;

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, memberId);
            stmt.setString(2, password);
            stmt.setString(3, username);
            stmt.setDate(4, Date.valueOf(birthDate));
            stmt.setString(5, phoneNumber);
            stmt.setString(6, email);

            int rows = stmt.executeUpdate();
            return rows > 0;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // 아이디 중복 체크
    public boolean isDuplicateId(String memberId) {
        String sql = "SELECT COUNT(*) FROM members WHERE member_id = ?";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, memberId);
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next() && rs.getInt(1) > 0;
            }

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // 회원정보 수정
    public boolean updateUserInfo(String memberId, String password, String username, String phoneNumber, String email) {
        String sql = """
        UPDATE members
        SET password = ?, username = ?, phone_number = ?, email = ?
        WHERE member_id = ?
        """;

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, password);
            stmt.setString(2, username);
            stmt.setString(3, phoneNumber);
            stmt.setString(4, email);
            stmt.setString(5, memberId);

            int rows = stmt.executeUpdate();
            return rows > 0;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
