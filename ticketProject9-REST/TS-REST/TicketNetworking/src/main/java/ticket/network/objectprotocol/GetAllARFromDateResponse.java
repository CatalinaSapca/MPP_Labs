package ticket.network.objectprotocol;

import ticket.network.dto.ArtisticRepresentationDTO;

public class GetAllARFromDateResponse implements Response {
    private Iterable<ArtisticRepresentationDTO> ARs;

    public GetAllARFromDateResponse(Iterable<ArtisticRepresentationDTO> ARs) {
        this.ARs = ARs;
    }

    public Iterable<ArtisticRepresentationDTO> getARs() { return ARs; }


}
