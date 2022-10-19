package ticket.persistance;

import ticket.model.Seller;

public interface SellerRepository extends Repository<Long, Seller> {
    public Seller findOneByUsername(String username);
}
