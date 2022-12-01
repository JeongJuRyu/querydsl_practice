package com.study.querydsl;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.study.querydsl.entity.Hello;
import com.study.querydsl.entity.QHello;
import jakarta.persistence.EntityManager;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;


@SpringBootTest
@Transactional
class QuerydslApplicationTests {

    @Autowired
    EntityManager em;
    @Test
    void contextLoads() {

        JPAQueryFactory jpaQueryFactory = new JPAQueryFactory(em);
        Hello hello = new Hello();
        QHello hello1 = QHello.hello;
        em.persist(hello);
        Hello hello2 = jpaQueryFactory.selectFrom(hello1).fetchOne();
        Assertions.assertThat(hello2).isEqualTo(hello);
    }

}
