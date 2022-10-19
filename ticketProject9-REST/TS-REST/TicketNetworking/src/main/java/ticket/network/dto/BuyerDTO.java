package ticket.network.dto;

import java.io.Serializable;


public class BuyerDTO implements Serializable{
    private String idAR;
    private String firstName;
    private String lastName;
    private String noTickets;

    public BuyerDTO(String idAR, String firstName, String lastName, String noTickets) {
        this.idAR = idAR;
        this.firstName = firstName;
        this.lastName = lastName;
        this.noTickets = noTickets;
    }

    public String getARId() {
        return idAR;
    }
    public String getFirstName() {
        return firstName;
    }
    public String getLastName() { return lastName; }
    public String getNoTickets() {
        return noTickets;
    }

    public void setIdAR(String idAR) { this.idAR = idAR; }
    public void setFirstName(String firstName) { this.firstName = firstName; }
    public void setLastName(String lastName) { this.lastName = lastName; }
    public void setNoTickets(String noTickets) { this.noTickets = noTickets; }

    @Override
    public String toString(){
        return "BuyerDTO[" + idAR + " ; " + firstName + " ; " + lastName + " ; " + noTickets +"]";
    }
}
