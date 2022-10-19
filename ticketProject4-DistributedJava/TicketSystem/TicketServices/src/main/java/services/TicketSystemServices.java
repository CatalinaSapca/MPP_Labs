package services;

import ticket.model.ArtisticRepresentation;
import ticket.model.Buyer;
import ticket.model.Seller;

import java.sql.SQLException;
import java.time.LocalDateTime;


public interface TicketSystemServices {
//     void login(User user, IChatObserver client) throws ChatException;
//     void sendMessage(Message message) throws ChatException;
//     void logout(User user, IChatObserver client) throws ChatException;
//     User[] getLoggedFriends(User user) throws ChatException;

     Seller login(Seller seller, TicketSystemObserver client) throws ServicesException;
     void logout(Seller seller, TicketSystemObserver client) throws ServicesException;
     Seller findSellerByUsername(Seller seller) throws ServicesException;
     Iterable<ArtisticRepresentation> getAllAR() throws SQLException, ServicesException;
     ArtisticRepresentation updateAR(ArtisticRepresentation artisticRepresentation, TicketSystemObserver client) throws ServicesException, SQLException;
     Buyer addBuyer(Buyer buyer) throws ServicesException;
     Iterable<ArtisticRepresentation> getAllARFromDate(LocalDateTime date) throws SQLException, ServicesException;
}
