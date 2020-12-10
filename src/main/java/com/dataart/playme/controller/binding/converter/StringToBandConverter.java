package com.dataart.playme.controller.binding.converter;

import com.dataart.playme.exception.NoSuchRecordException;
import com.dataart.playme.model.Band;
import com.dataart.playme.repository.BandRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class StringToBandConverter implements Converter<String, Band> {

    private final BandRepository bandRepository;

    @Autowired
    public StringToBandConverter(BandRepository bandRepository) {
        this.bandRepository = bandRepository;
    }

    @Override
    public Band convert(String bandId) {
        return bandRepository.findById(bandId)
                .orElseThrow(() -> new NoSuchRecordException("Can't find band by id"));
    }
}
