package ticket.network.protobuffprotocol;

import services.ServicesException;
import services.TicketSystemObserver;
import services.TicketSystemServices;
import ticket.model.ArtisticRepresentation;
import ticket.model.Buyer;
import ticket.model.Seller;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.rmi.RemoteException;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class ProtoTSProxy implements TicketSystemServices {
    private String host;
      private int port;

      private TicketSystemObserver client;

      private InputStream input;
      private OutputStream output;
      private Socket connection;

      private BlockingQueue<TSProtobufs.TSResponse> qresponses;
      private volatile boolean finished;
      public ProtoTSProxy(String host, int port) {
          this.host = host;
          this.port = port;
          qresponses=new LinkedBlockingQueue<TSProtobufs.TSResponse>();
      }

      public Seller login(Seller user, TicketSystemObserver client) throws ServicesException {
          initializeConnection();
          sendRequest(ProtoUtils.createLoginRequest(user));
          TSProtobufs.TSResponse response = readResponse();
          if (response.getType()== TSProtobufs.TSResponse.Type.Ok){
              this.client=client;
              Seller seller1 = ProtoUtils.getSeller(response);
              return seller1;
          }
          if (response.getType()== TSProtobufs.TSResponse.Type.Login){
              this.client=client;
              Seller seller1 = ProtoUtils.getSeller(response);
              return seller1;
          }
          if (response.getType()== TSProtobufs.TSResponse.Type.Error){
              String errorText=ProtoUtils.getError(response);
              closeConnection();
              throw new ServicesException(errorText);
          }
          return user;
      }

    public void logout(Seller seller, TicketSystemObserver client) throws ServicesException {
        sendRequest(ProtoUtils.createLogoutRequest(seller));
        TSProtobufs.TSResponse response = readResponse();
        closeConnection();
        if (response.getType()== TSProtobufs.TSResponse.Type.Error){
            String errorText=ProtoUtils.getError(response);
            throw new ServicesException(errorText);
        }
    }

    public Buyer addBuyer(Buyer buyer) throws ServicesException {
        sendRequest(ProtoUtils.createAddBuyerRequest(buyer));
        TSProtobufs.TSResponse response = readResponse();

        if (response.getType()== TSProtobufs.TSResponse.Type.AddBuyer){
            //this.client=client;
            Buyer buyer1 = ProtoUtils.getBuyer(response);
            return buyer1;
        }
        if (response.getType()== TSProtobufs.TSResponse.Type.Ok){
            //this.client=client;
//            Buyer buyer1 = ProtoUtils.getBuyer(response);
//            return buyer1;
            return null;
        }
        if (response.getType()== TSProtobufs.TSResponse.Type.Error){
            String errorText=ProtoUtils.getError(response);
            closeConnection();
            throw new ServicesException(errorText);
        }
        return buyer;
    }

    public ArtisticRepresentation updateAR(ArtisticRepresentation artisticRepresentation, TicketSystemObserver client) throws ServicesException {
        sendRequest(ProtoUtils.createBuyTicketRequest(artisticRepresentation));
        TSProtobufs.TSResponse response = readResponse();

        if (response.getType()== TSProtobufs.TSResponse.Type.BuyTicket){
            //this.client=client;
            List<ArtisticRepresentation> ar1 = (List<ArtisticRepresentation>) ProtoUtils.getArtisticRepresentations(response);
            return ar1.get(0);
        }
        if (response.getType()== TSProtobufs.TSResponse.Type.Ok){
            //this.client=client;
            //ArtisticRepresentation ar1 = ProtoUtils.getArtisticRepresentation(response);
            //return ar1;
            return null;
        }
        if (response.getType()== TSProtobufs.TSResponse.Type.Error){
            String errorText=ProtoUtils.getError(response);
            closeConnection();
            throw new ServicesException(errorText);
        }
        return artisticRepresentation;
    }

    public Seller findSellerByUsername(Seller seller) throws ServicesException {
        sendRequest(ProtoUtils.createFindSellerByUsernameRequest(seller));
        TSProtobufs.TSResponse response = readResponse();

        if (response.getType()== TSProtobufs.TSResponse.Type.FindSellerByUsername){
            Seller seller1 = ProtoUtils.getSeller(response);
            return seller1;
        }
        if (response.getType()== TSProtobufs.TSResponse.Type.Error){
            String errorText=ProtoUtils.getError(response);
            closeConnection();
            throw new ServicesException(errorText);
        }
        return seller;
    }

    public Iterable<ArtisticRepresentation> getAllAR() throws ServicesException {
        sendRequest(ProtoUtils.createGetAllARRequest());
        TSProtobufs.TSResponse response = readResponse();
        if (response.getType()== TSProtobufs.TSResponse.Type.Error){
            String errorText=ProtoUtils.getError(response);
            closeConnection();
            throw new ServicesException(errorText);
        }
        Iterable<ArtisticRepresentation> ars =  ProtoUtils.getArtisticRepresentations(response);
        return ars;
    }

    public Iterable<ArtisticRepresentation> getAllARFromDate(LocalDateTime date) throws ServicesException {
        sendRequest(ProtoUtils.createFGetAllARFRomDateRequest(date));
        TSProtobufs.TSResponse response = readResponse();
        if (response.getType()== TSProtobufs.TSResponse.Type.Error){
            String errorText=ProtoUtils.getError(response);
            closeConnection();
            throw new ServicesException(errorText);
        }
        Iterable<ArtisticRepresentation> ars =  ProtoUtils.getArtisticRepresentations(response);
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

      private void sendRequest(TSProtobufs.TSRequest request)throws ServicesException{
          try {
              System.out.println("Sending request ..." + request);
              //request.writeTo(output);
              request.writeDelimitedTo(output);
              output.flush();
              System.out.println("Request sent.");
          } catch (IOException e) {
              throw new ServicesException("Error sending object "+e);
          }

      }

      private TSProtobufs.TSResponse readResponse() throws ServicesException{
          TSProtobufs.TSResponse response=null;
          try{
              response=qresponses.take();

          } catch (InterruptedException e) {
              e.printStackTrace();
          }
          return response;
      }

      private void initializeConnection() throws ServicesException{
           try {
              connection=new Socket(host,port);
              output=connection.getOutputStream();
              //output.flush();
              input=connection.getInputStream();     //new ObjectInputStream(connection.getInputStream());
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


      private void handleUpdate(TSProtobufs.TSResponse updateResponse){
          switch (updateResponse.getType()){
              case BoughtTicket: {
                  List<ArtisticRepresentation> ar = (List<ArtisticRepresentation>) ProtoUtils.getArtisticRepresentations(updateResponse);
                  System.out.println("Someone bought tickets " + ar.get(0));
                  try {
                      client.boughtTicket(ar.get(0));
                  } catch (ServicesException | SQLException | RemoteException e) {
                      e.printStackTrace();
                  }
                  break;
              }
          }

      }
      private class ReaderThread implements Runnable{
          public void run() {
              while(!finished){
                  try {
                      TSProtobufs.TSResponse response = TSProtobufs.TSResponse.parseDelimitedFrom(input);
                      System.out.println("response received "+response);

                      if (isUpdateResponse(response.getType())){
                           handleUpdate(response);
                      }else{
                          try {
                              qresponses.put(response);
                          } catch (InterruptedException e) {
                              e.printStackTrace();
                          }
                      }
                  } catch (IOException e) {
                      System.out.println("Reading error "+e);
                  }
              }
          }
      }

    private boolean isUpdateResponse(TSProtobufs.TSResponse.Type type){
        switch (type){
            case BoughtTicket:  return true;
        }
        return false;
    }
}
