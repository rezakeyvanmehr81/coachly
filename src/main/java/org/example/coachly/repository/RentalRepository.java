package org.example.coachly.repository;

import org.example.coachly.model.Rental;
import org.example.coachly.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RentalRepository extends JpaRepository<Rental, Long> {

    List<Rental> findByBorrowerOrderByCreatedAtDesc(User borrower);

    List<Rental> findByItem_OwnerOrderByCreatedAtDesc(User owner);
}

