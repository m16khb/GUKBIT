package com.gukbit.dto;

import com.gukbit.domain.Notice;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class NoticeDto extends Notice{

  // Dto에서 필요한 부분을 빌더패턴을 통해 entity로 만드는 역할
  public Notice toEntity(){
    Notice build = Notice.builder()
        .bid(bid)
        .author(author)
        .date(date)
        .view(view)
        .title(title)
        .content(content)
        .build();
    return build;
  }

}
