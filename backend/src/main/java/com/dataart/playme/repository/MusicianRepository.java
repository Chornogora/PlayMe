package com.dataart.playme.repository;

import com.dataart.playme.model.Musician;
import com.dataart.playme.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MusicianRepository extends JpaRepository<Musician, String> {

    Optional<Musician> findByUser(User user);
}
