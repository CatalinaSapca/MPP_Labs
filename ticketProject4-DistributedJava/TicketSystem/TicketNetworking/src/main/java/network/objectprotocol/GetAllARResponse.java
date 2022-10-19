package network.objectprotocol;

import network.dto.ArtisticRepresentationDTO;

public class GetAllARResponse implements Response {
    private Iterable<ArtisticRepresentationDTO> arsDTO;

    public GetAllARResponse(Iterable<ArtisticRepresentationDTO> arsDTO) {
        this.arsDTO = arsDTO;
    }

    public Iterable<ArtisticRepresentationDTO> getARs() {
        return arsDTO;
    }
}