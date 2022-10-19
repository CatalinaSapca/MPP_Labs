package ticket.model.validators;

import ticket.model.Buyer;

import java.security.InvalidParameterException;


/**
 * Validates an instance of class Buyer
 * Throws ValidateException if instance is invalid
 */
public class BuyerValidator implements Validator<Buyer> {

    /**
     * @throws InvalidParameterException if id  id is null or <=0 / first name/last name are null.
     */
    @Override
    public void validate(Buyer entity) throws ValidationException {
        if (entity == null)
            throw new InvalidParameterException();
        else {
            if (entity.getId() <= 0)
                throw new ValidationException("Invalid ID!");
            if (entity.getIdAR() == null)
                throw new ValidationException("Invalid idAR!");
            if (entity.getFirstName() == null)
                throw new ValidationException("Invalid first name!");
            if (entity.getLastName() == null)
                throw new ValidationException("Invalid last name!");
            if (entity.getNoTickets() < 0)
                throw new ValidationException("Invalid noTichets!");
        }
    }
}