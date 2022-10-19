package ticket.model;

import java.util.Objects;

public class Buyer extends Entity<Long> {
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

    public Long getIdAR(){
        return idAR;
    }

    public void setIdAR(Long idAR){
        this.idAR=idAR;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

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
