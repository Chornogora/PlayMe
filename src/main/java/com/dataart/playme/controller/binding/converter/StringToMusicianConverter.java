package com.dataart.playme.controller.binding.converter;

import com.dataart.playme.exception.NoSuchRecordException;
import com.dataart.playme.model.Musician;
import com.dataart.playme.repository.MusicianRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class StringToMusicianConverter implements Converter<String, Musician> {

    private final MusicianRepository musicianRepository;

    @Autowired
    public StringToMusicianConverter(MusicianRepository musicianRepository) {
        this.musicianRepository = musicianRepository;
    }

    @Override
    public Musician convert(String bandId) {
        return musicianRepository.findById(bandId)
                .orElseThrow(() -> new NoSuchRecordException("Can't find musician by id"));
    }
}