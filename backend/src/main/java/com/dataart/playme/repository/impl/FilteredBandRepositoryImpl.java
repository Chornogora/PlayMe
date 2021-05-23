package com.dataart.playme.repository.impl;

import com.dataart.playme.dto.BandFilterBean;
import com.dataart.playme.model.Band;
import com.dataart.playme.repository.FilteredBandRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
import java.util.List;

@Repository
public class FilteredBandRepositoryImpl extends AbstractConstraintRepository implements FilteredBandRepository {

    private final EntityManager entityManager;

    @Autowired
    public FilteredBandRepositoryImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public List<Band> getBandsFiltered(BandFilterBean filterBean) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Band> query = criteriaBuilder.createQuery(Band.class);
        Root<Band> root = query.from(Band.class);

        Predicate[] predicates = new Predicate[]{
                criteriaBuilder.like(root.get("name"), "%" + filterBean.getName() + "%"),
                criteriaBuilder.between(root.get("creationDate"), filterBean.getMinCreationDate(), filterBean.getMaxCreationDate()),
        };

        Order order = ASCENDING.equals(filterBean.getSortingType()) ?
                criteriaBuilder.asc(root.get(filterBean.getSortingField())) :
                criteriaBuilder.desc(root.get(filterBean.getSortingField()));

        query.where(predicates).orderBy(order);
        TypedQuery<Band> typedQuery = entityManager
                .createQuery(query)
                .setMaxResults(filterBean.getLimit())
                .setFirstResult(filterBean.getOffset());
        return typedQuery.getResultList();
    }
}
