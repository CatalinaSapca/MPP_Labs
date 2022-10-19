package ticket.network.dto;


import ticket.model.ArtisticRepresentation;
import ticket.model.Buyer;
import ticket.model.Seller;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class DTOUtils {

    private static LocalDateTime fromStringToLocalDateTime(String string){
        LocalDateTime data=LocalDateTime.now();
        try{
            data = LocalDateTime.parse(string);
        }
        catch (Exception ex){
            String[] arr = string.split(" ");
            String[] arrDate = arr[0].split("-");
            String[] arrTime = arr[1].split(":");

            data = LocalDateTime.of(Integer.parseInt(arrDate[0]),Integer.parseInt(arrDate[1]), Integer.parseInt(arrDate[2]), Integer.parseInt(arrTime[0]), Integer.parseInt(arrTime[1]));
        }
        return data;
    }

    public static ArtisticRepresentation getFromDTO(ArtisticRepresentationDTO arDTO){
        if(arDTO == null)
            return null;

        String id = arDTO.getId();
        String artistName = arDTO.getArtistName();
        String data = arDTO.getData();
        String location = arDTO.getLocation();
        String  availableSeats = arDTO.getAvailableSeats();
        String soldSeats = arDTO.getSoldSeats();

        ArtisticRepresentation a = new ArtisticRepresentation(artistName, fromStringToLocalDateTime(data), location, Long.parseLong(availableSeats), Long.parseLong(soldSeats));
        a.setId(Long.parseLong(id));
        return a;
    }

    public static ArtisticRepresentationDTO getDTO(ArtisticRepresentation ar){
        if(ar == null)
            return null;
        String id = ar.getId().toString();
        String artistName = ar.getArtistName();
        String data = ar.getData().toString();
        String location = ar.getLocation();
        String availableSeats = ar.getAvailableSeats().toString();
        String soldSeats = ar.getSoldSeats().toString();
        return new ArtisticRepresentationDTO(id, artistName, data, location, availableSeats, soldSeats);
    }

    public static Buyer getFromDTO(BuyerDTO buyerDTO){
        if (buyerDTO == null)
            return null;

        String idAR = buyerDTO.getARId();
        String firstName = buyerDTO.getFirstName();
        String lastName = buyerDTO.getLastName();
        String noTickets = buyerDTO.getNoTickets();

        return new Buyer(Long.parseLong(idAR), firstName, lastName, Long.parseLong(noTickets));

    }
    public static BuyerDTO getDTO(Buyer buyer){
        if(buyer == null)
            return null;

        String idAR = buyer.getIdAR().toString();
        String firstName = buyer.getFirstName();
        String lastName = buyer.getLastName();
        String noTickets = buyer.getNoTickets().toString();

        return new BuyerDTO(idAR, firstName, lastName, noTickets);
    }

    public static Seller getFromDTO(SellerDTO sellerDTO){
        if(sellerDTO == null)
            return null;

        String id = sellerDTO.getId();
        String firstName = sellerDTO.getFirstName();
        String lastName = sellerDTO.getLastName();
        String username = sellerDTO.getUsername();
        String password = sellerDTO.getPassword();

        Seller s = new Seller(firstName, lastName, username, password);
        s.setId(Long.parseLong(id));

        return s;

    }
    public static SellerDTO getDTO(Seller seller){
        if(seller == null)
            return null;

        String id = seller.getId().toString();
        String firstName = seller.getFirstName();
        String lastName = seller.getLastName();
        String username = seller.getUsername();
        String password = seller.getPassword();

        return new SellerDTO(id, firstName, lastName, username, password);
    }

    public static Iterable<ArtisticRepresentationDTO> getDTO(Iterable<ArtisticRepresentation> ars){
        List<ArtisticRepresentationDTO> arsDTO=new ArrayList<>();
        for (ArtisticRepresentation a: ars){
            arsDTO.add(getDTO(a));
        }
        return arsDTO;
    }

    public static Iterable<ArtisticRepresentation> getFromDTO(Iterable<ArtisticRepresentationDTO> arsDTO){
        List<ArtisticRepresentation> ars=new ArrayList<>();
        for (ArtisticRepresentationDTO a: arsDTO){
            ars.add(getFromDTO(a));
        }
        return ars;
    }
}
