package com.study.querydsl.entity;

import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

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


}
