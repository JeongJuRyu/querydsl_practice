package com.study.querydsl.repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.study.querydsl.dto.MemberSearchCondition;
import com.study.querydsl.dto.MemberTeamDto;
import com.study.querydsl.dto.QMemberTeamDto;
import com.study.querydsl.entity.Member;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Optional;

import static com.study.querydsl.entity.QMember.member;
import static com.study.querydsl.entity.QTeam.team;
import static org.springframework.util.StringUtils.*;

@Repository
@RequiredArgsConstructor
public class MemberJpaRepository {

    private final JPAQueryFactory queryFactory;
    private final EntityManager em;
    public void save(Member member){
        em.persist(member);
    }

    public Optional<Member> findById(Long id){
        Member findMember = em.find(Member.class, id);
        return Optional.ofNullable(findMember);
    }

    public List<MemberTeamDto> searchByBuilder(MemberSearchCondition cond){
        BooleanBuilder builder = new BooleanBuilder();
        if(hasText(cond.getUsername())){
            builder.and(member.username.eq(cond.getUsername()));
        }
        if(hasText(cond.getTeamName())){
            builder.and(team.name.eq(cond.getTeamName()));
        }
        if(cond.getAgeGoe() != null){
            builder.and(member.age.goe(cond.getAgeGoe()));
        }
        if(cond.getAgeGoe() != null){
            builder.and(member.age.loe(cond.getAgeLoe()));
        }
        return queryFactory
                .select(new QMemberTeamDto(
                        member.id.as("memberId"),
                        member.username,
                        member.age,
                        team.id.as("teamId"),
                        team.name.as("teamName")))
                .from(member)
                .leftJoin(member.team, team)
                .where(builder)
                .fetch();
    }
    public List<MemberTeamDto> search(MemberSearchCondition condition){
        return queryFactory
                .select(new QMemberTeamDto(
                        member.id.as("memberId"),
                        member.username,
                        member.age,
                        team.id.as("teamId"),
                        team.name.as("teamName")
                ))
                .from(member)
                .leftJoin(member.team, team)
                .where(
                        usernameEq(condition.getUsername()),
                        teamNameEq(condition.getTeamName()),
                        ageGoe(condition.getAgeGoe()),
                        ageLoe(condition.getAgeLoe())
//                        ageBetween( condition.getAgeLoe(), condition.getAgeGoe())
                )
                .fetch();
    }
    private BooleanExpression ageBetween(int ageLoe, int ageGoe){
        return ageGoe(ageLoe).and(ageGoe(ageGoe));
    }
    private BooleanExpression usernameEq(String username) {
        return !hasText(username) ? null : member.username.eq(username);
    }
    private BooleanExpression teamNameEq(String teamName) {
        return !hasText(teamName) ? null : team.name.eq(teamName);
    }
    private BooleanExpression ageGoe(Integer ageGoe) {
        return ageGoe != null ? member.age.goe(ageGoe) : null;
    }
    private BooleanExpression ageLoe(Integer ageLoe) {
        return ageLoe != null ? member.age.loe(ageLoe) : null;
    }

}
