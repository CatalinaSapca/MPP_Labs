package ticket.persistance.jdbc;

import ticket.model.ArtisticRepresentation;
import ticket.model.validators.ValidationException;
import ticket.model.validators.Validator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ticket.persistance.ARRepository;
import ticket.persistance.jdbc.utils.JdbcUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class ARDatabase implements ARRepository {

    private final JdbcUtils dbUtils;
    final private Validator<ArtisticRepresentation> validator;
    private static final Logger logger= LogManager.getLogger();

    public ARDatabase(Validator<ArtisticRepresentation> validator, Properties props) {
        this.validator = validator;
        dbUtils=new JdbcUtils(props);
        logger.info("Initializing ARDatabase");
    }

    private LocalDateTime fromStringToLocalDateTime(String string){
        LocalDateTime data=LocalDateTime.now();
        try{
            data = LocalDateTime.parse(string);
        }
        catch (Exception ex){
            String[] arr = string.split(" ");
            String[] arrDate = arr[0].split("-");
            String[] arrTime = arr[1].split(":");

            data = LocalDateTime.of(Integer.parseInt(arrDate[0]),Integer.parseInt(arrDate[1]), Integer.parseInt(arrDate[2]), Integer.parseInt(arrTime[0]), Integer.parseInt(arrTime[1]));
        }
        return data;
    }

    public ArtisticRepresentation extractEntity(List<String> attributes) {
        if (attributes.size() < 6)
            throw new ValidationException("Insuficient datas!\n");
        ArtisticRepresentation ar = new ArtisticRepresentation(attributes.get(1), LocalDateTime.parse(attributes.get(2)), attributes.get(3), Long.parseLong(attributes.get(4)), Long.parseLong(attributes.get(5)));
        ar.setId(Long.parseLong(attributes.get(0)));
        return ar;
    }

    protected String createEntityAsString(ArtisticRepresentation entity) {
        return entity.getId() + ";" + entity.getArtistName() + ";" + entity.getData() + ";" + entity.getLocation() + ";" + entity.getAvailableSeats()+ ";"+entity.getSoldSeats();
    }


    @Override
    public ArtisticRepresentation findOne(Long id) {
        if (id == null)
            throw new IllegalArgumentException("id must be not null");

        Connection con= dbUtils.getConnection();
        try {
            PreparedStatement preStmt = con.prepareStatement("select * from ARs where id = ?");
            preStmt.setInt(1, Math.toIntExact(id));
            ResultSet result = preStmt.executeQuery();

            if (result.next()) {
                Long idd = result.getLong("id");
                String artistName = result.getString("artistName");
                String location = result.getString("location");
                LocalDateTime data = this.fromStringToLocalDateTime(result.getString("data"));
                Long availableSeats = result.getLong("availableSeats");
                Long soldSeats = result.getLong("soldSeats");

                ArtisticRepresentation ar = new ArtisticRepresentation(artistName, data, location, availableSeats, soldSeats);
                ar.setId(idd);

                result.close();
                preStmt.close();

                return ar;
            }
        } catch (Exception e) {
            System.out.println(e.toString());
        }
        return null;
    }

    public Iterable<ArtisticRepresentation>  findAllARWithARName(String name) {
        if (name == null)
            throw new IllegalArgumentException("name must be not null");

        Connection con= dbUtils.getConnection();
        List<ArtisticRepresentation> ars= new ArrayList<>();
        try {
            PreparedStatement preStmt = con.prepareStatement("select * from ARs where artistName = ?");
            preStmt.setString(1, name);
            ResultSet result = preStmt.executeQuery();
            while(result.next()){
                Long id=result.getLong("id");
                String artistName = result.getString("artistName");
                LocalDateTime data = this.fromStringToLocalDateTime(result.getString("data"));
                String location = result.getString("location");
                Long availableSeats = result.getLong("availableSeats");
                Long soldSeats = result.getLong("soldSeats");

                ArtisticRepresentation ar = new ArtisticRepresentation(artistName, data, location, availableSeats, soldSeats);
                ar.setId(id);

                ars.add(ar);
            }
            result.close();
            preStmt.close();
        } catch (Exception e) {
            System.out.println(e.toString());
        }
        return ars;
    }


    @Override
    public Iterable<ArtisticRepresentation> findAll(){
        Connection con= dbUtils.getConnection();
        List<ArtisticRepresentation> ars= new ArrayList<>();

        try{
            PreparedStatement preStmt = con.prepareStatement("select * from ARs ");
            ResultSet result = preStmt.executeQuery();
            while(result.next()){
                Long id=result.getLong("id");
                String artistName = result.getString("artistName");
                LocalDateTime data = this.fromStringToLocalDateTime(result.getString("data"));
                String location = result.getString("location");
                Long availableSeats = result.getLong("availableSeats");
                Long soldSeats = result.getLong("soldSeats");

                ArtisticRepresentation ar = new ArtisticRepresentation(artistName, data, location, availableSeats, soldSeats);
                ar.setId(id);

                ars.add(ar);
            }
            result.close();
            preStmt.close();
        }
        catch(SQLException ex){
            System.err.println("Error DB" + ex);
        }

        return ars;
    }


    @Override
    public ArtisticRepresentation save(ArtisticRepresentation entity) {
        logger.traceEntry("saving AR {}", entity);
        if (entity == null)
            throw new IllegalArgumentException("entity must be not null");
        validator.validate(entity);
//        if (findOne(entity.getId()) != null) {
//            return entity;
//        }

        Connection con= dbUtils.getConnection();
        try{
            PreparedStatement preStmt = con.prepareStatement("insert into ARs (artistName, data, location, availableSeats, soldSeats) values (?,?,?,?,?)");
            preStmt.setString(1,entity.getArtistName());
            //preStmt.setTimestamp(2, Timestamp.valueOf(entity.getData()));
            //preStmt.setDate(2, Date.valueOf(String.valueOf(entity.getData())));
            //preStmt.setDate(2, Date.valueOf(String.valueOf(entity.getData())));
            //preStmt.setTime(2, Time.valueOf(String.valueOf(entity.getData())));
            preStmt.setString(2, entity.getData().toString());
            preStmt.setString(3,entity.getLocation());
            preStmt.setInt(4, Math.toIntExact(entity.getAvailableSeats()));
            preStmt.setInt(5, Math.toIntExact(entity.getSoldSeats()));
            int result=preStmt.executeUpdate();

            preStmt.close();

            logger.trace("Saved {} instances", result);
            if (result == 0)
                throw new Exception("Insert Failed");
        }
        catch(SQLException ex){
            System.err.println("Error DB" + ex);
        }
        catch(Exception ex){
            logger.error(ex);
            System.err.println(ex);
        }

        logger.traceExit();
        return null;
    }


    @Override
    public ArtisticRepresentation delete(Long id) {
        logger.traceEntry("deleting AR with id {}", id);
        if (id == null)
            throw new IllegalArgumentException("Non existent user!");

        Connection con= dbUtils.getConnection();
        try {
            ArtisticRepresentation entity = findOne(id);

            PreparedStatement preStmt = con.prepareStatement("DELETE FROM ARs WHERE id = ?");
            preStmt.setInt(1, Math.toIntExact(id));
            int result = preStmt.executeUpdate();

            preStmt.close();

            //con.close();
            logger.trace("Deleted {} instances", result);
            if (result > 0)
                return entity;
            else
                return null;

        } catch (Exception e) {
            logger.error(e);
            System.out.println(e.toString());
        }

        logger.traceExit();
        return null;
    }


    @Override
    public ArtisticRepresentation update(ArtisticRepresentation entity) {
        logger.traceEntry("updating AR {}", entity);
        if (entity == null)
            throw new IllegalArgumentException("entity must be not null!");
        validator.validate(entity);

        if (findOne(entity.getId()) == null)
            return entity;
        Connection con= dbUtils.getConnection();
        try {
            PreparedStatement preStmt = con.prepareStatement("update ARs set artistName=(?), data=(?), location=(?), availableSeats=(?), soldSeats=(?) where id=(?);");
            String data = entity.getData().getYear()+"-"+entity.getData().getMonth()+"-"+entity.getData().getDayOfMonth()+" "+entity.getData().getHour()+":"+entity.getData().getMinute();
            preStmt.setString(1, entity.getArtistName());
            preStmt.setString(2, entity.getData().toString());
            //preStmt.setString(2, data);
            preStmt.setString(3, entity.getLocation());
            preStmt.setInt(4, Math.toIntExact(entity.getAvailableSeats()));
            preStmt.setInt(5, Math.toIntExact(entity.getSoldSeats()));
            preStmt.setInt(6, Math.toIntExact(entity.getId()));
            int result = preStmt.executeUpdate();

            preStmt.close();

            logger.trace("Updated {} instances", result);
            if (result > 0)
                return null;
            else
                return entity;
        } catch (Exception e) {
            logger.error(e);
            System.out.println(e.toString());
        }

        logger.traceExit();
        return entity;
    }


}
