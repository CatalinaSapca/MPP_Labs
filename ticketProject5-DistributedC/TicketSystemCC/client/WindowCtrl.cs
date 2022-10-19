using System;
using System.Collections.Generic;
using System.Windows.Forms;
using model;
using services;

namespace client
{
    public class WindowCtrl : ITicketSystemObserver
    {
        public event EventHandler<UserEventArgs> updateEvent; //ctrl calls it when it has received an update
        private readonly ITicketSystemServices server;
        private Seller currentSeller;
        public WindowCtrl(ITicketSystemServices server)
        {
            this.server = server;
            currentSeller = null;
        }

        public void setCurrentSeller(Seller seller)
        {
            this.currentSeller = seller;
        }

        public Seller getCurrentSeller()
        {
            return currentSeller;
        }

        public void boughtTicket(ArtisticRepresentation ar)
        {
            Console.WriteLine("Someone bought tickets " + ar);
            UserEventArgs userArgs = new UserEventArgs(TSUserEvent.BoughtTicket, ar);
            OnUserEvent(userArgs);
        }

        protected virtual void OnUserEvent(UserEventArgs e)
        {
            if (updateEvent == null) return;
            updateEvent(this, e);
            Console.WriteLine("Update Event called");
        }

        public Seller login(String username, String password)
        {
            Seller s = new Seller("firstName", "lastName", username, password)
            {
                Id = 1
            };
           
            Seller loggedSeller = this.server.login(s, this);
            currentSeller = loggedSeller;
            Console.WriteLine("Login succeeded ....");
            return loggedSeller;
        }

        public void logout()
        {
            try
            {
                server.logout(this.currentSeller, this);
                this.currentSeller = null;
            }
            catch (ServicesException e)
            {
                Console.WriteLine("Logout error " + e);
            }

        }

        public IEnumerable<ArtisticRepresentation> getAllAR()
        {
            IEnumerable<ArtisticRepresentation> ars = server.getAllAR();
            return ars;
        }

        public IEnumerable<ArtisticRepresentation> getAllARFromDate(DateTime data)
        {
            IEnumerable<ArtisticRepresentation> ars = server.getAllARFromDate(data);
            return ars;
        }

        public ArtisticRepresentation updateAR(ArtisticRepresentation ar)
        {
            ArtisticRepresentation aa = server.updateAR(ar, this);
            UserEventArgs userArgs = new UserEventArgs(TSUserEvent.BoughtTicket, ar);
            OnUserEvent(userArgs);
            return aa;
        }

        public Buyer addBuyer(Buyer buyer)
        {
            return server.addBuyer(buyer);
        }

    }
}
