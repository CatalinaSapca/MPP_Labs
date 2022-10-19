package ticket.network.objectprotocol;

import ticket.network.dto.BuyerDTO;

public class AddBuyerRequest implements Request{
    private BuyerDTO buyerDTO;

    public AddBuyerRequest(BuyerDTO buyerDTO) {
        this.buyerDTO = buyerDTO;
    }

    public BuyerDTO getBuyer() {
        return buyerDTO;
    }
}

