using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace model.Validators
{
    /**
     * Validates an instance of class Buyer
     * Throws ValidateException if instance is invalid
     */
    public class BuyerValidator : Validator<Buyer>
    {
        virtual public void Validate(Buyer entity)
        {
            if (entity == null)
                throw new ArgumentNullException("entity must be not null");
            else
            {
                if (entity.Id <= 0)
                    throw new ValidationException("Invalid ID!");
                if (entity.IdAR <= 0)
                    throw new ValidationException("Invalid IDAR!");
                if (entity.FirstName == null)
                    throw new ValidationException("Invalid first name!");
                if (entity.LastName == null)
                    throw new ValidationException("Invalid last name!");
                if (entity.NoTickets == 0)
                    throw new ValidationException("Invalid username!");
            }
        }
    }
}
