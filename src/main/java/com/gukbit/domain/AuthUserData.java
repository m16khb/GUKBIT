package com.gukbit.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

@Entity
@Getter @Setter
@ToString
@Table(name = "auth_user_data")
@NoArgsConstructor
public class AuthUserData {
    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Integer aid;

    @Column(name = "user_id")
    protected String userId;

    @Column(name = "academy_code")
    protected String academyCode;

    @Column(name = "course_id")
    protected String courseId;

    @Column(name = "course_name")
    protected String courseName;

    @Column(name = "session")
    protected Integer session;

    public AuthUserData(String userId, String academyCode, String courseId, String courseName, Integer session) {
        this.userId = userId;
        this.academyCode = academyCode;
        this.courseId = courseId;
        this.courseName = courseName;
        this.session = session;
    }
}
