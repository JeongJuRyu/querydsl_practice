package com.study.querydsl.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.study.querydsl.entity.Member;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
public class MemberRepositoryTest{
    @Autowired
    MemberRespository memberRepository;

    @PersistenceContext
    EntityManager em;

    @Autowired
    JPAQueryFactory queryFactory;

    @Test
    public void basicTest(){
        Member member1 = new Member("member1", 10);
        memberRepository.save(member1);

        List<Member> findMember = memberRepository.findByUsername(member1.getUsername());
        assertThat(findMember.size()).isEqualTo(1);
    }
}

