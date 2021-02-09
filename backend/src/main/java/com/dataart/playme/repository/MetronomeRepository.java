package com.dataart.playme.repository;

import com.dataart.playme.model.Metronome;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MetronomeRepository extends JpaRepository<Metronome, String> {
}
