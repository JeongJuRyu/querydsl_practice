package com.study.querydsl.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;



@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Hello {
    @Id  @GeneratedValue
    private Long id;
}
