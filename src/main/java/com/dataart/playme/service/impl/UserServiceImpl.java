package com.dataart.playme.service.impl;

import com.dataart.playme.dto.EditUserDto;
import com.dataart.playme.dto.FilterBean;
import com.dataart.playme.exception.NoSuchRecordException;
import com.dataart.playme.exception.NonUniqueException;
import com.dataart.playme.model.Status;
import com.dataart.playme.model.User;
import com.dataart.playme.repository.StatusRepository;
import com.dataart.playme.repository.UserRepository;
import com.dataart.playme.service.UserService;
import com.dataart.playme.util.Constants;
import com.dataart.playme.util.DateUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    private final StatusRepository statusRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, StatusRepository statusRepository) {
        this.userRepository = userRepository;
        this.statusRepository = statusRepository;
    }

    @Override
    public User getById(String id) {
        return userRepository.findById(id).orElseThrow(() -> new NoSuchRecordException("User was not found"));
    }

    @Override
    public User getByLogin(String login) {
        return userRepository.findByLogin(login).orElseThrow(() -> new NoSuchRecordException("User was not found"));
    }

    @Override
    public List<User> findFiltered(FilterBean filterBean) {
        return userRepository.findFiltered(filterBean);
    }

    @Override
    public int getUsersCount(FilterBean filterBean) {
        return userRepository.getUsersCount(filterBean);
    }

    @Override
    public User addUser(User user) {
        String id = UUID.randomUUID().toString();
        user.setId(id);
        user.setCreationDate(new Date(System.currentTimeMillis()));
        return userRepository.save(user);
    }

    @Override
    public User updateUser(String userId, EditUserDto changes) {
        User user = getById(userId);
        user.setFirstName(changes.getFirstName());
        user.setLastName(changes.getLastName());
        user.setBirthdate(changes.getBirthdate());

        if (!changes.getEmail().equals(user.getEmail())) {
            user.setEmail(changes.getEmail());
            String statusName = Status.StatusName.PENDING.getValue();
            Status pendingStatus = statusRepository.findByName(statusName)
                    .orElseThrow(() -> new NoSuchRecordException("Cannot find pending status"));
            user.setStatus(pendingStatus);
        } else if (!user.getStatus().getName().equals(changes.getStatus())) {
            Status newStatus = statusRepository.findByName(changes.getStatus())
                    .orElseThrow(() -> new NoSuchRecordException("Cannot find status"));
            user.setStatus(newStatus);
        }

        try {
            return userRepository.save(user);
        } catch (DataIntegrityViolationException e) {
            throw new NonUniqueException("Email is not unique");
        }
    }

    @Override
    public User deleteUser(User user) {
        String statusName = Status.StatusName.DELETED.getValue();
        Status deletedStatus = statusRepository.findByName(statusName)
                .orElseThrow(() -> new NoSuchRecordException("Cannot find deleted status"));
        if (!user.getStatus().equals(deletedStatus)) {
            String dateFormatted = DateUtil.dateToString(new Date(System.currentTimeMillis()));
            String modifiedLogin = user.getLogin() + Constants.DELETED_USER_MARK + dateFormatted;
            user.setLogin(modifiedLogin);
            user.setPassword(StringUtils.EMPTY);
            user.setStatus(deletedStatus);
            return userRepository.save(user);
        }
        return user;
    }
}
