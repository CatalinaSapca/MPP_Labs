package ticket.network.objectprotocol;

import ticket.network.dto.SellerDTO;

public class LoginResponse implements Response {
    private SellerDTO seller;

    public LoginResponse(SellerDTO seller) {
        this.seller = seller;
    }

    public SellerDTO getSeller() {
        return seller;
    }
}
