package com.dataart.playme.repository;

import com.dataart.playme.model.Membership;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MembershipRepository extends JpaRepository<Membership, Membership.MembershipId> {
}
