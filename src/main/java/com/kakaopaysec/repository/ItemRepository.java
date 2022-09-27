package com.kakaopaysec.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.kakaopaysec.entity.ItemInfo;

@Repository
public interface ItemRepository extends JpaRepository<ItemInfo, Long> {
    ItemInfo findByCode(String code);
}
