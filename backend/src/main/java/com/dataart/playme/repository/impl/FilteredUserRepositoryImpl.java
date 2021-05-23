package com.dataart.playme.repository.impl;

import com.dataart.playme.dto.FilterBean;
import com.dataart.playme.model.User;
import com.dataart.playme.repository.FilteredUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.List;
import java.util.Map;

@Repository
public class FilteredUserRepositoryImpl extends AbstractConstraintRepository implements FilteredUserRepository {

    private static final Map<String, String> SORTING_PARAMETERS = Map.of(
            "id", "u.id",
            "login", "u.login",
            "email", "u.email",
            "firstName", "u.firstName",
            "lastName", "u.lastName",
            "birthdate", "u.birthdate",
            "creationDate", "u.creationDate");

    private static final String FILTERED_SEARCH_QUERY_PATTERN = "SELECT u FROM User u " +
            "WHERE u.login LIKE CONCAT('%%', :login, '%%') " +
            "AND u.email LIKE CONCAT('%%', :email, '%%') " +
            "AND u.firstName LIKE CONCAT('%%', :firstName, '%%') " +
            "AND u.lastName LIKE CONCAT('%%', :lastName, '%%') " +
            "AND u.birthdate BETWEEN :birthdateFrom AND :birthdateTo " +
            "AND u.creationDate BETWEEN :creationDateFrom AND :creationDateTo " +
            "AND u.role.name IN :roles " +
            "AND u.status.name IN :statuses " +
            "ORDER BY %s %s";

    private final EntityManager entityManager;

    @Autowired
    protected FilteredUserRepositoryImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public List<User> findFiltered(FilterBean filterBean) {
        String queryString = getFilteredSearchQuery(filterBean);
        Query query = entityManager.createQuery(queryString, User.class)
                .setMaxResults(filterBean.getLimit())
                .setFirstResult(filterBean.getOffset());
        Query formed = query.setParameter("login", filterBean.getLogin())
                .setParameter("email", filterBean.getEmail())
                .setParameter("firstName", filterBean.getFirstName())
                .setParameter("lastName", filterBean.getLastName())
                .setParameter("birthdateFrom", filterBean.getBirthdateFrom())
                .setParameter("birthdateTo", filterBean.getBirthdateTo())
                .setParameter("creationDateFrom", filterBean.getCreationDateFrom())
                .setParameter("creationDateTo", filterBean.getCreationDateTo())
                .setParameter("roles", filterBean.getRoles())
                .setParameter("statuses", filterBean.getStatuses());
        return formed.getResultList();
    }

    private String getFilteredSearchQuery(FilterBean filterBean) {
        String sortingField = SORTING_PARAMETERS.get(filterBean.getSortingField());
        String sortingType = SORTING_TYPES.get(filterBean.getSortingType());
        return String.format(FILTERED_SEARCH_QUERY_PATTERN, sortingField, sortingType);
    }
}
