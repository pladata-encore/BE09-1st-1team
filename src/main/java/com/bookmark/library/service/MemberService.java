package com.bookmark.library.service;

import com.bookmark.library.dao.MemberDAO;
import com.bookmark.library.model.Member;

import java.time.LocalDate;

public class MemberService {
    private final MemberDAO memberDAO;

    public MemberService(MemberDAO memberDAO) {
        this.memberDAO = memberDAO;
    }

    public Member getUserInfo(String memberId, String password) {
        return memberDAO.getUserInfo(memberId, password);  // ✅ Member 객체 반환
    }

    public boolean saveUser(String member_id, String password, String username, LocalDate birth_date, String phone_number, String email) {
        return memberDAO.insertUser(member_id, password, username, birth_date, phone_number, email);
    }

    public boolean isDuplicateId(String member_id) {
        return memberDAO.isDuplicateId(member_id);
    }

    public boolean updateUser(String memberId, String password, String username, String phoneNumber, String email) {
        return memberDAO.updateUserInfo(memberId, password, username, phoneNumber, email);
    }
}
