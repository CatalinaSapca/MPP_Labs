package ticket.network.protobuffprotocol;

import ticket.model.ArtisticRepresentation;
import ticket.model.Buyer;
import ticket.model.Seller;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ProtoUtils {
    public static TSProtobufs.TSRequest createLoginRequest(Seller user){
        TSProtobufs.Seller userDTO = TSProtobufs.Seller.newBuilder().setId(user.getId().toString()).setFirstName(user.getFirstName()).setLastName(user.getLastName()).setUsername(user.getUsername()).setPassword(user.getPassword()).build();
        TSProtobufs.TSRequest request= TSProtobufs.TSRequest.newBuilder().setType(TSProtobufs.TSRequest.Type.Login)
                .setSeller(userDTO).build();
        return request;
    }
    public static TSProtobufs.TSRequest createLogoutRequest(Seller user){
        TSProtobufs.Seller userDTO= TSProtobufs.Seller.newBuilder().setId(user.getId().toString()).setFirstName(user.getFirstName()).setLastName(user.getLastName()).setUsername(user.getUsername()).setPassword(user.getPassword()).build();
        TSProtobufs.TSRequest request= TSProtobufs.TSRequest.newBuilder().setType(TSProtobufs.TSRequest.Type.Logout)
                .setSeller(userDTO).build();
        return request;
    }

    public static TSProtobufs.TSRequest createAddBuyerRequest(Buyer buyer){
        TSProtobufs.Buyer buyerDTO = TSProtobufs.Buyer.newBuilder().setId(buyer.getId().toString()).setFirstName(buyer.getFirstName()).setLastName(buyer.getLastName()).setIdAR(buyer.getIdAR().toString()).setNoTickets(buyer.getNoTickets().toString()).build();
        TSProtobufs.TSRequest request= TSProtobufs.TSRequest.newBuilder().setType(TSProtobufs.TSRequest.Type.AddBuyer)
                .setBuyer(buyerDTO).build();
        return request;
    }

    public static TSProtobufs.TSRequest createBuyTicketRequest(ArtisticRepresentation artisticRepresentation){
        TSProtobufs.ArtisticRepresentation arDTO = TSProtobufs.ArtisticRepresentation.newBuilder().setId(artisticRepresentation.getId().toString()).setArtistName(artisticRepresentation.getArtistName()).setData(artisticRepresentation.getData().toString()).setLocation(artisticRepresentation.getLocation()).setAvailableSeats(artisticRepresentation.getAvailableSeats().toString()).setSoldSeats(artisticRepresentation.getSoldSeats().toString()).build();
        TSProtobufs.TSRequest request = TSProtobufs.TSRequest.newBuilder().setType(TSProtobufs.TSRequest.Type.BuyTicket)
                .setArtisticRepresentation(arDTO).build();
        return request;
    }

    public static TSProtobufs.TSRequest createFindSellerByUsernameRequest(Seller seller){
        TSProtobufs.Seller userDTO = TSProtobufs.Seller.newBuilder().setId(seller.getId().toString()).setFirstName(seller.getFirstName()).setLastName(seller.getLastName()).setUsername(seller.getUsername()).setPassword(seller.getPassword()).build();
        TSProtobufs.TSRequest request = TSProtobufs.TSRequest.newBuilder().setType(TSProtobufs.TSRequest.Type.FindSellerByUsername)
                .setSeller(userDTO).build();
        return request;
    }

    public static TSProtobufs.TSRequest createGetAllARRequest(){
        TSProtobufs.TSRequest request = TSProtobufs.TSRequest.newBuilder().setType(TSProtobufs.TSRequest.Type.GetAllAR)
                .build();
        return request;
    }

    public static TSProtobufs.TSRequest createFGetAllARFRomDateRequest(LocalDateTime data){
        TSProtobufs.ArtisticRepresentation arDTO= TSProtobufs.ArtisticRepresentation.newBuilder().setId("1").setArtistName("sss").setData(data.toString()).setLocation("sdvg").setAvailableSeats("1").setSoldSeats("1").build();
        TSProtobufs.TSRequest request = TSProtobufs.TSRequest.newBuilder().setType(TSProtobufs.TSRequest.Type.GetAllARFromDate)
                .setArtisticRepresentation(arDTO).build();
        return request;
    }





    public static TSProtobufs.TSResponse createOkResponse(){
        TSProtobufs.TSResponse response= TSProtobufs.TSResponse.newBuilder()
                .setType(TSProtobufs.TSResponse.Type.Ok).build();
        return response;
    }

    public static TSProtobufs.TSResponse createErrorResponse(String text){
        TSProtobufs.TSResponse response= TSProtobufs.TSResponse.newBuilder()
                .setType(TSProtobufs.TSResponse.Type.Error)
                .setError(text).build();
        return response;
    }

    public static TSProtobufs.TSResponse createLoginResponse(Seller seller){
        TSProtobufs.Seller sellerDTO = TSProtobufs.Seller.newBuilder().setId(seller.getId().toString()).setFirstName(seller.getFirstName()).setLastName(seller.getLastName()).setUsername(seller.getUsername()).setPassword(seller.getPassword()).build();

        TSProtobufs.TSResponse response = TSProtobufs.TSResponse.newBuilder()
                .setType(TSProtobufs.TSResponse.Type.Login)
                .setSeller(sellerDTO).build();
        return response;
    }

    public static TSProtobufs.TSResponse createLogoutResponse(){
        //TSProtobufs.Seller sellerDTO = TSProtobufs.Seller.newBuilder().setId(seller.getId().toString()).setFirstName(seller.getFirstName()).setLastName(seller.getLastName()).setUsername(seller.getUsername()).setPassword(seller.getPassword()).build();

        TSProtobufs.TSResponse response = TSProtobufs.TSResponse.newBuilder()
                .setType(TSProtobufs.TSResponse.Type.Logout)
                .build();
        return response;
    }

    public static TSProtobufs.TSResponse createBoughtTicketResponse(ArtisticRepresentation artisticRepresentation){
//        TSProtobufs.ArtisticRepresentation arDTO = TSProtobufs.ArtisticRepresentation.newBuilder().setId(artisticRepresentation.getId().toString()).setArtistName(artisticRepresentation.getArtistName()).setData(artisticRepresentation.getData().toString()).setLocation(artisticRepresentation.getLocation()).setAvailableSeats(artisticRepresentation.getAvailableSeats().toString()).setSoldSeats(artisticRepresentation.getSoldSeats().toString()).build();
//
//        TSProtobufs.TSResponse response = TSProtobufs.TSResponse.newBuilder()
//                .setType(TSProtobufs.TSResponse.Type.BoughtTicket)
//                .setArtisticRepresentation(0, arDTO).build();
//        return response;
        TSProtobufs.TSResponse.Builder response = TSProtobufs.TSResponse.newBuilder()
                .setType(TSProtobufs.TSResponse.Type.BoughtTicket);


            TSProtobufs.ArtisticRepresentation arDTO = TSProtobufs.ArtisticRepresentation.newBuilder().setId(artisticRepresentation.getId().toString()).setArtistName(artisticRepresentation.getArtistName()).setData(artisticRepresentation.getData().toString()).setLocation(artisticRepresentation.getLocation()).setAvailableSeats(artisticRepresentation.getAvailableSeats().toString()).setSoldSeats(artisticRepresentation.getSoldSeats().toString()).build();
            response.addAllArtisticRepresentation(Collections.singleton(arDTO));


        return response.build();
    }

    public static TSProtobufs.TSResponse createAddBuyerResponse(Buyer buyer){
        if(buyer == null)
        {
            TSProtobufs.TSResponse response = TSProtobufs.TSResponse.newBuilder()
                    .setType(TSProtobufs.TSResponse.Type.Ok)
                    .build();
            return response;
        }
        else {
            TSProtobufs.Buyer buyerDTO = TSProtobufs.Buyer.newBuilder().setId(buyer.getId().toString()).setIdAR(buyer.getIdAR().toString()).setFirstName(buyer.getFirstName()).setLastName(buyer.getLastName()).setNoTickets(buyer.getNoTickets().toString()).build();

            TSProtobufs.TSResponse response = TSProtobufs.TSResponse.newBuilder()
                    .setType(TSProtobufs.TSResponse.Type.AddBuyer)
                    .setBuyer(buyerDTO).build();
            return response;
        }
    }

    public static TSProtobufs.TSResponse createBuyTicketResponse(ArtisticRepresentation artisticRepresentation){
        if(artisticRepresentation == null) {
            TSProtobufs.ArtisticRepresentation arDTO = null;
            TSProtobufs.TSResponse response = TSProtobufs.TSResponse.newBuilder()
                    .setType(TSProtobufs.TSResponse.Type.Ok)
                   .build();
            return response;
        }
        else
        {
            TSProtobufs.ArtisticRepresentation arDTO = TSProtobufs.ArtisticRepresentation.newBuilder().setId(artisticRepresentation.getId().toString()).setArtistName(artisticRepresentation.getArtistName()).setData(artisticRepresentation.getData().toString()).setLocation(artisticRepresentation.getLocation()).setAvailableSeats(artisticRepresentation.getAvailableSeats().toString()).setSoldSeats(artisticRepresentation.getSoldSeats().toString()).build();

            TSProtobufs.TSResponse response = TSProtobufs.TSResponse.newBuilder()
                    .setType(TSProtobufs.TSResponse.Type.BuyTicket)
                    .setArtisticRepresentation(0, arDTO).build();
            return response;
        }
    }

    public static TSProtobufs.TSResponse createGetAllARResponse(Iterable<ArtisticRepresentation> ars){
        TSProtobufs.TSResponse.Builder response = TSProtobufs.TSResponse.newBuilder()
                .setType(TSProtobufs.TSResponse.Type.GetAllAR);

        for(ArtisticRepresentation ar : ars){
            TSProtobufs.ArtisticRepresentation arDTO = TSProtobufs.ArtisticRepresentation.newBuilder().setId(ar.getId().toString()).setArtistName(ar.getArtistName()).setData(ar.getData().toString()).setLocation(ar.getLocation()).setAvailableSeats(ar.getAvailableSeats().toString()).setSoldSeats(ar.getSoldSeats().toString()).build();
            response.addAllArtisticRepresentation(Collections.singleton(arDTO));
        }

        return response.build();
    }

    public static TSProtobufs.TSResponse createGetAllARFromDateResponse(Iterable<ArtisticRepresentation> ars){
        TSProtobufs.TSResponse.Builder response = TSProtobufs.TSResponse.newBuilder()
                .setType(TSProtobufs.TSResponse.Type.GetAllARFromDate);

        for(ArtisticRepresentation ar : ars){
            TSProtobufs.ArtisticRepresentation arDTO = TSProtobufs.ArtisticRepresentation.newBuilder().setId(ar.getId().toString()).setArtistName(ar.getArtistName()).setData(ar.getData().toString()).setLocation(ar.getLocation()).setAvailableSeats(ar.getAvailableSeats().toString()).setSoldSeats(ar.getSoldSeats().toString()).build();
            response.addAllArtisticRepresentation(Collections.singleton(arDTO));
        }

        return response.build();
    }

    public static TSProtobufs.TSResponse createFindSellerByUsernameResponse(Seller seller){
        TSProtobufs.Seller sellerDTO = TSProtobufs.Seller.newBuilder().setId(seller.getId().toString()).setFirstName(seller.getFirstName()).setLastName(seller.getLastName()).setUsername(seller.getUsername()).setPassword(seller.getPassword()).build();

        TSProtobufs.TSResponse response = TSProtobufs.TSResponse.newBuilder()
                .setType(TSProtobufs.TSResponse.Type.FindSellerByUsername)
                .setSeller(sellerDTO).build();
        return response;
    }

    public static String getError(TSProtobufs.TSResponse response){
        String errorMessage=response.getError();
        return errorMessage;
    }

    public static Seller getSeller(TSProtobufs.TSResponse response){
        Seller seller = new Seller();
        seller.setId(Long.parseLong(response.getSeller().getId()));
        seller.setFirstName(response.getSeller().getFirstName());
        seller.setLastName(response.getSeller().getLastName());
        seller.setUsername(response.getSeller().getUsername());
        seller.setPassword(response.getSeller().getPassword());
        return seller;
    }

    public static Buyer getBuyer(TSProtobufs.TSResponse response){
        Buyer buyer = new Buyer(1L,"scd","sdc",2L);
        buyer.setId(Long.parseLong(response.getBuyer().getId()));
        buyer.setIdAR(Long.parseLong(response.getBuyer().getIdAR()));
        buyer.setFirstName(response.getBuyer().getFirstName());
        buyer.setLastName(response.getBuyer().getLastName());
        buyer.setNoTickets(Long.parseLong(response.getBuyer().getNoTickets()));
        return buyer;
    }

    public static ArtisticRepresentation getArtisticRepresentation(TSProtobufs.TSResponse response){
        ArtisticRepresentation ar = new ArtisticRepresentation("1L", LocalDateTime.now(),"sdc",2L, 2L);
        ar.setId(Long.parseLong(response.getArtisticRepresentation(0).getId()));
        ar.setArtistName(response.getArtisticRepresentation(0).getArtistName());
        //ar.setData(LocalDateTime.parse(response.getArtisticRepresentation(0).getData()));
        ar.setData(getDateFromString(response.getArtisticRepresentation(0).getData()));
        ar.setLocation(response.getArtisticRepresentation(0).getLocation());
        ar.setAvailableSeats(Long.parseLong(response.getArtisticRepresentation(0).getAvailableSeats()));
        ar.setSoldSeats(Long.parseLong(response.getArtisticRepresentation(0).getSoldSeats()));
        return ar;
    }

    public static Iterable<ArtisticRepresentation> getArtisticRepresentations(TSProtobufs.TSResponse response){
        List<ArtisticRepresentation> ars = new ArrayList<>();
        for(int i=0;i<response.getArtisticRepresentationCount();i++){
            ArtisticRepresentation ar = new ArtisticRepresentation("1L", LocalDateTime.now(),"sdc",2L, 2L);
            ar.setId(Long.parseLong(response.getArtisticRepresentation(i).getId()));
            ar.setArtistName(response.getArtisticRepresentation(i).getArtistName());
            //ar.setData(LocalDateTime.parse(response.getArtisticRepresentation(i).getData()));
            ar.setData(getDateFromString(response.getArtisticRepresentation(i).getData()));
            ar.setLocation(response.getArtisticRepresentation(i).getLocation());
            ar.setAvailableSeats(Long.parseLong(response.getArtisticRepresentation(i).getAvailableSeats()));
            ar.setSoldSeats(Long.parseLong(response.getArtisticRepresentation(i).getSoldSeats()));

            ars.add(ar);
        }
        return ars;
    }

    public static LocalDateTime getDateFromString(String raw){
        String[] datatime = raw.split(" ");
        String[] data = datatime[0].split("/");
        String[] time = datatime[1].split(":");
        LocalDateTime result = LocalDateTime.of(Integer.parseInt(data[2]), Integer.parseInt(data[1]), Integer.parseInt(data[0]),Integer.parseInt(time[0]), Integer.parseInt(time[1]));
        return result;
    }

    public static Seller getSeller(TSProtobufs.TSRequest request){
        Seller seller = new Seller();
        seller.setId(Long.parseLong(request.getSeller().getId()));
        seller.setFirstName(request.getSeller().getFirstName());
        seller.setLastName(request.getSeller().getLastName());
        seller.setUsername(request.getSeller().getUsername());
        seller.setPassword(request.getSeller().getPassword());
        return seller;
    }

    public static Buyer getBuyer(TSProtobufs.TSRequest request){
        Buyer buyer = new Buyer(1L,"scd","sdc",2L);
        buyer.setId(Long.parseLong(request.getBuyer().getId()));
        buyer.setIdAR(Long.parseLong(request.getBuyer().getIdAR()));
        buyer.setFirstName(request.getBuyer().getFirstName());
        buyer.setLastName(request.getBuyer().getLastName());
        buyer.setNoTickets(Long.parseLong(request.getBuyer().getNoTickets()));
        return buyer;
    }

    public static ArtisticRepresentation getArtisticRepresentation(TSProtobufs.TSRequest request){
        ArtisticRepresentation ar = new ArtisticRepresentation("1L", LocalDateTime.now(),"sdc",2L, 2L);
        ar.setId(Long.parseLong(request.getArtisticRepresentation().getId()));
        ar.setArtistName(request.getArtisticRepresentation().getArtistName());
        ar.setData(LocalDateTime.parse(request.getArtisticRepresentation().getData()));
        ar.setLocation(request.getArtisticRepresentation().getLocation());
        ar.setAvailableSeats(Long.parseLong(request.getArtisticRepresentation().getAvailableSeats()));
        ar.setSoldSeats(Long.parseLong(request.getArtisticRepresentation().getSoldSeats()));
        return ar;
    }




}
