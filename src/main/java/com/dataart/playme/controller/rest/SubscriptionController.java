package com.dataart.playme.controller.rest;

import com.dataart.playme.controller.binding.annotation.ActiveBand;
import com.dataart.playme.controller.binding.annotation.CurrentMusician;
import com.dataart.playme.model.Band;
import com.dataart.playme.model.Musician;
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

    @Autowired
    public SubscriptionController(SubscriptionService subscriptionService) {
        this.subscriptionService = subscriptionService;
    }

    @GetMapping("/subscriptions")
    public List<Band> getSubscriptions(@CurrentMusician Musician musician) {
        return subscriptionService.getSubscriptions(musician);
    }

    @PostMapping("/bands/{band}/_subscribe")
    public void subscribe(@ActiveBand Band band, @CurrentMusician Musician musician) {
        subscriptionService.addSubscription(band, musician);
    }

    @PostMapping("/bands/{band}/_unsubscribe")
    public void unsubscribe(@PathVariable Band band, @CurrentMusician Musician musician) {
        subscriptionService.deleteSubscription(band, musician);
    }
}
