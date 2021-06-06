package com.dataart.playme.service.impl;

import com.dataart.playme.dto.EditUserDto;
import com.dataart.playme.dto.UpdateMusicianDto;
import com.dataart.playme.exception.NoSuchRecordException;
import com.dataart.playme.model.Musician;
import com.dataart.playme.model.User;
import com.dataart.playme.repository.MusicianRepository;
import com.dataart.playme.service.MusicianService;
import com.dataart.playme.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class MusicianServiceImpl implements MusicianService {

    private UserService userService;

    private final MusicianRepository musicianRepository;

    @Autowired
    public MusicianServiceImpl(MusicianRepository musicianRepository) {
        this.musicianRepository = musicianRepository;
    }

    @Override
    public Musician findByUser(User user) {
        return musicianRepository.findByUser(user)
                .orElseThrow(() -> new NoSuchRecordException("Can't find musician"));
    }

    @Override
    public Musician createMusician(User user) {
        Musician musician = new Musician();
        musician.setId(UUID.randomUUID().toString());
        musician.setUser(user);
        return musicianRepository.save(musician);
    }

    @Override
    @Transactional
    public Musician updateMusician(UpdateMusicianDto musician, Musician currentMusician) {
        User updatedUser = updateUserData(musician, currentMusician.getUser());
        return updateMusicianData(musician, currentMusician, updatedUser);
    }

    private User updateUserData(UpdateMusicianDto musician, User userToUpdate) {
        EditUserDto editUserDto = new EditUserDto();
        editUserDto.setFirstName(musician.getFirstName());
        editUserDto.setLastName(musician.getLastName());
        editUserDto.setEmail(musician.getEmail());
        editUserDto.setBirthdate(userToUpdate.getBirthdate());
        editUserDto.setStatus(userToUpdate.getStatus().getName());
        return userService.updateUser(userToUpdate.getId(), editUserDto);
    }

    private Musician updateMusicianData(UpdateMusicianDto musician, Musician currentMusician,
                                        User updatedUser) {
        currentMusician.setUser(updatedUser);
        currentMusician.setMusicianSkills(musician.getMusicianSkills());
        currentMusician.setEmailNotifications(musician.isEmailNotifications());
        return musicianRepository.save(currentMusician);
    }

    @Lazy
    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }
}
