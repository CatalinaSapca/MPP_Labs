package ticket.network.dto;

import java.io.Serializable;


public class ArtisticRepresentationDTO implements Serializable{
    private String id;
    private String artistName;
    private String data;
    private String location;
    private String availableSeats;
    private String soldSeats;

    public ArtisticRepresentationDTO(String id, String artistName, String data, String location, String availableSeats, String soldSeats) {
        this.id = id;
        this.artistName = artistName;
        this.data = data;
        this.location = location;
        this.availableSeats = availableSeats;
        this.soldSeats = soldSeats;
    }

    public String getId() {
        return id;
    }
    public String getArtistName() { return artistName; }
    public String getData() { return data; }
    public String getLocation() { return location; }
    public String getAvailableSeats() { return availableSeats; }
    public String getSoldSeats() { return soldSeats; }

    public void setId(String id) { this.id = id; }
    public void setAvailableSeats(String availableSeats) { this.availableSeats = availableSeats; }
    public void setSoldSeats(String soldSeats) { this.soldSeats = soldSeats; }
    public void setArtistName(String artistName) { this.artistName = artistName; }
    public void setData(String data) { this.data = data; }
    public void setLocation(String location) { this.location = location; }

    @Override
    public String toString(){
        return "ArtisticRepresentationDTO["+id+";"+artistName+";"+data+";"+location+";"+availableSeats+";"+soldSeats+"]";
    }
}
