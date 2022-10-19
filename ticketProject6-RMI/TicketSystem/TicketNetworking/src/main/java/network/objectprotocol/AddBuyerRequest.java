package network.objectprotocol;

import network.dto.BuyerDTO;

public class AddBuyerRequest implements Request{
    private BuyerDTO buyerDTO;

    public AddBuyerRequest(BuyerDTO buyerDTO) {
        this.buyerDTO = buyerDTO;
    }

    public BuyerDTO getBuyer() {
        return buyerDTO;
    }
}

