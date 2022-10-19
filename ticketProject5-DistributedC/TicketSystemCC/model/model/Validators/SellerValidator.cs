using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace model.Validators
{
    /**
     * Validates an instance of class Seller
     * Throws ValidateException if instance is invalid
     */
    public class SellerValidator : Validator<Seller>
    {
        virtual public void Validate(Seller entity)
        {
            if (entity == null)
                throw new ArgumentNullException("entity must be not null");
            else
            {
                if (entity.Id <= 0)
                    throw new ValidationException("Invalid ID!");
                if (entity.FirstName == null)
                    throw new ValidationException("Invalid first name!");
                if (entity.LastName == null)
                    throw new ValidationException("Invalid last name!");
                if (entity.Username == null)
                    throw new ValidationException("Invalid username!");
                if (entity.Password == null)
                    throw new ValidationException("Invalid password!");
            }
        }
    }
}
