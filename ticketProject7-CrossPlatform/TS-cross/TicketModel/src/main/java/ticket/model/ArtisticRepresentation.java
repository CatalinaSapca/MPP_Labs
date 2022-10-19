package ticket.model;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

public class ArtisticRepresentation extends Entity<Long> implements Serializable {
    private String artistName;
    private LocalDateTime data;
    private String location;
    private Long availableSeats;
    private Long soldSeats;

    public ArtisticRepresentation(String artistName, LocalDateTime data, String location, Long availableSeats) {
        this.artistName=artistName;
        this.data=data;
        this.location=location;
        this.availableSeats=availableSeats;
        this.soldSeats=(long)0;
    }

    public ArtisticRepresentation(String artistName, LocalDateTime data, String location, Long availableSeats, long soldSeats) {
        this.artistName=artistName;
        this.data=data;
        this.location=location;
        this.availableSeats=availableSeats;
        this.soldSeats=soldSeats;
    }

    public ArtisticRepresentation(String id, String availableSeats, String soldSeats) {
        this.setId(Long.valueOf(id));
        this.availableSeats = Long.valueOf(availableSeats);
        this.soldSeats = Long.valueOf(soldSeats);
        this.data = LocalDateTime.now();
        this.location = "aaa";
    }

    public String getArtistName() {
        return artistName;
    }

    public void setArtistName(String artistName) {
        this.artistName = artistName;
    }

    public LocalDateTime getData() {
        return data;
    }

    public void setData(LocalDateTime data) {
        this.data = data;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Long getAvailableSeats() {
        return availableSeats;
    }

    public void setAvailableSeats(long availableSeats){this.availableSeats=availableSeats;}

    public Long getSoldSeats(){return soldSeats;}

    public void setSoldSeats(long soldSeats){this.soldSeats=soldSeats;}

    @Override
    public String toString() {
        return this.getArtistName() + " " + this.getData();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ArtisticRepresentation)) return false;
        ArtisticRepresentation that = (ArtisticRepresentation) o;
        return getLocation().equals(that.getLocation()) &&
                getArtistName().equals(that.getArtistName()) &&
                getData().equals(that.getData()) &&
                getAvailableSeats().equals(that.getAvailableSeats()) &&
                getSoldSeats().equals(that.getSoldSeats());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getArtistName(), getLocation(), getData(), getAvailableSeats(), getSoldSeats());
    }

}
