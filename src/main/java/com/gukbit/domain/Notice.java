package com.gukbit.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@Entity
@Table
@AllArgsConstructor
@NoArgsConstructor
public class Notice {

    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Integer bid;

    @Column(columnDefinition = "varchar(45) not null comment '등록자'")
    protected String author;

    @Column
    protected String date;

    @Column
    protected Integer view;

    @Column(columnDefinition = "TEXT not null comment '타이틀'")
    protected String title;

    @Column(columnDefinition = "TEXT not null comment '내용'")
    protected String content;

}
