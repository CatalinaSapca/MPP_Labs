package services;


import ticket.model.ArtisticRepresentation;

import java.sql.SQLException;

public interface TicketSystemObserver {
//     void messageReceived(Message message) throws TicketSystemException;
//     void friendLoggedIn(User friend) throws TicketSystemException;
//     void friendLoggedOut(User friend) throws TicketSystemException;

     void boughtTicket(ArtisticRepresentation ar) throws ServicesException, SQLException;
}
