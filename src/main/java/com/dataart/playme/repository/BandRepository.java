package com.dataart.playme.repository;

import com.dataart.playme.model.Band;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BandRepository extends JpaRepository<Band, String> {

    @Query("SELECT b FROM Band b WHERE b.id IN :#{#bandIds}")
    List<Band> findByMultipleId(@Param("bandIds") List<String> bandIds);
}
