import ticket.network.utils.AbstractServer;
import ticket.network.utils.ServerException;
import ticket.network.utils.TicketSystemRpcConcurrentServer;
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
import java.util.Properties;

public class StartRpcServer {

    private static int defaultPort=55555;


    public static void main(String[] args) {
        // UserRepository userRepo=new UserRepositoryMock();
        Properties serverProps=new Properties();
        try {
            serverProps.load(StartRpcServer.class.getResourceAsStream("/ticketserver.properties"));
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

        int ticketServerPort=defaultPort;
        try {
            ticketServerPort = Integer.parseInt(serverProps.getProperty("ticket.server.port"));
        }catch (NumberFormatException nef){
            System.err.println("Wrong  Port Number" + nef.getMessage());
            System.err.println("Using default port " + defaultPort);
        }
        System.out.println("Starting server on port: " + ticketServerPort);
        AbstractServer server = new TicketSystemRpcConcurrentServer(ticketServerPort, ticketServerImpl);
        try {
            server.start();
        } catch (ServerException e) {
            System.err.println("Error starting the server" + e.getMessage());
        }finally {
            try {
                server.stop();
            }catch(ServerException e){
                System.err.println("Error stopping server "+e.getMessage());
            }
        }
    }
}
