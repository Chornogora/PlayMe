package com.dataart.playme.service.impl;

import com.dataart.playme.exception.NoSuchRecordException;
import com.dataart.playme.model.Musician;
import com.dataart.playme.model.User;
import com.dataart.playme.repository.MusicianRepository;
import com.dataart.playme.service.MusicianService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class MusicianServiceImpl implements MusicianService {

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
}
