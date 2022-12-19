package com.study.querydsl.repository;

import com.study.querydsl.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MemberRespository extends JpaRepository<Member, Long> {
    //select m from member m where m.username = ?
    List<Member> findByUsername(String username);
}
