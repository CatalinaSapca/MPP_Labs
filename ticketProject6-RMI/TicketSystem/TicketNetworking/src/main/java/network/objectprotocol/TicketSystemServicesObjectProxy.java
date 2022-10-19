package network.objectprotocol;

import network.dto.ArtisticRepresentationDTO;
import network.dto.BuyerDTO;
import network.dto.DTOUtils;
import network.dto.SellerDTO;
import services.ServicesException;
import services.TicketSystemObserver;
import services.TicketSystemServices;
import ticket.model.ArtisticRepresentation;
import ticket.model.Buyer;
import ticket.model.Seller;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.rmi.RemoteException;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;


public class TicketSystemServicesObjectProxy implements TicketSystemServices {
    private String host;
    private int port;

    private TicketSystemObserver client;

    private ObjectInputStream input;
    private ObjectOutputStream output;
    private Socket connection;

    //private List<Response> responses;
    private BlockingQueue<Response> qresponses;
    private volatile boolean finished;
    public TicketSystemServicesObjectProxy(String host, int port) {
        this.host = host;
        this.port = port;
        //responses=new ArrayList<Response>();
        qresponses=new LinkedBlockingQueue<Response>();
    }

    public Seller login(Seller seller, TicketSystemObserver client) throws ServicesException {
        initializeConnection();
        SellerDTO sellerDTO= DTOUtils.getDTO(seller);
        sendRequest(new LoginRequest(sellerDTO));
        Response response=readResponse();
        if (response instanceof OkResponse){
            this.client=client;
            return seller;
        }
        if (response instanceof ErrorResponse){
            ErrorResponse err=(ErrorResponse)response;
            closeConnection();
            throw new ServicesException(err.getMessage());
        }

        LoginResponse resp=(LoginResponse) response;
        SellerDTO seller1 = resp.getSeller();
        Seller seller2 = DTOUtils.getFromDTO(seller1);
        return seller2;
    }

    public ArtisticRepresentation updateAR(ArtisticRepresentation artisticRepresentation, TicketSystemObserver client) throws ServicesException {
        ArtisticRepresentationDTO arDTO= DTOUtils.getDTO(artisticRepresentation);
        sendRequest(new BuyTicketRequest(arDTO));
        Response response=readResponse();
        if (response instanceof ErrorResponse){
            ErrorResponse err=(ErrorResponse)response;
            throw new ServicesException(err.getMessage());
        }

        this.client=client;
        BuyTicketResponse resp=(BuyTicketResponse) response;
        ArtisticRepresentationDTO ar = resp.getAr();
        ArtisticRepresentation ars = DTOUtils.getFromDTO(ar);
        return ars;
    }

    public void logout(Seller seller, TicketSystemObserver client) throws ServicesException {
        SellerDTO sellerDTO= DTOUtils.getDTO(seller);
        sendRequest(new LogoutRequest(sellerDTO));
        Response response=readResponse();
        closeConnection();
        if (response instanceof ErrorResponse){
            ErrorResponse err=(ErrorResponse)response;
            throw new ServicesException(err.getMessage());
        }
    }


    public Seller findSellerByUsername(Seller seller) throws ServicesException {
        return null;
    }

    public Buyer addBuyer(Buyer buyer) throws ServicesException {
        //initializeConnection();
        BuyerDTO buyerDTO= DTOUtils.getDTO(buyer);
        sendRequest(new AddBuyerRequest(buyerDTO));
        Response response=readResponse();
        if (response instanceof ErrorResponse){
            ErrorResponse err=(ErrorResponse)response;
            closeConnection();
            throw new ServicesException(err.getMessage());
        }
        AddBuyerResponse resp=(AddBuyerResponse) response;
        BuyerDTO buyerDTO1 = resp.getBuyer();
        Buyer buyer1 = DTOUtils.getFromDTO(buyerDTO1);
        return buyer1;
    }

    public Iterable<ArtisticRepresentation> getAllAR() throws ServicesException {
        sendRequest(new GetAllARRequest());
        Response response=readResponse();
        if (response instanceof ErrorResponse){
            ErrorResponse err=(ErrorResponse)response;
            throw new ServicesException(err.getMessage());
        }
        GetAllARResponse resp=(GetAllARResponse) response;
        Iterable<ArtisticRepresentationDTO> arsDTO=resp.getARs();
        Iterable<ArtisticRepresentation> ars = DTOUtils.getFromDTO(arsDTO);
        return ars;
    }

    public Iterable<ArtisticRepresentation> getAllARFromDate(LocalDateTime date) throws ServicesException {
        sendRequest(new GetAllARFromDateRequest(date));
        Response response=readResponse();
        if (response instanceof ErrorResponse){
            ErrorResponse err=(ErrorResponse)response;
            throw new ServicesException(err.getMessage());
        }
        GetAllARFromDateResponse resp=(GetAllARFromDateResponse) response;
        Iterable<ArtisticRepresentationDTO> arsDTO=resp.getARs();
        Iterable<ArtisticRepresentation> ars = DTOUtils.getFromDTO(arsDTO);
        return ars;
    }

    private void closeConnection() {
        finished=true;
        try {
            input.close();
            output.close();
            connection.close();
            client=null;
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void sendRequest(Request request)throws ServicesException {
        try {
            output.writeObject(request);
            output.flush();
        } catch (IOException e) {
            throw new ServicesException("Error sending object " + e);
        }

    }

    private Response readResponse() throws ServicesException {
        Response response=null;
        try{

            response=qresponses.take();

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return response;
    }
    private void initializeConnection() throws ServicesException {
         try {
            connection=new Socket(host,port);
            output=new ObjectOutputStream(connection.getOutputStream());
            output.flush();
            input=new ObjectInputStream(connection.getInputStream());
            finished=false;
            startReader();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void startReader(){
        Thread tw=new Thread(new ReaderThread());
        tw.start();
    }


    private void handleUpdate(UpdateResponse update){
        if (update instanceof BoughtTicketResponse){

            BoughtTicketResponse bUpd=(BoughtTicketResponse) update;
            ArtisticRepresentation ar= DTOUtils.getFromDTO(bUpd.getAR());
            System.out.println("Someone bought tickets to " + ar);
            try {
                client.boughtTicket(ar);
            } catch (ServicesException | SQLException | RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    private class ReaderThread implements Runnable{
        public void run() {
            while(!finished){
                try {
                    Object response=input.readObject();
                    System.out.println("response received "+response);
                    if (response instanceof UpdateResponse){
                         handleUpdate((UpdateResponse)response);
                    }else{
                        /*responses.add((Response)response);
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        synchronized (responses){
                            responses.notify();
                        }*/
                        try {
                            qresponses.put((Response)response);
                        } catch (InterruptedException e) {
                            e.printStackTrace();  
                        }
                    }
                } catch (IOException e) {
                    System.out.println("Reading error "+e);
                } catch (ClassNotFoundException e) {
                    System.out.println("Reading error "+e);
                }
            }
        }
    }
}
