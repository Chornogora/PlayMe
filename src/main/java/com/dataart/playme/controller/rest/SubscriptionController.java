package com.dataart.playme.controller.rest;

import com.dataart.playme.controller.binding.annotation.CurrentMusician;
import com.dataart.playme.exception.ConflictException;
import com.dataart.playme.model.Band;
import com.dataart.playme.model.Musician;
import com.dataart.playme.service.BandService;
import com.dataart.playme.service.SubscriptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class SubscriptionController {

    private final SubscriptionService subscriptionService;

    private final BandService bandService;

    @Autowired
    public SubscriptionController(SubscriptionService subscriptionService, BandService bandService) {
        this.subscriptionService = subscriptionService;
        this.bandService = bandService;
    }

    @GetMapping("/subscriptions")
    public List<Band> getSubscriptions(@CurrentMusician Musician musician) {
        return subscriptionService.getSubscriptions(musician);
    }

    @PostMapping("/bands/{band}/_subscribe")
    public void subscribe(@PathVariable Band band, @CurrentMusician Musician musician) {
        if (bandService.isActiveBand(band)) {
            subscriptionService.addSubscription(band, musician);
            return;
        }
        throw new ConflictException("Band is disabled");
    }

    @PostMapping("/bands/{band}/_unsubscribe")
    public void unsubscribe(@PathVariable Band band, @CurrentMusician Musician musician) {
        subscriptionService.deleteSubscription(band, musician);
    }
}
