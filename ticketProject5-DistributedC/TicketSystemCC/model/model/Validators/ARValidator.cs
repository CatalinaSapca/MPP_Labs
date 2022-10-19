using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace model.Validators
{
    /**
     * Validates an instance of class ArtisticRepresentation
     * Throws ValidateException if instance is invalid
     */
    public class ARValidator : Validator<ArtisticRepresentation>
    {
        virtual public void Validate(ArtisticRepresentation entity)
        {
            if (entity == null)
                throw new ArgumentNullException("entity must be not null");
            else
            {
                if (entity.Id <= 0)
                    throw new ValidationException("Invalid ID!");
                if (entity.ArtistName == null)
                    throw new ValidationException("Invalid artist name!");
                if (entity.Data == null)
                    throw new ValidationException("Invalid data!");
                if (entity.Location == null)
                    throw new ValidationException("Invalid location!");
                if (entity.AvailableSeats < 0)
                    throw new ValidationException("Invalid number of available seats!");
                if (entity.SoldSeats < 0)
                    throw new ValidationException("Invalid number of sold seats!");
            }
        }
    }
}
