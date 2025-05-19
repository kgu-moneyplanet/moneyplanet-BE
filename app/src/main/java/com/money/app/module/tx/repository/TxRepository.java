package com.money.app.module.tx.repository;

import com.money.app.module.tx.domain.Tx;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface TxRepository extends JpaRepository<Tx, String> {
    Optional<Tx> findById(String id);
    List<Tx> findAllByUserIdAndTxDate(String userId, LocalDate txDate);

    // 특정 유저의 거래 내역을 날짜 범위로 조회하는 메서드
    @Query("SELECT t FROM Tx t WHERE t.user.id = :userId AND t.txDate BETWEEN :startDate AND :endDate")
    List<Tx> findTransactionsByUserIdAndDateRange(String userId, LocalDate startDate, LocalDate endDate);
}
