using model;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;


namespace services
{
    public interface ITicketSystemServices

    {
        //void login(User user, IChatObserver client);
        //void sendMessage(Message message);
        //void logout(User user, IChatObserver client);
        //User[] getLoggedFriends(User user);


        Seller login(Seller seller, ITicketSystemObserver client);
        void logout(Seller seller, ITicketSystemObserver client);
        Seller findSellerByUsername(Seller seller);
        IEnumerable<ArtisticRepresentation> getAllAR();
        ArtisticRepresentation updateAR(ArtisticRepresentation artisticRepresentation, ITicketSystemObserver client);
        Buyer addBuyer(Buyer buyer);
        IEnumerable<ArtisticRepresentation> getAllARFromDate(DateTime date);


    }
}
