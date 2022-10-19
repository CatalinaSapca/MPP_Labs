package network.objectprotocol;

import network.dto.ArtisticRepresentationDTO;

public class BoughtTicketResponse implements Response {
    private ArtisticRepresentationDTO ar;

    public BoughtTicketResponse(ArtisticRepresentationDTO ar) {
        this.ar = ar;
    }

    public ArtisticRepresentationDTO getAR() {
        return ar;
    }
}
