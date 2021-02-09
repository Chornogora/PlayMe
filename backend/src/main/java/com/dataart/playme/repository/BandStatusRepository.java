package com.dataart.playme.repository;

import com.dataart.playme.model.BandStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BandStatusRepository extends JpaRepository<BandStatus, String> {

    BandStatus findByName(String name);
}
