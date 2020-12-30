package com.dataart.playme.repository;

import com.dataart.playme.model.MemberStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberStatusRepository extends JpaRepository<MemberStatus, String> {

    Optional<MemberStatus> findByName(String name);
}
