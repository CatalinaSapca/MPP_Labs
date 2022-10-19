using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace model
{
    public class Entity<ID>
    {
        public ID Id { get; set; }

        public override bool Equals(object o)
        {
            if (this == o) return true;
            Entity<ID> that = (Entity<ID>)o;
            return Id.Equals(that.Id);
        }
    }
}
