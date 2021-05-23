package com.dataart.playme.service.impl;

import com.dataart.playme.exception.ConflictException;
import com.dataart.playme.exception.NoSuchRecordException;
import com.dataart.playme.model.Band;
import com.dataart.playme.model.MemberStatus;
import com.dataart.playme.model.Membership;
import com.dataart.playme.model.Musician;
import com.dataart.playme.repository.MemberStatusRepository;
import com.dataart.playme.repository.MembershipRepository;
import com.dataart.playme.service.BandService;
import com.dataart.playme.service.SubscriptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SubscriptionServiceImpl implements SubscriptionService {

    private final BandService bandService;

    private final MembershipRepository membershipRepository;

    private final MemberStatusRepository memberStatusRepository;

    @Autowired
    public SubscriptionServiceImpl(BandService bandService, MembershipRepository membershipRepository, MemberStatusRepository memberStatusRepository) {
        this.bandService = bandService;
        this.membershipRepository = membershipRepository;
        this.memberStatusRepository = memberStatusRepository;
    }

    @Override
    public List<Band> getSubscriptions(Musician musician) {
        return musician.getMemberships().stream()
                .map(Membership::getBand)
                .collect(Collectors.toList());
    }

    @Override
    public void addSubscription(Band band, Musician musician) {
        if (!bandService.isMemberOf(band, musician)) {
            String statusName = MemberStatus.ExistedStatus.SUBSCRIBER.getValue();
            MemberStatus memberStatus = memberStatusRepository.findByName(statusName)
                    .orElseThrow(() -> new NoSuchRecordException("Cannot find member status"));
            membershipRepository.save(new Membership(new Membership.MembershipId(musician.getId(), band.getId()),
                    musician, band, memberStatus));
            return;
        }
        throw new ConflictException("User is a subscriber or a member of this group");
    }

    @Override
    public void deleteSubscription(Band band, Musician musician) {
        String statusName = MemberStatus.ExistedStatus.SUBSCRIBER.getValue();
        Membership.MembershipId membershipId = new Membership.MembershipId(musician.getId(), band.getId());
        Membership membership = membershipRepository.findById(membershipId)
                .orElseThrow(() -> new NoSuchRecordException("Can't find member"));
        if (membership.getStatus().getName().equals(statusName)) {
            membershipRepository.delete(membership);
            return;
        }
        throw new ConflictException("User is not a subscriber");
    }
}
