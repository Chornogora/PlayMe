package com.dataart.playme.service.impl;

import com.dataart.playme.model.Band;
import com.dataart.playme.model.MemberStatus;
import com.dataart.playme.model.Membership;
import com.dataart.playme.model.Musician;
import com.dataart.playme.repository.BandRepository;
import com.dataart.playme.service.BandService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BandServiceImpl implements BandService {

    private final BandRepository bandRepository;

    @Autowired
    public BandServiceImpl(BandRepository bandRepository) {
        this.bandRepository = bandRepository;
    }

    @Override
    public boolean isMemberOf(Band band, Musician musician) {
        return band.getMembers().stream()
                .anyMatch(bandMembership -> musician.getMemberships().stream()
                        .anyMatch(musicianMembership -> musicianMembership.equals(bandMembership)));
    }

    @Override
    public boolean canChangePost(Band band, Musician musician) {
        Membership membership = band.getMembers().stream()
                .filter(bandMembership -> musician.getMemberships().stream()
                        .anyMatch(musicianMembership -> musicianMembership.equals(bandMembership)))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("musician is not a member of a current band"));
        String statusName = membership.getStatus().getName();
        return statusName.equals(MemberStatus.ExistedStatus.LEADER.getValue()) ||
                statusName.equals(MemberStatus.ExistedStatus.ADMINISTRATOR.getValue());
    }

    @Override
    public List<Band> getByMultipleId(List<String> bandIds) {
        return bandRepository.findByMultipleId(bandIds);
    }
}
