package services;


import ticket.model.ArtisticRepresentation;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.sql.SQLException;

public interface TicketSystemObserver extends Remote{
     void boughtTicket(ArtisticRepresentation ar) throws ServicesException, SQLException, RemoteException;
}
