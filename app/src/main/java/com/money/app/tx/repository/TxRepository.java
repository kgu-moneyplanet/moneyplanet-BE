package com.money.app.tx.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import com.money.app.tx.domain.Tx;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TxRepository extends JpaRepository<Tx, String> {
    Optional<Tx> findById(String id);
    List<Tx> findAllByUserIdAndTxDate(String userId, LocalDate txDate);
}
