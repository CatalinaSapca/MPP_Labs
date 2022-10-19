using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace model
{
    public class Buyer : Entity<long>
    {
        private long idAR { get; set; }
        private string firstName { get; set; }
        private string lastName { get; set; }
        private long noTickets { get; set; }

        public Buyer(long idAR, string firstName, string lastName, long noTickets)
        {
            this.idAR = idAR;
            this.firstName = firstName;
            this.lastName = lastName;
            this.noTickets = noTickets;
        }

        public long IdAR
        {
            get { return idAR; }
            set { idAR = value; }
        }

        public string FirstName
        {
            get { return firstName; }
            set { firstName = value; }
        }

        public string LastName
        {
            get { return lastName; }
            set { lastName = value; }
        }

        public long NoTickets
        {
            get { return noTickets; }
            set { noTickets = value; }
        }

        public override string ToString()
        {
            return idAR + "," + firstName + "," + lastName;
        }
        public override bool Equals(object o)
        {
            if (this == o) return true;
            Buyer that = (Buyer)o;
            return idAR.Equals(that.idAR) &
                firstName.Equals(that.firstName) &
                lastName.Equals(that.lastName) &
                noTickets.Equals(that.noTickets);
        }

    }
}
