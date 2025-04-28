package com.bookmark.library.model;

import java.time.LocalDate;
import java.time.Period;

public record Member(
        String id,              // 사용자 id
        String username,        // 사용자 이름
        LocalDate birthDate,    // 생년월일
        String phoneNumber,     // 전화번호
        String email           // 이메일
) {
    public int getAge() {
        return Period.between(birthDate, LocalDate.now()).getYears();
    }
}
