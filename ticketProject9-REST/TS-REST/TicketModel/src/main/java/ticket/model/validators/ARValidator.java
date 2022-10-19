package ticket.model.validators;

import ticket.model.ArtisticRepresentation;

import java.security.InvalidParameterException;


/**
 * Validates an instance of class ArtisticRepresentation
 * Throws ValidateException if instance is invalid
 */
public class ARValidator implements Validator<ArtisticRepresentation> {

    /**
     * @throws InvalidParameterException if id  id is null or <=0 / first name/last name are null.
     */
    @Override
    public void validate(ArtisticRepresentation entity) throws ValidationException {
        if (entity == null)
            throw new InvalidParameterException();
        else {
            if (entity.getId() <= 0)
                throw new ValidationException("Invalid ID!");
            if (entity.getArtistName() == null)
                throw new ValidationException("Invalid first name!");
            if (entity.getLocation() == null)
                throw new ValidationException("Invalid location!");
            if (entity.getData() == null)
                throw new ValidationException("Invalid data!");
            if (entity.getAvailableSeats() < 0)
                throw new ValidationException("Invalid number of available seats!");
            if (entity.getSoldSeats() < 0)
                throw new ValidationException("Invalid number of sold seats!");
        }
    }
}
