package network.objectprotocol;

import network.dto.ArtisticRepresentationDTO;

public class BuyTicketResponse implements Response {
    private ArtisticRepresentationDTO ar;

    public BuyTicketResponse(ArtisticRepresentationDTO ar) { this.ar = ar; }

    public ArtisticRepresentationDTO getAr() {
        return ar;
    }
}