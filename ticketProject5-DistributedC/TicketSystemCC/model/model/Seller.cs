using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace model
{

    public class Seller : Entity<long>
    {
        private string firstName { get; set; }
        private string lastName { get; set; }

        private string username { get; set; }
        private string password { get; set; }

        public Seller(string firstName, string lastName, string username, string password)
        {
            this.firstName = firstName;
            this.lastName = lastName;
            this.username = username;
            this.password = password;
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

        public string Username
        {
            get { return username; }
            set { username = value; }
        }

        public string Password
        {
            get { return password; }
            set { password = value; }
        }

        public override string ToString()
        {
            return Id + "," + firstName + "," + lastName;
        }
        public override bool Equals(object o)
        {
            if (this == o) return true;
            Seller that = (Seller)o;
            return firstName.Equals(that.firstName) &
                lastName.Equals(that.lastName) &
                username.Equals(that.username) &
                password.Equals(that.password);
        }

    }
}
