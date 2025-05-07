package com.money.app.module.tx.repository;

import com.money.app.module.tx.domain.Report;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface ReportRepository extends JpaRepository<Report, String> {
    Optional<Report> findByTxId(String txId);
}
