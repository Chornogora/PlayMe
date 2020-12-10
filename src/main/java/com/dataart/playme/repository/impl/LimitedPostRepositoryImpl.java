package com.dataart.playme.repository.impl;

import com.dataart.playme.dto.PostRequestDto;
import com.dataart.playme.model.Post;
import com.dataart.playme.repository.LimitedPostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
public class LimitedPostRepositoryImpl implements LimitedPostRepository {

    private static final String QUERY_STRING = "SELECT p FROM Post p " +
            "WHERE p.band IN :bands " +
            "ORDER by p.creationDatetime DESC";

    private final EntityManager entityManager;

    @Autowired
    protected LimitedPostRepositoryImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public List<Post> findByBand(PostRequestDto dto) {
        return entityManager.createQuery(QUERY_STRING, Post.class)
                .setMaxResults(dto.getLimit())
                .setFirstResult(dto.getOffset())
                .setParameter("bands", dto.getBands())
                .getResultList();
    }
}
