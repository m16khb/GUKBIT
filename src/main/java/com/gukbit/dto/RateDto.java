package com.gukbit.dto;

import com.gukbit.domain.Rate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;


@Builder
@AllArgsConstructor
public class RateDto extends Rate{

  // Dto에서 필요한 부분을 빌더패턴을 통해 entity로 만드는 역할
  public Rate toEntity(){
    Rate build = Rate.builder()
        .rid(rid)
        .cCid(cCid)
        .userId(userId)
        .date(date)
        .oneStatement(oneStatement)
        .lecturersEval(lecturersEval)
        .curriculumEval(curriculumEval)
        .employmentEval(employmentEval)
        .cultureEval(cultureEval)
        .facilityEval(facilityEval)
        .advantage(advantage)
        .disadvantage(disadvantage)
        .build();
    return build;
  }
}
