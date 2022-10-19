using model;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace persistance.Interfaces
{
    public interface ISellerRepository : Repository<long, Seller>
    {
        Seller FindOneByUsername(String username);
    }
}
