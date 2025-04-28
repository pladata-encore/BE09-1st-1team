package com.bookmark.library;

import com.bookmark.library.dao.*;
import com.bookmark.library.service.*;
import com.bookmark.library.view.HomeView;

public class Main {
    public static void main(String[] args) {
        var conn = DB.estabilishConnection();
        Services.register(new LoanService(new LoanDAO(conn)));
        Services.register(new MemberService(new MemberDAO(conn)));
        Services.register(new ReviewService(new ReviewDAO(conn)));
        Services.register(new SearchService(new BookDAO(conn), new CategoryDAO(conn)));

        var homeView = new HomeView();
        homeView.showHome();
    }
}
