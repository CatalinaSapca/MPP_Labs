package ticket.network.rpcprotocol;

import ticket.network.dto.ArtisticRepresentationDTO;
import ticket.network.dto.BuyerDTO;
import ticket.network.dto.DTOUtils;
import ticket.network.dto.SellerDTO;
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
import java.util.ArrayList;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;


public class TicketSystemServicesRpcProxy implements TicketSystemServices {
    private String host;
    private int port;

    private TicketSystemObserver client;

    private ObjectInputStream input;
    private ObjectOutputStream output;
    private Socket connection;

    private BlockingQueue<Response> qresponses;
    private volatile boolean finished;
    public TicketSystemServicesRpcProxy(String host, int port) {
        this.host = host;
        this.port = port;
        qresponses=new LinkedBlockingQueue<Response>();
    }

    public Seller login(Seller seller, TicketSystemObserver client) throws ServicesException {
        initializeConnection();
        SellerDTO sellerDTO = DTOUtils.getDTO(seller);
        Request req = new Request.Builder().type(RequestType.LOGIN).data(sellerDTO).build();
        sendRequest(req);
        Response response = readResponse();

        if (response.type() == ResponseType.LOGIN){
            //this.client=client;
            this.client=client;
            SellerDTO seller1 = (SellerDTO) response.data();
            return DTOUtils.getFromDTO(seller1);
        }
        if (response.type() == ResponseType.OK){
            //this.client=client;
            this.client=client;
            SellerDTO seller1 = (SellerDTO) response.data();
            return DTOUtils.getFromDTO(seller1);
        }
        if (response.type() == ResponseType.ERROR){
            String err=response.data().toString();
            closeConnection();
            throw new ServicesException(err);
        }
        return seller;
    }

    public Buyer addBuyer(Buyer buyer) throws ServicesException {
        BuyerDTO buyerDTO = DTOUtils.getDTO(buyer);
        Request req = new Request.Builder().type(RequestType.ADD_BUYER).data(buyerDTO).build();
        sendRequest(req);
        Response response = readResponse();

        if (response.type() == ResponseType.ADDED_BUYER){
            //this.client=client;
            BuyerDTO buyer1 = (BuyerDTO) response.data();
            return DTOUtils.getFromDTO(buyer1);
        }
        if (response.type() == ResponseType.OK){
            //this.client=client;
            BuyerDTO buyer1 = (BuyerDTO) response.data();
            return DTOUtils.getFromDTO(buyer1);
        }
        if (response.type() == ResponseType.ERROR){
            String err=response.data().toString();
            closeConnection();
            throw new ServicesException(err);
        }
        return buyer;
    }

    public ArtisticRepresentation updateAR(ArtisticRepresentation artisticRepresentation, TicketSystemObserver client) throws ServicesException {
        ArtisticRepresentationDTO arDTO= DTOUtils.getDTO(artisticRepresentation);
        Request req = new Request.Builder().type(RequestType.BUY_TICKET).data(arDTO).build();
        sendRequest(req);
        Response response = readResponse();

        if (response.type() == ResponseType.BUY_TICKET){
            //this.client=client;
            ArtisticRepresentationDTO ar1 = (ArtisticRepresentationDTO) response.data();
            return DTOUtils.getFromDTO(ar1);
        }
        if (response.type() == ResponseType.OK){
            //this.client=client;
            ArtisticRepresentationDTO ar1 = (ArtisticRepresentationDTO) response.data();
            return DTOUtils.getFromDTO(ar1);
        }
        if (response.type() == ResponseType.ERROR){
            String err=response.data().toString();
            closeConnection();
            throw new ServicesException(err);
        }
        return artisticRepresentation;
    }

    public void logout(Seller seller, TicketSystemObserver client) throws ServicesException {
        SellerDTO sellerDTO= DTOUtils.getDTO(seller);
        Request req=new Request.Builder().type(RequestType.LOGOUT).data(sellerDTO).build();
        sendRequest(req);
        Response response=readResponse();
        closeConnection();
        if (response.type()== ResponseType.ERROR){
            String err=response.data().toString();
            throw new ServicesException(err);
        }
    }

    public Seller findSellerByUsername(Seller seller) throws ServicesException {
        SellerDTO sellerDTO = DTOUtils.getDTO(seller);
        Request req = new Request.Builder().type(RequestType.FIND_SELLER_BY_USERNAME).data(sellerDTO).build();
        sendRequest(req);
        Response response = readResponse();

        if (response.type() == ResponseType.GET_SELLER_BY_USERNAME){
            SellerDTO seller1 = (SellerDTO) response.data();
            return DTOUtils.getFromDTO(seller1);
        }
        if (response.type() == ResponseType.ERROR){
            String err=response.data().toString();
            closeConnection();
            throw new ServicesException(err);
        }
        return seller;
    }

    public Iterable<ArtisticRepresentation> getAllAR() throws ServicesException {
        Request req=new Request.Builder().type(RequestType.GET_ALL_AR).data("").build();
        sendRequest(req);
        Response response = readResponse();
        if (response.type()== ResponseType.ERROR){
            String err = response.data().toString();
            throw new ServicesException(err);
        }
        Iterable<ArtisticRepresentationDTO> arsDTO = (Iterable<ArtisticRepresentationDTO>) response.data();
        Iterable<ArtisticRepresentation> ars = DTOUtils.getFromDTO(arsDTO);
        return ars;
    }

    public Iterable<ArtisticRepresentation> getAllARFromDate(LocalDateTime date) throws ServicesException {
        Request req=new Request.Builder().type(RequestType.GET_ALL_AR_FROM_DATE).data(date).build();
        sendRequest(req);
        Response response = readResponse();
        if (response.type()== ResponseType.ERROR){
            String err = response.data().toString();
            throw new ServicesException(err);
        }
        ArrayList<ArtisticRepresentationDTO> arsDTO = (ArrayList<ArtisticRepresentationDTO>) response.data();
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
            throw new ServicesException("Error sending object "+e);
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


    private void handleUpdate(Response response){
        if (response.type()== ResponseType.BOUGHT_TICKET){

            ArtisticRepresentation ar = DTOUtils.getFromDTO((ArtisticRepresentationDTO) response.data());
            System.out.println("Someone bought tickets " + ar);
            try {
                client.boughtTicket(ar);
            } catch (SQLException | ServicesException | RemoteException throwables) {
                throwables.printStackTrace();
            }
        }
//        if (response.type() == ResponseType.ADDED_BUYER){
//            Buyer buyer = DTOUtils.getFromDTO((BuyerDTO) response.data());
//            System.out.println("Buyer added " + buyer);
//            try {
//                client.(friend);
//            } catch (ServicesException e) {
//                e.printStackTrace();
//            }
//        }
//
//        if (response.type()== ResponseType.NEW_MESSAGE){
//            Message message= DTOUtils.getFromDTO((MessageDTO)response.data());
//            try {
//                client.messageReceived(message);
//            } catch (ChatException e) {
//                e.printStackTrace();
//            }
//        }
    }

    private boolean isUpdate(Response response){
        return response.type()== ResponseType.BOUGHT_TICKET;
    }
    private class ReaderThread implements Runnable{
        public void run() {
            while(!finished){
                try {
                    Object response=input.readObject();
                    System.out.println("response received "+response);
                    if (isUpdate((Response)response)){
                        handleUpdate((Response)response);
                    }else{

                        try {
                            qresponses.put((Response)response);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                } catch (IOException | ClassNotFoundException e) {
                    System.out.println("Reading error "+e);
                }
            }
        }
    }
}
