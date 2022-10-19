package ticket.network.protobuffprotocol;

import services.ServicesException;
import services.TicketSystemObserver;
import services.TicketSystemServices;
import ticket.model.ArtisticRepresentation;
import ticket.model.Buyer;
import ticket.model.Seller;
import ticket.network.dto.ArtisticRepresentationDTO;
import ticket.network.dto.DTOUtils;
import ticket.network.rpcprotocol.Response;
import ticket.network.rpcprotocol.ResponseType;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.rmi.RemoteException;
import java.sql.SQLException;


public class ProtoTSWorker implements Runnable, TicketSystemObserver {
    private TicketSystemServices server;
     private Socket connection;

     private InputStream input;
     private OutputStream output;
     private volatile boolean connected;
     public ProtoTSWorker(TicketSystemServices server, Socket connection) {
         this.server = server;
         this.connection = connection;
         try{
             output = connection.getOutputStream() ;//new ObjectOutputStream(connection.getOutputStream());
             input = connection.getInputStream(); //new ObjectInputStream(connection.getInputStream());
             connected = true;
         } catch (IOException e) {
             e.printStackTrace();
         }
     }

     public void run() {
         while(connected){
             try {
                // Object request=input.readObject();
                 System.out.println("Waiting requests ...");
                 TSProtobufs.TSRequest request = TSProtobufs.TSRequest.parseDelimitedFrom(input);
                 System.out.println("Request received: "+request);
                 TSProtobufs.TSResponse response = handleRequest(request);
                 if (response!=null){
                    sendResponse(response);
                 }
             } catch (IOException e) {
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
        System.out.println("Someone bought tickets " + artisticRepresentation);
        try {
            sendResponse(ProtoUtils.createBoughtTicketResponse(artisticRepresentation));
        } catch (IOException e) {
            throw new ServicesException("Sending error: " + e);
        }
    }

     private TSProtobufs.TSResponse handleRequest(TSProtobufs.TSRequest request){
         TSProtobufs.TSResponse response = null;
         switch (request.getType()){
             case Login:{
                 System.out.println("Login request ...");
                 Seller seller = ProtoUtils.getSeller(request);
                 try {
                     Seller seller1 = server.login(seller, this);
                     return ProtoUtils.createLoginResponse(seller1);
                 } catch (ServicesException | RemoteException e) {
                     connected=false;
                     return ProtoUtils.createErrorResponse(e.getMessage());
                 }
             }
             case Logout:{
                 System.out.println("Logout request");
                 Seller seller = ProtoUtils.getSeller(request);
                 try {
                     server.logout(seller, this);
                     connected=false;
                     return ProtoUtils.createLogoutResponse();

                 } catch (ServicesException | RemoteException e) {
                     return ProtoUtils.createErrorResponse(e.getMessage());
                 }
             }
             case AddBuyer:{
                 System.out.println("AddBuyerRequest ...");
                 Buyer buyer = ProtoUtils.getBuyer(request);
                 try {
                     Buyer buyer1 = server.addBuyer(buyer);
                     return ProtoUtils.createAddBuyerResponse(buyer1);
                 } catch (ServicesException | RemoteException e) {
                     return ProtoUtils.createErrorResponse(e.getMessage());
                 }
             }
             case BuyTicket:{
                 System.out.println("BuyTicketRequest ...");
                 ArtisticRepresentation ar = ProtoUtils.getArtisticRepresentation(request);
                 try {
                     ArtisticRepresentation artisticRepresentation1 = server.updateAR(ar, this);
                     return ProtoUtils.createBuyTicketResponse(artisticRepresentation1);
                 } catch (ServicesException e) {
                     return ProtoUtils.createErrorResponse(e.getMessage());
                 } catch (SQLException | RemoteException throwables) {
                     throwables.printStackTrace();
                 }
             }
             case GetAllAR:{System.out.println("GetAllARRequest ...");
                 try {
                     Iterable<ArtisticRepresentation> ars = server.getAllAR();
                     return ProtoUtils.createGetAllARResponse(ars);
                 } catch (ServicesException e) {
                     return ProtoUtils.createErrorResponse(e.getMessage());
                 } catch (SQLException | RemoteException throwables) {
                     throwables.printStackTrace();
                 }
             }
             case GetAllARFromDate:{
                 ArtisticRepresentation ar = ProtoUtils.getArtisticRepresentation(request);
                 try {
                     Iterable<ArtisticRepresentation> ars = server.getAllARFromDate(ar.getData());
                     return ProtoUtils.createGetAllARFromDateResponse(ars);
                 } catch (ServicesException e) {
                     return ProtoUtils.createErrorResponse(e.getMessage());
                 } catch (SQLException | RemoteException throwables) {
                     throwables.printStackTrace();
                 }
             }
             case FindSellerByUsername:{
                 Seller seller = ProtoUtils.getSeller(request);
                 try {
                     Seller seller1 = server.findSellerByUsername(seller);
                     return ProtoUtils.createFindSellerByUsernameResponse(seller1);
                 } catch (ServicesException e) {
                     return ProtoUtils.createErrorResponse(e.getMessage());
                 } catch (RemoteException throwables) {
                     throwables.printStackTrace();
                 }
             }



         }
         return response;
     }

     private void sendResponse(TSProtobufs.TSResponse response) throws IOException{
         System.out.println("sending response "+response);
         response.writeDelimitedTo(output);
         //output.writeObject(response);
         output.flush();
     }
}
