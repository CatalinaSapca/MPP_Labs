using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;
using System;
using services;
using persistance;
using persistance.Interfaces;
using model;

namespace Server
{
    public class TicketServerImpl : ITicketSystemServices
    {
        private IARRepository repoAR;
        private IBuyerRepository repoBuyer;
        private ISellerRepository repoSeller;

        private readonly IDictionary<long, ITicketSystemObserver> loggedUsers;



        public TicketServerImpl(IARRepository repoAR, IBuyerRepository repoBuyer, ISellerRepository repoSeller)
        {
            this.repoAR = repoAR;
            this.repoBuyer = repoBuyer;
            this.repoSeller = repoSeller;
            loggedUsers = new Dictionary<long, ITicketSystemObserver>();
        }

        public Seller login(Seller seller, ITicketSystemObserver client)
        {
            Seller s = repoSeller.FindOneByUsername(seller.Username);
            if (s != null)
            {
                if (loggedUsers.ContainsKey(s.Id))
                    throw new ServicesException("User already logged in.");
                else
                {
                    if (seller.Password.Equals(s.Password))
                        loggedUsers[s.Id] = client;
                }
            }
            return s;
        }

        public ArtisticRepresentation updateAR(ArtisticRepresentation artisticRepresentation, ITicketSystemObserver client)
        {
            ArtisticRepresentation ar = repoAR.Update(artisticRepresentation);
            if (ar == null)
            {
                notifyAllUsers(artisticRepresentation);
            }
            return ar;
        }

        public Buyer addBuyer(Buyer buyer)
        {
            Buyer b = repoBuyer.Save(buyer);
            return b;
        }

        public void logout(Seller seller, ITicketSystemObserver client)
        {
            ITicketSystemObserver localClient = loggedUsers[seller.Id];
            if (localClient == null)
                throw new ServicesException("User " + seller.Id + " is not logged in.");
            loggedUsers.Remove(seller.Id);
        }

        public Seller findSellerByUsername(Seller seller)
        {
            return repoSeller.FindOneByUsername(seller.Username);
        }

        public IEnumerable<ArtisticRepresentation> getAllAR()
        {
            Console.WriteLine("sunt in service impl");
            return repoAR.FindAll();
        }

        public IEnumerable<ArtisticRepresentation> getAllARFromDate(DateTime date)
        {
            List<ArtisticRepresentation> all = new List<ArtisticRepresentation>();
            foreach (ArtisticRepresentation x in repoAR.FindAll())
            {
                if (x.Data.Year == date.Year && x.Data.Month == date.Month && x.Data.Day == date.Day)
                    all.Add(x);
            }
            return all;
        }

        private void notifyAllUsers(ArtisticRepresentation ar)
        {
            IEnumerable<Seller> sellers = repoSeller.FindAll();
            Console.WriteLine("Users " + sellers);

            foreach (Seller s in sellers)
            {
                //ITicketSystemObserver ticketClient = loggedUsers[s.Id];
                //if (ticketClient != null)
                //{
                //    ITicketSystemObserver TSClient = loggedUsers[s.Id];
                //    Task.Run(() => TSClient.boughtTicket(ar));
                //}
                if (loggedUsers.ContainsKey(s.Id))
                {
                    ITicketSystemObserver TSClient = loggedUsers[s.Id];
                    Task.Run(() => TSClient.boughtTicket(ar));
                }
            }
        }

    }

}
