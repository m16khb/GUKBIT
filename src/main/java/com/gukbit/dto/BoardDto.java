package com.gukbit.dto;

import com.gukbit.domain.Board;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class BoardDto extends Board{


  // Dto에서 필요한 부분을 빌더패턴을 통해 entity로 만드는 역할
  public Board toEntity(){
    Board build = Board.builder()
        .bid(bid)
        .author(author)
        .date(date)
        .view(view)
        .title(title)
        .content(content)
        .bAcademyName(bAcademyName)
        .bAcademyCode(bAcademyCode)
        .bCourseName(bCourseName)
        .bCourseCode(bCourseCode)
        .visible(visible)
        .recommend(recommend)
        .build();
    return build;
  }

}
