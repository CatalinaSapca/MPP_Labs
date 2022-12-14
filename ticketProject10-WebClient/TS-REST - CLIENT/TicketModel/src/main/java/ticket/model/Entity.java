package ticket.model;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.io.Serializable;

public class Entity<ID> implements Serializable {

    private static final long serialVersionUID = 7331115341259248461L;
    private ID id;

    public Entity() { }

    public ID getId() {
        return id;
    }

    public void setId(ID id) {
        this.id = id;
    }

}