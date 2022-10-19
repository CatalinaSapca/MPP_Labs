package network.objectprotocol;

import network.dto.BuyerDTO;

public class AddBuyerResponse implements Response {
    private BuyerDTO buyer;

    public AddBuyerResponse(BuyerDTO buyer) {
        this.buyer = buyer;
    }

    public BuyerDTO getBuyer() {
        return buyer;
    }
}
