package ticket.server;

import services.ServicesException;
import services.TicketSystemObserver;
import services.TicketSystemServices;
import ticket.model.ArtisticRepresentation;
import ticket.model.Buyer;
import ticket.model.Seller;
import ticket.persistance.ARRepository;
import ticket.persistance.BuyerRepository;
import ticket.persistance.SellerRepository;

import java.rmi.RemoteException;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class TicketSystemServicesImpl implements TicketSystemServices {

    private final ARRepository repoAR;
    private final BuyerRepository repoBuyer;
    private final SellerRepository repoSeller;

    private final Map<Long, TicketSystemObserver> loggedUsers;

    public TicketSystemServicesImpl(ARRepository repoAR, BuyerRepository repoBuyer, SellerRepository repoSeller) {
        this.repoAR = repoAR;
        this.repoBuyer = repoBuyer;
        this.repoSeller = repoSeller;

        loggedUsers = new ConcurrentHashMap<>();
    }

    public synchronized Seller login(Seller seller, TicketSystemObserver client) throws ServicesException, RemoteException {
        Seller s = repoSeller.findOneByUsername(seller.getUsername());
        if (s != null){
            if(loggedUsers.get(s.getId()) != null)
                throw new ServicesException("User already logged in.");
            else {
                if(seller.getPassword().equals(s.getPassword()))
                    loggedUsers.put(s.getId(), client);
            }
        }
        return s;
    }


    public synchronized ArtisticRepresentation updateAR(ArtisticRepresentation artisticRepresentation, TicketSystemObserver client) throws ServicesException, SQLException, RemoteException {
        ArtisticRepresentation ar = repoAR.update(artisticRepresentation);
        if(ar==null){
            notifyAllUsers(artisticRepresentation);
        }
        return ar;
    }

    private final int defaultThreadsNo=5;
    private void notifyAllUsers(ArtisticRepresentation ar) throws ServicesException, SQLException {
        Iterable<Seller> sellers=repoSeller.findAll();
        System.out.println("Users "+sellers);

        ExecutorService executor= Executors.newFixedThreadPool(defaultThreadsNo);
        for(Seller s : sellers){
            TicketSystemObserver ticketClient = loggedUsers.get(s.getId());
            if (ticketClient != null)
                executor.execute(() -> {
                    try {
                        System.out.println("Notifying [" + s.getId()+ "] ar ["+ar.getId()+"] was updated.");
                        ticketClient.boughtTicket(ar);
                    } catch (ServicesException | SQLException | RemoteException e) {
                        System.err.println("Error notifying user " + e);
                    }
                });
        }

        executor.shutdown();
    }

    public synchronized Buyer addBuyer(Buyer buyer) throws RemoteException {
        Buyer b = repoBuyer.save(buyer);
        return b;
    }

    public synchronized void logout(Seller seller, TicketSystemObserver client) throws ServicesException, RemoteException {
        TicketSystemObserver localClient = loggedUsers.remove(seller.getId());
        if (localClient==null)
            throw new ServicesException("User " + seller.getId() + " is not logged in.");
    }

    public Seller findSellerByUsername(Seller seller) throws ServicesException, RemoteException {
        return repoSeller.findOneByUsername(seller.getUsername());
    }

    public synchronized Iterable<ArtisticRepresentation> getAllAR() throws SQLException, RemoteException {
        return repoAR.findAll();
    }

    public synchronized Iterable<ArtisticRepresentation> getAllARFromDate(LocalDateTime date) throws SQLException, RemoteException {
        ArrayList<ArtisticRepresentation> all=new ArrayList<>();
        this.repoAR.findAll().forEach(x->{
            if(x.getData().getYear()==date.getYear() && x.getData().getMonth()==date.getMonth() && x.getData().getDayOfMonth()==date.getDayOfMonth())
                all.add(x);
        });
        return all;
    }
}
