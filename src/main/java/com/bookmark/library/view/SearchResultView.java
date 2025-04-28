package com.bookmark.library.view;

import com.bookmark.library.model.Book;
import com.bookmark.library.util.IO;

import java.util.List;

public class SearchResultView {
    public static List<Book> searchResults;

    public SearchResultView(List<Book> results) {
        searchResults = results;
    }

    public void showSearchResults(String returnPrompt) {
        System.out.println("\n==== [검색 결과] ====");
        if (searchResults == null || searchResults.isEmpty()) {
            System.out.println("검색 결과가 없습니다.");
            return;
        }

        for (int i = 0; i < searchResults.size(); i++) {
            Book book = searchResults.get(i);
            System.out.println("[" + (i + 1) + "] " +
                               book.title() + " | " +
                               book.author() + " | " +
                               book.publisher() + " | " +
                               book.publishDate().getYear());
        }
        System.out.println("0. " + returnPrompt);

        int choice = IO.selectMenu(searchResults.size());
        if (choice == 0) return;

        Book book = searchResults.get(choice - 1);
        var detailView = new ShowBookDetailView();
        detailView.showBookDetail(book);
    }
}
