package com.study.querydsl.entity;

import com.querydsl.core.QueryResults;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.study.querydsl.entity.QMember.*;
import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@Transactional
public class QuerydslBasicTest {
    @PersistenceContext
    EntityManager em;
    JPAQueryFactory queryFactory;

    @BeforeEach
    public void setup() {
        queryFactory = new JPAQueryFactory(em);
        Team teamA = new Team("teamA");
        Team teamB = new Team("teamB");

        em.persist(teamA);
        em.persist(teamB);

        Member member1 = new Member("member1", 10, teamA);
        Member member2 = new Member("member2", 20, teamA);
        Member member3 = new Member("member3", 30, teamB);
        Member member4 = new Member("member4", 40, teamB);

        teamA.getMembers().add(member1);
        teamA.getMembers().add(member2);
        teamB.getMembers().add(member3);
        teamB.getMembers().add(member4);
        em.persist(member1);
        em.persist(member2);
        em.persist(member3);
        em.persist(member4);
    }
     @Test
     public void startJPQL(){
         Member member = em.createQuery("select m from Member m where m.username = :username", Member.class)
                 .setParameter("username", "member1")
                 .getSingleResult();

         assertThat(member.getUsername()).isEqualTo("member1");
     }

     @Test
     public void startQuerydsl(){
         Member findMember = queryFactory
                 .select(member)
                 .from(member)
                 .where(member.username.eq("member1"))
                 .fetchOne();
         assertThat(findMember.getUsername()).isEqualTo("member1");

     }

     @Test
     public void search(){
         Member findMember = queryFactory
                 .selectFrom(member)
                 .where(member.username.eq("member1")
                         .and(member.age.eq(10)))
                 .fetchOne();
        assertThat(findMember.getUsername()).isEqualTo("member1");

     }

    @Test
    public void searchAndParam(){
        Member findMember = queryFactory
                .selectFrom(member)
                .where( // and를 사용하지 않고 더 가독성있게 보기
                        member.username.eq("member1"),
                        member.age.between(10, 30))
                .fetchOne();
        assertThat(findMember.getUsername()).isEqualTo("member1");
    }

    @Test
    public void resultFetch(){
        List<Member> fetch = queryFactory
                .selectFrom(member)
                .fetch();
        Member member1 = queryFactory
                .selectFrom(member)
                .where(member.age.eq(10))
                .fetchOne();
        int size = queryFactory
                .selectFrom(member)
                .fetch().size();
        assertThat(size).isEqualTo(4);
    }

    /**
     * 회원 정렬 순서
     * 1. 회원 나이 내림차순(desc)
     * 2. 회원 이름 올림차순(asc)
     * 단, 2에서 회원 이름이 없으면 마지막에 출력(nulls last)
     */
    @Test
    public void sort(){
        em.persist(new Member(null, 100));
        em.persist(new Member("member5", 100));
        em.persist(new Member("member6", 100));

        List<Member> result = queryFactory
                .selectFrom(member)
                .where(member.age.eq(100))
                .orderBy(member.age.desc(),
                        member.username.asc().nullsLast())
                .fetch();
        Member member5 = result.get(0);
        Member member6 = result.get(1);
        Member memberNull = result.get(2);
        assertThat(member5.getUsername()).isEqualTo("member5");
        assertThat(member6.getUsername()).isEqualTo("member6");
        assertThat(memberNull.getUsername()).isNull();
    }

    @Test
    public void paging1(){
        List<Member> members = queryFactory
                .selectFrom(member)
                .orderBy(member.username.desc())
                .offset(1)
                .limit(2)
                .fetch();
        assertThat(members.size()).isEqualTo(2);
    }
}
