package com.dataart.playme.repository;

import com.dataart.playme.model.tokens.CaptchaToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

public interface CaptchaTokenRepository extends JpaRepository<CaptchaToken, String> {

    @Transactional
    @Modifying
    @Query("DELETE FROM CaptchaToken t WHERE t.creationDatetime < :#{#minimalDate}")
    void cleanTokens(@Param("minimalDate") Date minimalDate);
}
