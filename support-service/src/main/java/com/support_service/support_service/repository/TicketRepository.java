package com.support_service.support_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.support_service.support_service.model.Ticket;

@Repository
public interface TicketRepository extends JpaRepository<Ticket, Long> {
}
