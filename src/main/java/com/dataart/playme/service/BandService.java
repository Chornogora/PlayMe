package com.dataart.playme.service;

import com.dataart.playme.dto.BandCreatingDto;
import com.dataart.playme.dto.BandFilterBean;
import com.dataart.playme.dto.MemberDto;
import com.dataart.playme.model.Band;
import com.dataart.playme.model.Membership;
import com.dataart.playme.model.Musician;

import java.util.List;

public interface BandService {

    boolean isMemberOf(Band band, Musician musician);

    boolean canChangePost(Band band, Musician musician);

    List<Band> getByMultipleId(List<String> bandIds);

    List<Band> findBands(BandFilterBean filterBean);

    Band createBand(BandCreatingDto dto, Musician musician);

    Band updateBand(BandCreatingDto dto, Band band, Musician changedBy);

    Membership addMember(MemberDto dto, Musician addedBy);

    Membership updateMember(MemberDto dto, Musician changedBy);

    void deleteMember(Band band, Musician musician, Musician deletedBy);
}
