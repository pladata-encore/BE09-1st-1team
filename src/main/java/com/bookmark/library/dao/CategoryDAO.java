package com.bookmark.library.dao;

import com.bookmark.library.model.Category;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CategoryDAO {
    private final Connection conn;

    public CategoryDAO(Connection conn) {
        this.conn = conn;
    }
    
    public List<Category> getCategories() {
        List<Category> categories = new ArrayList<>();
        String sql = "SELECT category_id, category_name FROM categories";

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                categories.add(new Category(
                        rs.getInt("category_id"),
                        rs.getString("category_name")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return categories;
    }

    public String getCategoryNameById(int categoryId) {
        String sql = "SELECT category_name FROM categories WHERE category_id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, categoryId);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getString("category_name");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
