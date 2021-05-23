package com.dataart.playme.repository;

import com.dataart.playme.model.tokens.EmailConfirmationToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.Optional;

@Repository
public interface EmailConfirmationTokenRepository extends JpaRepository<EmailConfirmationToken, String> {

    @Query("SELECT t FROM EmailConfirmationToken t WHERE t.token = :#{#content}")
    Optional<EmailConfirmationToken> findByContent(@Param("content") String content);

    @Transactional
    @Modifying
    @Query("DELETE FROM EmailConfirmationToken t WHERE t.creationDatetime < :#{#minimalDate}")
    void cleanTokens(@Param("minimalDate") Date minimalDate);
}
