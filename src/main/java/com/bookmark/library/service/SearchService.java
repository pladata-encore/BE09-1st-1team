package com.bookmark.library.service;

import com.bookmark.library.dao.BookDAO;
import com.bookmark.library.dao.CategoryDAO;
import com.bookmark.library.model.Book;
import com.bookmark.library.model.Category;

import java.util.List;

public class SearchService {
    private final BookDAO bookDAO;
    private final CategoryDAO categoryDAO;

    public SearchService(BookDAO bookDAO, CategoryDAO categoryDAO) {
        this.bookDAO = bookDAO;
        this.categoryDAO = categoryDAO;
    }

    public List<Book> search(String keyword) {
        return bookDAO.searchBooks(keyword);
    }

    public List<Category> getCategories() {
        return categoryDAO.getCategories();
    }

    public List<Book> getBooksByCategory(int categoryId) {
        return bookDAO.getBooksByCategory(categoryId);
    }
}
