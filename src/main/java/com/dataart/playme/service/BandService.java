package com.dataart.playme.service;

import com.dataart.playme.model.Band;
import com.dataart.playme.model.Musician;

import java.util.List;

public interface BandService {

    boolean isMemberOf(Band band, Musician musician);

    boolean canChangePost(Band band, Musician musician);

    List<Band> getByMultipleId(List<String> bandIds);
}
