import services.TicketSystemServices;
import ticket.model.validators.ARValidator;
import ticket.model.validators.BuyerValidator;
import ticket.model.validators.SellerValidator;
import ticket.persistance.ARRepository;
import ticket.persistance.BuyerRepository;
import ticket.persistance.SellerRepository;
import ticket.persistance.jdbc.ARDatabase;
import ticket.persistance.jdbc.BuyerDatabase;
import ticket.persistance.jdbc.SellerDatabase;
import ticket.server.TicketSystemServicesImpl;

import java.io.IOException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.Properties;

public class StartRMIServer {
    public static void main(String[] args) {

        Properties serverProps=new Properties();
        try {
            serverProps.load(StartRMIServer.class.getResourceAsStream("/ticketserver.properties"));
            System.out.println("Server properties set. ");
            serverProps.list(System.out);
        } catch (IOException e) {
            System.err.println("Cannot find ticketserver.properties "+e);
            return;
        }

        ARRepository repoAR = new ARDatabase(new ARValidator(), serverProps);
        BuyerRepository repoBuyer = new BuyerDatabase(new BuyerValidator(), serverProps);
        SellerRepository repoSeller = new SellerDatabase(new SellerValidator(), serverProps);

        TicketSystemServices ticketServerImpl = new TicketSystemServicesImpl(repoAR, repoBuyer, repoSeller);

//        try {
//            String name = serverProps.getProperty("ticket.rmi.serverID","Ticket");
//            TicketSystemServices stub =(TicketSystemServices) UnicastRemoteObject.exportObject(ticketServerImpl, 0);
//
//            Registry registry = LocateRegistry.getRegistry();
//            System.out.println("before binding");
//            registry.rebind(name, stub);
//            System.out.println("TicketSystem server bound");
//        } catch (Exception e) {
//            System.err.println("TicketSystem server exception:"+e);
//            e.printStackTrace();
//        }

    }

}
