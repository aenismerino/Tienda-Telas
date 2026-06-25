package support_service.support.service;

import org.springframework.stereotype.Service;
import support_service.support.DTO.SupportTicketDTO;
import support_service.support.model.SupportTicket;
import support_service.support.repository.SupportTicketRepository;

import java.util.List;

@Service
public class SupportTicketServiceImpl implements SupportTicketService {

    private final SupportTicketRepository ticketRepository;

    public SupportTicketServiceImpl(SupportTicketRepository ticketRepository) {
        this.ticketRepository = ticketRepository;
    }

    @Override
    public SupportTicket createTicket(SupportTicketDTO ticketDTO) {
        SupportTicket ticket = new SupportTicket();
        
        ticket.setUserId(ticketDTO.getUserId());
        ticket.setOrderId(ticketDTO.getOrderId());
        ticket.setSubject(ticketDTO.getSubject());
        ticket.setDescription(ticketDTO.getDescription());
        
        return ticketRepository.save(ticket);
    }

    @Override
    public SupportTicket getTicketById(Long id) {
        return ticketRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Ticket de soporte no encontrado con el ID: " + id));
    }

    @Override
    public List<SupportTicket> getAllTickets() {
        return ticketRepository.findAll();
    }

    @Override
    public SupportTicket updateTicketStatus(Long id, String status) {
        SupportTicket ticket = getTicketById(id);
        ticket.setStatus(status.toUpperCase());
        return ticketRepository.save(ticket);
    }
}
