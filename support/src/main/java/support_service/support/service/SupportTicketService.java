package support_service.support.service;

import support_service.support.DTO.SupportTicketDTO;
import support_service.support.model.SupportTicket;
import java.util.List;
public interface SupportTicketService {

    SupportTicket createTicket(SupportTicketDTO ticketDTO);
    SupportTicket getTicketById(Long id);
    List<SupportTicket> getAllTickets();
    SupportTicket updateTicketStatus(Long id, String status);
}
