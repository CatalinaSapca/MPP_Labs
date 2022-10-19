package ticket.model;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

@javax.persistence.Entity
@Table( name = "Buyers" )
public class Buyer extends Entity<Long> implements Serializable {
    private Long idAR;
    private String firstName;
    private String lastName;
    private Long noTickets;

    public Buyer(Long idAR, String firstName, String lastName, Long noTickets) {
        this.idAR=idAR;
        this.firstName = firstName;
        this.lastName = lastName;
        this.noTickets=noTickets;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false, nullable = false)
    @Override
    public Long getId() {
        return super.getId();
    }

    @Override
    public void setId(Long id) {
        super.setId(id);
    }


    @Column(name = "idAR")
    public Long getIdAR(){
        return idAR;
    }

    public void setIdAR(Long idAR){
        this.idAR=idAR;
    }

    @Column(name = "firstName")
    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    @Column(name = "lastName")
    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    @Column(name = "noTickets")
    public Long getNoTickets() {
        return noTickets;
    }

    public void setNoTickets(Long noTichets) {
        this.noTickets = noTichets;
    }

    @Override
    public String toString() {
        return this.getIdAR() + " " + this.getFirstName() + " " + this.getLastName();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Buyer)) return false;
        Buyer that = (Buyer) o;
        return getIdAR().equals(that.getIdAR()) &&
                getFirstName().equals(that.getFirstName()) &&
                getLastName().equals(that.getLastName()) &&
                getNoTickets().equals(that.getNoTickets());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getIdAR(), getFirstName(), getLastName(), getNoTickets());
    }

}
