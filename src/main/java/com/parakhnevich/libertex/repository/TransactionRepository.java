package com.parakhnevich.libertex.repository;

import com.parakhnevich.libertex.constant.TransactionStatus;
import com.parakhnevich.libertex.repository.entity.TransactionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface TransactionRepository extends JpaRepository<TransactionEntity, Long> {

    @Transactional
    @Modifying
    @Query(value = "update TransactionEntity t set t.status = :status where t.id = :id")
    void updateStatus(Long id, TransactionStatus status);
}
