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
 * 거래주체별거래량 DTO
 */
@Entity
@Builder
@Table(name = "invest_agent_vol")
@AllArgsConstructor()
@NoArgsConstructor
@Getter
@Setter
public class InvestAgentVolInfo {
	
	@Id
	@Column(length = 32)
    private String code;
    
	@Column
    private BigDecimal windowSize;
	
	@Column
	private LocalDateTime timestamp;
	
	@Column
	private BigDecimal foreighVolume;
	
	@Column
	private BigDecimal istttVolume;
	
	@Column
	private BigDecimal indiVolume;
	
	@Column
	private BigDecimal see;

}
