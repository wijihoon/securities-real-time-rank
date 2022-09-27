package com.kakaopaysec.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.kakaopaysec.entity.InvestAgentVolInfo;

@Repository
public interface InvestAgentVolRepository extends JpaRepository<InvestAgentVolInfo, Long> {
	Optional<InvestAgentVolInfo> findByCode(String code);
}
