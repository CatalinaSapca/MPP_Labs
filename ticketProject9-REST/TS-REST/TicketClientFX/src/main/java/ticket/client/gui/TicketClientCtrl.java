package ticket.client.gui;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import services.ServicesException;
import services.TicketSystemObserver;
import services.TicketSystemServices;
import ticket.model.ArtisticRepresentation;
import ticket.model.Buyer;
import ticket.model.Seller;

import javax.swing.*;
import java.io.Serializable;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class TicketClientCtrl implements TicketSystemObserver{

    private TicketSystemServices server;

    private Seller currentSeller;
    private ObservableList<ArtisticRepresentation> modelAll;
    private ObservableList<ArtisticRepresentation> modelAllSearch;


    public TicketClientCtrl(TicketSystemServices server) {
        this.server = server;
        modelAll = FXCollections.observableArrayList();
        modelAllSearch = FXCollections.observableArrayList();
    }

    public Seller getCurrentSeller() {
        return currentSeller;
    }

    public TicketSystemServices getServer() {
        return server;
    }

    public ObservableList<ArtisticRepresentation> getModelAll() {
        return modelAll;
    }

    public ObservableList<ArtisticRepresentation> getModelAllSearch() {
        return modelAllSearch;
    }

    public void setCurrentSeller(Seller currentSeller) {
        this.currentSeller = currentSeller;
    }

    public Seller login(Seller seller) throws ServicesException {
        try {
            return server.login(seller,this);
        } catch (RemoteException e) {
            throw new ServicesException("Error logging " + e);
        }
    }

    public void logout(Seller seller) {
        try {
            server.logout(seller, this);
        } catch (ServicesException | RemoteException e) {
            System.out.println("Logout error "+e);
        }
    }

    public List<ArtisticRepresentation> getAllARFromDate(LocalDateTime data) throws ServicesException {
        try {
            Iterable<ArtisticRepresentation> ars = server.getAllARFromDate(data);
            List<ArtisticRepresentation> arsList= StreamSupport.stream(ars.spliterator(), false)
                    .collect(Collectors.toList());
            modelAll.setAll(arsList);
            return arsList;
        } catch (RemoteException e) {
            throw new ServicesException("Error logging " + e);
        }
        catch (SQLException e) {
            throw new ServicesException("SQLException " + e);
        }
    }

    public List<ArtisticRepresentation> getAllAR() throws ServicesException {
        try {
            Iterable<ArtisticRepresentation> ars = server.getAllAR();
            List<ArtisticRepresentation> arsList= StreamSupport.stream(ars.spliterator(), false)
                    .collect(Collectors.toList());
            modelAll.setAll(arsList);
            return arsList;
        } catch (RemoteException e) {
            throw new ServicesException("Error logging " + e);
        }
        catch (SQLException e) {
            throw new ServicesException("SQLException " + e);
        }
    }

    public ArtisticRepresentation updateAR(ArtisticRepresentation ar) throws ServicesException{
        try {
            ArtisticRepresentation a = server.updateAR(ar, this);
            return a;
        } catch (RemoteException e) {
            throw new ServicesException("Error logging " + e);
        }
        catch (SQLException e) {
            throw new ServicesException("SQLException " + e);
        }
    }

    public Buyer addBuyer(Buyer buyer) throws ServicesException {
        try {
            Buyer b = server.addBuyer(buyer);
            return b;
        } catch (RemoteException e) {
            throw new ServicesException("Error logging " + e);
        }
    }

    public void boughtTicket(ArtisticRepresentation ar) throws ServicesException, SQLException {
        modelAll.removeIf(artisticRepresentation -> artisticRepresentation.getId().equals(ar.getId()));
        modelAll.add(ar);

        ObservableList<ArtisticRepresentation> x = FXCollections.observableArrayList();
        x.addAll(modelAll);
        modelAll.removeAll(modelAll);
        modelAll.setAll(x);

        //-----------
        modelAllSearch.removeIf(artisticRepresentation -> artisticRepresentation.getId().equals(ar.getId()));
        modelAllSearch.add(ar);
    }


}
