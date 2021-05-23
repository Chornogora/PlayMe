package com.dataart.playme.repository;

import com.dataart.playme.model.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StatusRepository extends JpaRepository<Status, String> {

    Optional<Status> findByName(String name);
}
