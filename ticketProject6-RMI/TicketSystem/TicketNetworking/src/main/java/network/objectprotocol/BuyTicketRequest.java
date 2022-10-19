package network.objectprotocol;

import network.dto.ArtisticRepresentationDTO;

public class BuyTicketRequest implements Request{
    private ArtisticRepresentationDTO arDTO;

    public BuyTicketRequest(ArtisticRepresentationDTO arDTO) {
        this.arDTO = arDTO;
    }

    public ArtisticRepresentationDTO getArDTO() {
        return arDTO;
    }
}

