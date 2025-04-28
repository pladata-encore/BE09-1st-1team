package com.bookmark.library.view;

import com.bookmark.library.service.SearchService;
import com.bookmark.library.service.Services;
import com.bookmark.library.util.IO;

import java.util.LinkedHashSet;
import java.util.Scanner;
import java.util.Set;

public class KeywordSearchView {
    private final SearchService searchService = Services.resolve(SearchService.class);

    public void runKeywordSearch() {
        System.out.println("""
                
                ==== [통합 검색] ====
                책의 제목이나 저자의 이름으로 검색할 수 있습니다.
                검색어를 입력하지 않고 ENTER를 눌러 나갈 수 있습니다.""");

        while (true) {
            String keyword;
            System.out.print("검색: ");
            keyword = IO.scanner.nextLine();
            if (keyword.isBlank()) return;

            var results = searchService.search(keyword);
            var resultView = new SearchResultView(results);
            resultView.showSearchResults("다시 검색");
        }
    }
}
