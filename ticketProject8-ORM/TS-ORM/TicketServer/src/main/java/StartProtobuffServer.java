import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.internal.SessionFactoryImpl;
import ticket.network.utils.AbstractServer;
import ticket.network.utils.ServerException;
import ticket.network.utils.TicketSystemProtobuffConcurrentServer;
import services.TicketSystemServices;
import ticket.model.validators.ARValidator;
import ticket.model.validators.BuyerValidator;
import ticket.model.validators.SellerValidator;
import ticket.persistance.ARRepository;
import ticket.persistance.BuyerRepository;
import ticket.persistance.SellerRepository;
import ticket.persistance.jdbc.ARDatabase;
import ticket.persistance.jdbc.BuyerDatabase;
import ticket.persistance.jdbc.ORM.BuyerDatabaseORM;
import ticket.persistance.jdbc.SellerDatabase;
import ticket.server.TicketSystemServicesImpl;

import java.io.IOException;
import java.util.Properties;


public class StartProtobuffServer {
    private static int defaultPort=55555;

    static SessionFactory initialize() {
        final StandardServiceRegistry registry = new StandardServiceRegistryBuilder().configure().build();
        try {
            return new MetadataSources(registry).buildMetadata().buildSessionFactory();
        }
        catch (Exception exception) {
            System.out.println("Exceptie--------------------------- " + exception);
            StandardServiceRegistryBuilder.destroy(registry);
        }
        return null;
    }

    static void close(SessionFactory sessionFactory) {
        if ( sessionFactory != null ) {
            sessionFactory.close();
        }
    }

    public static void main(String[] args) {
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
        //BuyerRepository repoBuyer = new BuyerDatabase(new BuyerValidator(), serverProps);
        BuyerRepository repoBuyer = new BuyerDatabaseORM(new BuyerValidator(), serverProps, initialize());
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
        AbstractServer server = new TicketSystemProtobuffConcurrentServer(ticketServerPort, ticketServerImpl);
        try {
            server.start();
        } catch (ServerException e) {
            System.err.println("Error starting the server" + e.getMessage());
        }



    }
}
