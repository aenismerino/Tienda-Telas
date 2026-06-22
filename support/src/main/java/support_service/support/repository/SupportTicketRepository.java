package support_service.support.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import support_service.support.model.SupportTicket;

public interface SupportTicketRepository extends JpaRepository<SupportTicket, Long> {

}
