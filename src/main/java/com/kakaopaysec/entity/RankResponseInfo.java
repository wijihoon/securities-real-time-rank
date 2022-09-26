package com.kakaopaysec.entity;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder
@AllArgsConstructor()
@NoArgsConstructor
@Getter
@Setter
public class RankResponseInfo {

    private String code;
    private String codeName;
	private Double rank;
	private BigDecimal price;
	private BigDecimal percent;

}
