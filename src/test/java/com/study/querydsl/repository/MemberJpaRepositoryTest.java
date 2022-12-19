package com.study.querydsl.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.study.querydsl.entity.Member;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.study.querydsl.entity.QMember.member;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class MemberJpaRepositoryTest {
    @Autowired
    MemberJpaRepository memberJpaRepository;

    @Autowired
    JPAQueryFactory queryFactory;
    public List<Member> findAll_Querydsl(){
        return queryFactory
                .selectFrom(member)
                .fetch();
    }

    public List<Member> findByUsername_QueryDsl(String username){
        return queryFactory
                .selectFrom(member)
                .where(member.username.eq(username))
                .fetch();
    }

    @Test
    public void basicTest(){
        Member member1 = new Member("member1", 10);
        memberJpaRepository.save(member1);

        List<Member> findMember = findByUsername_QueryDsl(member1.getUsername());
        assertThat(findMember.size()).isEqualTo(1);
    }
}