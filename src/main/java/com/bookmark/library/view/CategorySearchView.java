package com.bookmark.library.view;

import com.bookmark.library.service.SearchService;
import com.bookmark.library.service.Services;
import com.bookmark.library.util.IO;

public class CategorySearchView {
    private final SearchService searchService = Services.resolve(SearchService.class);

    public void showCategoryView() {
        while (true) {
            System.out.println("\n==== [도서 카테고리 선택] ====");

            var categories = searchService.getCategories();
            for (int i = 0; i < categories.size(); i++) {
                var category = categories.get(i);
                System.out.println("[" + (i+1) + "] " + category.name());
            }
            System.out.println("0. 뒤로");

            int choice = IO.selectMenu(categories.size());
            if (choice == 0) return;

            var category = categories.get(choice - 1);
            var results = searchService.getBooksByCategory(category.id());
            var resultView = new SearchResultView(results);
            resultView.showSearchResults("돌아가기");
            if (results.isEmpty()) {
                IO.pressEnterToProceed();
            }
        }
    }
}
