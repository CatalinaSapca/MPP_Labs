package services;

import ticket.model.ArtisticRepresentation;
import ticket.model.Buyer;
import ticket.model.Seller;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.sql.SQLException;
import java.time.LocalDateTime;


public interface TicketSystemServices {

     Seller login(Seller seller, TicketSystemObserver client) throws ServicesException, RemoteException;
     void logout(Seller seller, TicketSystemObserver client) throws ServicesException, RemoteException;
     Seller findSellerByUsername(Seller seller) throws ServicesException, RemoteException;
     Iterable<ArtisticRepresentation> getAllAR() throws SQLException, ServicesException, RemoteException;
     ArtisticRepresentation updateAR(ArtisticRepresentation artisticRepresentation, TicketSystemObserver client) throws ServicesException, SQLException, RemoteException;
     Buyer addBuyer(Buyer buyer) throws ServicesException, RemoteException;
     Iterable<ArtisticRepresentation> getAllARFromDate(LocalDateTime date) throws SQLException, ServicesException, RemoteException;
}
