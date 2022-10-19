package ticket.model;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

public class ArtisticRepresentation extends Entity<Long> implements Serializable {
    private String artistName;
//    @JsonSerialize(using = LocalDateTimeSerializer.class)
//    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
//    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
//@JsonFormat
//        (shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy hh:mm:ss")
//@JsonSerialize(using = LocalDateTimeSerializerCustom.class)
@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",shape = JsonFormat.Shape.STRING)
private LocalDateTime data = LocalDateTime.now();
    private String location;
    private Long availableSeats;
    private Long soldSeats;

    public ArtisticRepresentation() {
        super();
    }

    public ArtisticRepresentation(Long id, String artistName, LocalDateTime data, String location, Long availableSeats) {
        this.artistName=artistName;
        this.data=data;
        this.location=location;
        this.availableSeats=availableSeats;
        this.soldSeats=(long)0;
        super.setId(id);
    }

    public ArtisticRepresentation(Long id, String artistName, String location, Long availableSeats) {
        this.artistName=artistName;
        this.data=LocalDateTime.now();
        this.location=location;
        this.availableSeats=availableSeats;
        this.soldSeats=(long)0;
        super.setId(id);
    }

    public ArtisticRepresentation(String artistName, LocalDateTime data, String location, Long availableSeats) {
        this.artistName=artistName;
        this.data=data;
        this.location=location;
        this.availableSeats=availableSeats;
        this.soldSeats=(long)0;
    }

    public ArtisticRepresentation(String artistName, String data, String location, Long availableSeats) {
        this.artistName=artistName;
        this.data=LocalDateTime.parse(data);
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

    public Long getId() {
        return super.getId();
    }

    public void setId(Long id){
        super.setId(id);
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
