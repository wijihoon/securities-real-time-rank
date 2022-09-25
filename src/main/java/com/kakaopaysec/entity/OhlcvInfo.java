package com.kakaopaysec.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 캔들정보 DTO
 */

@Entity
@Builder
@Table(name = "ohlcv")
@AllArgsConstructor()
@NoArgsConstructor
@Getter
@Setter
public class OhlcvInfo {

	@Id
	@Column(length = 32)
    private String code;
    
	@Column
    private BigDecimal windowSize;
	
	@Column
	private LocalDateTime timestamp;
	
	@Column
	private BigDecimal open;
	
	@Column
	private BigDecimal close;

}
