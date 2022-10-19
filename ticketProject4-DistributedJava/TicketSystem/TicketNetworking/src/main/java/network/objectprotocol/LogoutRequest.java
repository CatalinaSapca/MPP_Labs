package network.objectprotocol;


import network.dto.SellerDTO;

public class LogoutRequest implements Request {
    private SellerDTO seller;

    public LogoutRequest(SellerDTO seller) { this.seller = seller; }

    public SellerDTO getSeller() {
        return seller;
    }
}
