package com.dataart.playme.service;

import com.dataart.playme.model.Musician;
import com.dataart.playme.model.User;

public interface MusicianService {

    Musician findByUser(User user);
}
