package com.gukbit.domain;

import lombok.*;

import javax.persistence.*;


@Getter
@Setter
@Entity
@Table(name = "rate")
@Builder
@ToString
@AllArgsConstructor
public class Rate {
    @Id
    @Column(name = "rid")
    protected String rid;
    @Column(name = "c_cid")
    protected String cCid;
    @Column(name = "user_id")
    protected String userId;
    @Column(name = "one_statement")
    protected String oneStatement;
    @Column(name = "lecturers_eval")
    protected Double lecturersEval;
    @Column(name = "curriculum_eval")
    protected Double curriculumEval;
    @Column(name = "employment_eval")
    protected Double employmentEval;
    @Column(name = "culture_eval")
    protected Double cultureEval;
    @Column(name = "facility_eval")
    protected Double facilityEval;
    @Column(name = "advantage")
    protected String advantage;
    @Column(name = "disadvantage")
    protected String disadvantage;
    @Column(name = "date")
    protected String date;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "c_cid", referencedColumnName = "cid", insertable = false, updatable = false)
    protected Course course;

    protected Rate() {
    }
}
