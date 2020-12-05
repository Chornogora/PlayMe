package com.dataart.playme.repository;

import com.dataart.playme.dto.FilterBean;
import com.dataart.playme.model.User;

import java.util.List;

public interface FilteredUserRepository {

    List<User> findFiltered(FilterBean filterBean);
}
