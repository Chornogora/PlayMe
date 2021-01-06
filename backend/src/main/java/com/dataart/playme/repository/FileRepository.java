package com.dataart.playme.repository;

import com.dataart.playme.model.File;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FileRepository extends JpaRepository<File, String> {
}
