package com.dataart.playme.service.impl;

import com.dataart.playme.dto.BandCreatingDto;
import com.dataart.playme.dto.BandFilterBean;
import com.dataart.playme.dto.MemberDto;
import com.dataart.playme.exception.ConflictException;
import com.dataart.playme.exception.NoSuchRecordException;
import com.dataart.playme.exception.NoSufficientPrivilegesException;
import com.dataart.playme.model.*;
import com.dataart.playme.repository.*;
import com.dataart.playme.service.BandService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class BandServiceImpl implements BandService {

    private final BandRepository bandRepository;

    private final MemberStatusRepository memberStatusRepository;

    private final MembershipRepository membershipRepository;

    private final MusicianRepository musicianRepository;

    private final BandStatusRepository bandStatusRepository;

    @Autowired
    public BandServiceImpl(BandRepository bandRepository, MemberStatusRepository memberStatusRepository,
                           MembershipRepository membershipRepository, MusicianRepository musicianRepository,
                           BandStatusRepository bandStatusRepository) {
        this.bandRepository = bandRepository;
        this.memberStatusRepository = memberStatusRepository;
        this.membershipRepository = membershipRepository;
        this.musicianRepository = musicianRepository;
        this.bandStatusRepository = bandStatusRepository;
    }

    @Override
    public List<Band> findBands(BandFilterBean filterBean) {
        return bandRepository.getBandsFiltered(filterBean);
    }

    @Override
    @Transactional
    public Band createBand(BandCreatingDto dto, Musician musician) {
        Band band = constructBand(dto);
        Band createdBand = bandRepository.save(band);
        Membership leader = createLeader(createdBand, musician);
        Membership createdMembership = membershipRepository.save(leader);
        createdBand.setMembers(Collections.singletonList(createdMembership));
        return createdBand;
    }

    @Override
    public Band updateBand(BandCreatingDto dto, Band band, Musician changedBy) {
        if (isLeader(band, changedBy)) {
            band.setName(dto.getName());
            return bandRepository.save(band);
        }
        throw new NoSufficientPrivilegesException("Can't update band: user is not a leader");
    }

    @Override
    public Membership addMember(MemberDto dto, Musician addedBy) {
        Musician musician = musicianRepository.findById(dto.getMusicianId())
                .orElseThrow(() -> new NoSuchRecordException("Can't find musician"));
        if (isLeader(dto.getBand(), addedBy) && !isPlayerOf(dto.getBand(), musician)) {
            if (dto.getStatusName().equals(MemberStatus.ExistedStatus.LEADER.getValue())) {
                throw new ConflictException("Can't add one more leader");
            }
            if (dto.getStatusName().equals(MemberStatus.ExistedStatus.SUBSCRIBER.getValue())) {
                throw new ConflictException("Can't add subscriber");
            }
            return saveMember(dto.getBand(), musician, dto.getStatusName());
        }
        throw new ConflictException("Can't add member: member has already added or user is not a leader");
    }

    @Override
    @Transactional
    public Membership updateMember(MemberDto dto, Musician changedBy) {
        Musician musician = musicianRepository.findById(dto.getMusicianId())
                .orElseThrow(() -> new NoSuchRecordException("Can't find musician"));

        if (dto.getStatusName().equals(MemberStatus.ExistedStatus.SUBSCRIBER.getValue())) {
            throw new ConflictException("Cannot change status to subscriber");
        }

        if (isLeader(dto.getBand(), changedBy) && isMemberOf(dto.getBand(), musician)) {
            if (dto.getStatusName().equals(MemberStatus.ExistedStatus.LEADER.getValue())) {
                String administratorStatusName = MemberStatus.ExistedStatus.ADMINISTRATOR.getValue();
                saveMember(dto.getBand(), changedBy, administratorStatusName);
            }
            return saveMember(dto.getBand(), musician, dto.getStatusName());
        }
        throw new ConflictException("Can't update member: musician is not a member or user is not a leader");
    }

    @Override
    public void deleteMember(Band band, Musician musician, Musician deletedBy) {
        if (isLeader(band, deletedBy)) {
            if (!musician.equals(deletedBy)) {
                deleteMember(musician.getId(), band.getId());
                return;
            } else if (getBandMembers(band).size() == 1) {
                disableBand(band);
                return;
            }
            throw new ConflictException("Can't delete user: need to change leader");
        }
        if (musician.equals(deletedBy)) {
            deleteMember(musician.getId(), band.getId());
            return;
        }
        throw new NoSufficientPrivilegesException("Can't delete member: user is not a band leader");
    }

    @Override
    public Band disableBand(Band band) {
        String disabledStatusName = BandStatus.StatusName.DISABLED.getValue();
        BandStatus bandStatus = bandStatusRepository.findByName(disabledStatusName);
        band.setBandStatus(bandStatus);
        return bandRepository.save(band);
    }

    @Override
    public Band activateBand(Band band) {
        String activeStatusName = BandStatus.StatusName.ACTIVE.getValue();
        BandStatus bandStatus = bandStatusRepository.findByName(activeStatusName);
        band.setBandStatus(bandStatus);
        return bandRepository.save(band);
    }

    @Override
    public boolean isMemberOf(Band band, Musician musician) {
        return band.getMembers().stream()
                .anyMatch(bandMembership -> musician.getMemberships().stream()
                        .anyMatch(musicianMembership -> musicianMembership.equals(bandMembership)));
    }

    @Override
    public boolean canChangePost(Band band, Musician musician) {
        return hasAdminRights(band, musician);
    }

    @Override
    public List<Band> getByMultipleId(List<String> bandIds) {
        return bandRepository.findByMultipleId(bandIds);
    }

    @Override
    public boolean isActiveBand(Band band) {
        String activeStatusName = BandStatus.StatusName.ACTIVE.getValue();
        return band.getBandStatus().getName().equals(activeStatusName);
    }

    private Band constructBand(BandCreatingDto dto) {
        Band band = new Band();
        band.setId(UUID.randomUUID().toString());
        band.setName(dto.getName());
        band.setCreationDate(new Date(System.currentTimeMillis()));

        String bandStatusName = BandStatus.StatusName.ACTIVE.getValue();
        BandStatus bandStatus = bandStatusRepository.findByName(bandStatusName);
        band.setBandStatus(bandStatus);

        return band;
    }

    private Membership saveMember(Band band, Musician musician, String statusName) {
        MemberStatus memberStatus = memberStatusRepository.findByName(statusName)
                .orElseThrow(() -> new NoSuchRecordException("Cannot find member status"));
        return membershipRepository.save(new Membership(new Membership.MembershipId(musician.getId(), band.getId()),
                musician, band, memberStatus));
    }

    private Membership createLeader(Band band, Musician leader) {
        String leaderStatusName = MemberStatus.ExistedStatus.LEADER.getValue();
        MemberStatus memberStatus = memberStatusRepository.findByName(leaderStatusName)
                .orElseThrow(() -> new NoSuchRecordException("Cannot find leader status"));
        return new Membership(new Membership.MembershipId(leader.getId(), band.getId()),
                leader, band, memberStatus);
    }

    private boolean hasAdminRights(Band band, Musician musician) {
        String statusName = getMemberStatusName(band, musician);
        return statusName.equals(MemberStatus.ExistedStatus.LEADER.getValue()) ||
                statusName.equals(MemberStatus.ExistedStatus.ADMINISTRATOR.getValue());
    }

    private boolean isLeader(Band band, Musician musician) {
        String statusName = getMemberStatusName(band, musician);
        return statusName.equals(MemberStatus.ExistedStatus.LEADER.getValue());
    }

    private String getMemberStatusName(Band band, Musician musician) {
        Membership membership = band.getMembers().stream()
                .filter(bandMembership -> musician.getMemberships().stream()
                        .anyMatch(musicianMembership -> musicianMembership.equals(bandMembership)))
                .findFirst()
                .orElseThrow(() -> new ConflictException("musician is not a member of a current band"));
        return membership.getStatus().getName();
    }

    private List<Membership> getBandMembers(Band band) {
        String subscriberStatusName = MemberStatus.ExistedStatus.SUBSCRIBER.getValue();
        return band.getMembers().stream()
                .filter(membership -> !membership.getStatus().getName().equals(subscriberStatusName))
                .collect(Collectors.toList());
    }

    private boolean isPlayerOf(Band band, Musician musician) {
        return getBandMembers(band).stream()
                .map(membership -> membership.getMusician().getId())
                .anyMatch(id -> id.equals(musician.getId()));
    }

    private void deleteMember(String musicianId, String bandId) {
        Membership membership = membershipRepository
                .findById(new Membership.MembershipId(musicianId, bandId))
                .orElseThrow(() -> new NoSuchRecordException("Can't find membership"));
        if (!membership.getStatus().getName().equals(MemberStatus.ExistedStatus.SUBSCRIBER.getValue())) {
            membershipRepository.delete(membership);
            return;
        }
        throw new ConflictException("Cannot delete subscriber");
    }
}
