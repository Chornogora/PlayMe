package com.dataart.playme.repository;

import com.dataart.playme.model.Membership;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MembershipRepository extends JpaRepository<Membership, Membership.MembershipId> {
}
