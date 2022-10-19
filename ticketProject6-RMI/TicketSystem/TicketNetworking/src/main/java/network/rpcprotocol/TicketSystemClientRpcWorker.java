package network.rpcprotocol;

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
import java.util.List;


public class TicketSystemClientRpcWorker implements Runnable, TicketSystemObserver {
    private TicketSystemServices server;
    private Socket connection;

    private ObjectInputStream input;
    private ObjectOutputStream output;
    private volatile boolean connected;

    public TicketSystemClientRpcWorker(TicketSystemServices server, Socket connection) {
        this.server = server;
        this.connection = connection;
        try{
            output=new ObjectOutputStream(connection.getOutputStream());
            output.flush();
            input=new ObjectInputStream(connection.getInputStream());
            connected=true;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void run() {
        while(connected){
            try {
                Object request=input.readObject();
                Response response=handleRequest((Request)request);
                if (response!=null){
                    sendResponse(response);
                }
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        try {
            input.close();
            output.close();
            connection.close();
        } catch (IOException e) {
            System.out.println("Error "+e);
        }
    }

    public void boughtTicket(ArtisticRepresentation artisticRepresentation) throws ServicesException {
        ArtisticRepresentationDTO arDTO= DTOUtils.getDTO(artisticRepresentation);
        Response resp=new Response.Builder().type(ResponseType.BOUGHT_TICKET).data(arDTO).build();
        System.out.println("Someone bought tickets " + artisticRepresentation);
        try {
            sendResponse(resp);
        } catch (IOException e) {
            throw new ServicesException("Sending error: " + e);
        }
    }

    public void buyTicket(ArtisticRepresentation artisticRepresentation) throws ServicesException {
        ArtisticRepresentationDTO arDTO= DTOUtils.getDTO(artisticRepresentation);
        Response resp=new Response.Builder().type(ResponseType.BUY_TICKET).data(arDTO).build();
        System.out.println("You bought tickets " + artisticRepresentation);
        try {
            sendResponse(resp);
        } catch (IOException e) {
            throw new ServicesException("Sending error: " + e);
        }
    }

    public void getAllAR(List<ArtisticRepresentation> ars) throws ServicesException {
        Iterable<ArtisticRepresentationDTO> arsDTO = DTOUtils.getDTO(ars);
        Response resp=new Response.Builder().type(ResponseType.GET_ALL_AR).data(arsDTO).build();
        System.out.println("You asked for all AR");
        try {
            sendResponse(resp);
        } catch (IOException e) {
            throw new ServicesException("Sending error: " + e);
        }
    }

    public void getAllARFromDate(List<ArtisticRepresentation> ars) throws ServicesException {
        Iterable<ArtisticRepresentationDTO> arsDTO = DTOUtils.getDTO(ars);
        Response resp=new Response.Builder().type(ResponseType.GET_ALL_AR_FROM_DATE).data(arsDTO).build();
        System.out.println("You asked for all AR from date");
        try {
            sendResponse(resp);
        } catch (IOException e) {
            throw new ServicesException("Sending error: " + e);
        }
    }

    private static Response okResponse=new Response.Builder().type(ResponseType.OK).build();
  //  private static Response errorResponse=new Response.Builder().type(ResponseType.ERROR).build();

    private Response handleRequest(Request request){
        Response response=null;
        if (request.type()== RequestType.LOGIN){
            System.out.println("Login request ..."+request.type());
            SellerDTO sellerDTO = (SellerDTO) request.data();
            Seller seller = DTOUtils.getFromDTO(sellerDTO);
            try {
                Seller s = server.login(seller, this);
                SellerDTO sDTO = DTOUtils.getDTO(s);
                return new Response.Builder().type(ResponseType.LOGIN).data(sDTO).build();
            } catch (ServicesException | RemoteException e) {
                connected=false;
                return new Response.Builder().type(ResponseType.ERROR).data(e.getMessage()).build();
            }
        }
        if (request.type()== RequestType.LOGOUT){
            System.out.println("Logout request");
            SellerDTO sellerDTO = (SellerDTO) request.data();
            Seller s = DTOUtils.getFromDTO(sellerDTO);
            try {
                server.logout(s, this);
                connected=false;
                return okResponse;

            } catch (ServicesException | RemoteException e) {
                return new Response.Builder().type(ResponseType.ERROR).data(e.getMessage()).build();
            }
        }
        if (request.type()== RequestType.GET_ALL_AR){
            System.out.println("GetAllAR Request ...");
            try {
                Iterable<ArtisticRepresentation> ars = server.getAllAR();
                Iterable<ArtisticRepresentationDTO> arsDTO = DTOUtils.getDTO(ars);
                return new Response.Builder().type(ResponseType.GET_ALL_AR).data(arsDTO).build();
            } catch (ServicesException | SQLException | RemoteException e) {
                return new Response.Builder().type(ResponseType.ERROR).data(e.getMessage()).build();
            }
        }

        if (request.type()== RequestType.GET_ALL_AR_FROM_DATE){
            System.out.println("GetAllARFromDate Request ...");
            LocalDateTime data = (LocalDateTime) request.data();
            try {
                Iterable<ArtisticRepresentation> ars = server.getAllARFromDate(data);
                Iterable<ArtisticRepresentationDTO> arsDTO = DTOUtils.getDTO(ars);
                return new Response.Builder().type(ResponseType.GET_ALL_AR_FROM_DATE).data(arsDTO).build();
            } catch (ServicesException | SQLException | RemoteException e) {
                return new Response.Builder().type(ResponseType.ERROR).data(e.getMessage()).build();
            }
        }

        if (request.type()== RequestType.BUY_TICKET){
            System.out.println("BuyTicket Request ...");
            ArtisticRepresentationDTO arDTO = (ArtisticRepresentationDTO) request.data();
            ArtisticRepresentation ar = DTOUtils.getFromDTO(arDTO);
            try {
                ArtisticRepresentation upAr = server.updateAR(ar, this);
                ArtisticRepresentationDTO upArDTO = DTOUtils.getDTO(upAr);
                return new Response.Builder().type(ResponseType.BUY_TICKET).data(upArDTO).build();
            } catch (ServicesException | SQLException | RemoteException e) {
                return new Response.Builder().type(ResponseType.ERROR).data(e.getMessage()).build();
            }
        }

        if (request.type()== RequestType.ADD_BUYER){
            System.out.println("AddBuyer Request ...");
            BuyerDTO buyerDTO = (BuyerDTO) request.data();
            Buyer buyer = DTOUtils.getFromDTO(buyerDTO);
            try {
                Buyer buyer1 = server.addBuyer(buyer);
                BuyerDTO buyer2 = DTOUtils.getDTO(buyer1);
                return new Response.Builder().type(ResponseType.ADDED_BUYER).data(buyer2).build();
            } catch (ServicesException | RemoteException e) {
                return new Response.Builder().type(ResponseType.ERROR).data(e.getMessage()).build();
            }
        }

        if (request.type()== RequestType.FIND_SELLER_BY_USERNAME){
            System.out.println("FindSellerByUsername Request ...");
            SellerDTO sellerDTO = (SellerDTO) request.data();
            Seller seller = DTOUtils.getFromDTO(sellerDTO);
            try {
                Seller seller1 = server.findSellerByUsername(seller);
                SellerDTO seller2 = DTOUtils.getDTO(seller1);
                return new Response.Builder().type(ResponseType.GET_SELLER_BY_USERNAME).data(seller2).build();
            } catch (ServicesException | RemoteException e) {
                return new Response.Builder().type(ResponseType.ERROR).data(e.getMessage()).build();
            }
        }

        return response;
    }

    private void sendResponse(Response response) throws IOException{
        System.out.println("sending response "+response);
        output.writeObject(response);
        output.flush();
    }
}
