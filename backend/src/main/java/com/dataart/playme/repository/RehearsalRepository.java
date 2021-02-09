package com.dataart.playme.repository;

import com.dataart.playme.model.Musician;
import com.dataart.playme.model.Rehearsal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RehearsalRepository extends JpaRepository<Rehearsal, String> {

    @Query("SELECT DISTINCT r FROM Rehearsal r " +
            "LEFT JOIN r.members m " +
            "WHERE r.creator.id = :#{#musician.id} " +
            "OR :#{#musician.id} IN (m.id)")
    List<Rehearsal> findByMusician(@Param("musician") Musician musician);
}
