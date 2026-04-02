package com.wit.travel.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RouteDayVO {

    private Integer dayNumber;

    private List<ScenicVO> scenics;
}
