package com.dataart.playme.service;

import com.dataart.playme.model.Band;
import com.dataart.playme.model.Musician;

import java.util.List;

public interface SubscriptionService {

    List<Band> getSubscriptions(Musician musician);

    void addSubscription(Band band, Musician musician);

    void deleteSubscription(Band band, Musician musician);
}
