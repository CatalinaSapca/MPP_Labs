package network.objectprotocol;

import network.dto.SellerDTO;

public class LoginRequest implements Request {
    private SellerDTO seller;

    public LoginRequest(SellerDTO seller) {
        this.seller = seller;
    }

    public SellerDTO getSeller() {
        return seller;
    }
}
