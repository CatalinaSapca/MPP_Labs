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
import java.util.List;


public class TicketSystemClientObjectWorker implements Runnable, TicketSystemObserver {
    private TicketSystemServices server;
    private Socket connection;

    private ObjectInputStream input;
    private ObjectOutputStream output;
    private volatile boolean connected;
    public TicketSystemClientObjectWorker(TicketSystemServices server, Socket connection) {
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
                Object response=handleRequest((Request)request);
                if (response!=null){
                   sendResponse((Response) response);
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
        System.out.println("Someone bought tickets " + artisticRepresentation);
        try {
            sendResponse(new BoughtTicketResponse(arDTO));
        } catch (IOException e) {
            throw new ServicesException("Sending error: " + e);
        }
    }

    public void buyTicket(ArtisticRepresentation artisticRepresentation) throws ServicesException {
        ArtisticRepresentationDTO arDTO= DTOUtils.getDTO(artisticRepresentation);
        System.out.println("You bought tickets " + artisticRepresentation);
        try {
            sendResponse(new BuyTicketResponse(arDTO));
        } catch (IOException e) {
            throw new ServicesException("Sending error: " + e);
        }
    }

    public void getAllAR(List<ArtisticRepresentation> ars) throws ServicesException {
        Iterable<ArtisticRepresentationDTO> arsDTO = DTOUtils.getDTO(ars);
        System.out.println("You asked for all AR");
        try {
            sendResponse(new GetAllARResponse(arsDTO));
        } catch (IOException e) {
            throw new ServicesException("Sending error: " + e);
        }
    }

    public void getAllARFromDate(List<ArtisticRepresentation> ars) throws ServicesException {
        Iterable<ArtisticRepresentationDTO> arsDTO = DTOUtils.getDTO(ars);
        System.out.println("You asked for all AR from date");
        try {
            sendResponse(new GetAllARFromDateResponse(arsDTO));
        } catch (IOException e) {
            throw new ServicesException("Sending error: " + e);
        }
    }

    private Response handleRequest(Request request){
        Response response = null;
        if (request instanceof LoginRequest){
            System.out.println("Login request ...");
            LoginRequest logReq = (LoginRequest)request;
            SellerDTO sellerDTO = logReq.getSeller();
            Seller seller =  DTOUtils.getFromDTO(sellerDTO);
            try {
                Seller seller1 = server.login(seller, this);
                return new LoginResponse(DTOUtils.getDTO(seller1));
            } catch (ServicesException | RemoteException e) {
                connected=false;
                return new ErrorResponse(e.getMessage());
            }
        }
        if (request instanceof LogoutRequest){
            System.out.println("Logout request");
            LogoutRequest logReq = (LogoutRequest)request;
            SellerDTO sellerDTO = logReq.getSeller();
            Seller seller = DTOUtils.getFromDTO(sellerDTO);
            try {
                server.logout(seller, this);
                connected=false;
                return new OkResponse();

            } catch (ServicesException | RemoteException e) {
               return new ErrorResponse(e.getMessage());
            }
        }
        if (request instanceof BuyTicketRequest){
            System.out.println("BuyTicketRequest ...");
            BuyTicketRequest buyTicketRequestReq = (BuyTicketRequest)request;
            ArtisticRepresentationDTO arDTO = buyTicketRequestReq.getArDTO();
            ArtisticRepresentation ar = DTOUtils.getFromDTO(arDTO);
            try {
                ArtisticRepresentation arr = server.updateAR(ar,this);
                return new BuyTicketResponse(DTOUtils.getDTO(arr));
            } catch (ServicesException | SQLException | RemoteException e) {
                return new ErrorResponse(e.getMessage());
            }
        }

        if (request instanceof AddBuyerRequest){
            System.out.println("AddBuyerRequest ...");
            AddBuyerRequest addBuyerRequest = (AddBuyerRequest) request;
            BuyerDTO buyerDTO = addBuyerRequest.getBuyer();
            Buyer buyer = DTOUtils.getFromDTO(buyerDTO);
            try {
                Buyer buyer1 = server.addBuyer(buyer);
                return new AddBuyerResponse(DTOUtils.getDTO(buyer1));
            } catch (ServicesException | RemoteException e) {
                return new ErrorResponse(e.getMessage());
            }
        }

        if (request instanceof GetAllARRequest){
            System.out.println("GetAllAR Request ...");
            GetAllARRequest getAllARRequest = (GetAllARRequest) request;
            try {
                Iterable<ArtisticRepresentation> ars = server.getAllAR();
                Iterable<ArtisticRepresentationDTO> arsDTO = DTOUtils.getDTO(ars);
                return new GetAllARResponse(arsDTO);
            } catch (ServicesException e) {
                return new ErrorResponse(e.getMessage());
            } catch (SQLException | RemoteException throwables) {
                throwables.printStackTrace();
            }
        }

        if (request instanceof GetAllARFromDateRequest){
            System.out.println("GetAllARFromDate Request ...");
            GetAllARFromDateRequest getReq=(GetAllARFromDateRequest) request;
            LocalDateTime date = getReq.getDate();
            try {
                Iterable<ArtisticRepresentation> ars = server.getAllARFromDate(date);
                Iterable<ArtisticRepresentationDTO> arsDTO = DTOUtils.getDTO(ars);
                return new GetAllARFromDateResponse(arsDTO);
            } catch (ServicesException e) {
                return new ErrorResponse(e.getMessage());
            } catch (SQLException | RemoteException throwables) {
                throwables.printStackTrace();
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
