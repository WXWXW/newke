package com.example.newke.vo;

import com.example.newke.dto.HomePageDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PageResult<T> {

    private List<T> recordList;

    private Integer count;



}
