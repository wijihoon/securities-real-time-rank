package com.kakaopaysec.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.kakaopaysec.entity.OhlcvInfo;

@Repository
public interface OhlcvRepository extends JpaRepository<OhlcvInfo, Long> {
	Optional<OhlcvInfo> findByCode(String code);
	List<OhlcvInfo> findAllByCode(String code);
    List<OhlcvInfo> findAll();
}

