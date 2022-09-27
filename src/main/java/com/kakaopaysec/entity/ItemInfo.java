package com.kakaopaysec.entity;



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
 * 종목정보 DTO
 */
@Entity
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor()
@Table(name = "item")
public class ItemInfo {
	
	@Id
	@Column(length = 32)
    private String code;
    
	@Column(length = 128)
    private String codeName;


}
