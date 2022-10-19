using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace model
{
    public class ArtisticRepresentation : Entity<long>
    {
        private string artistName { get; set; }
        private DateTime data { get; set; }
        private string location { get; set; }
        private long availableSeats { get; set; }
        private long soldSeats { get; set; }

        public ArtisticRepresentation(string artistName, DateTime data, string location, long availableSeats, long soldSeats)
        {
            this.artistName = artistName;
            this.data = data;
            this.location = location;
            this.availableSeats = availableSeats;
            this.soldSeats = soldSeats;
        }

        public string ArtistName
        {
            get { return artistName; }
            set { artistName = value; }
        }

        public DateTime Data
        {
            get { return data; }
            set { data = value; }
        }

        public string Location
        {
            get { return location; }
            set { location = value; }
        }

        public long AvailableSeats
        {
            get { return availableSeats; }
            set { availableSeats = value; }
        }

        public long SoldSeats
        {
            get { return soldSeats; }
            set { soldSeats = value; }
        }

        public override string ToString()
        {
            return Id + "," + artistName + "," + data;
        }
        public override bool Equals(object o)
        {
            if (this == o) return true;
            ArtisticRepresentation that = (ArtisticRepresentation)o;
            return artistName.Equals(that.artistName) &
                data.Equals(that.data) &
                location.Equals(that.location) &
                availableSeats.Equals(that.availableSeats) &
                soldSeats.Equals(that.soldSeats);
        }

    }
}
