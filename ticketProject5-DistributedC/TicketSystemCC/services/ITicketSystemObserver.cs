using model;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace services
{
    public interface ITicketSystemObserver
    {
        void boughtTicket(ArtisticRepresentation ar);

    }
}
