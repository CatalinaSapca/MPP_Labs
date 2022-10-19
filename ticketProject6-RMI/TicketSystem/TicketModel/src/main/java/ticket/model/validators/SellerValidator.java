package ticket.model.validators;

import ticket.model.Seller;

import java.security.InvalidParameterException;

/**
 * Validates an instance of class Seller
 * Throws ValidateException if instance is invalid
 */
public class SellerValidator implements Validator<Seller> {

    /**
     * @throws InvalidParameterException if id  id is null or <=0 / first name/last name are null.
     */
    @Override
    public void validate(Seller entity) throws ValidationException {
        if (entity == null)
            throw new InvalidParameterException();
        else {
            if (entity.getId() <= 0)
                throw new ValidationException("Invalid ID!");
            if (entity.getFirstName() == null)
                throw new ValidationException("Invalid first name!");
            if (entity.getLastName() == null)
                throw new ValidationException("Invalid last name!");
            if (entity.getUsername() == null || entity.getUsername().equals(""))
                throw new ValidationException("Invalid username!");
            if (entity.getPassword() == null)
                throw new ValidationException("Invalid password!");
        }
    }
}
