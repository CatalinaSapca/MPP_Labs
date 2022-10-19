package ticket.persistance.jdbc;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ticket.model.Buyer;
import ticket.model.validators.ValidationException;
import ticket.model.validators.Validator;
import ticket.persistance.BuyerRepository;
import ticket.persistance.jdbc.utils.JdbcUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class BuyerDatabase implements BuyerRepository {

    private final JdbcUtils dbUtils;
    final private Validator<Buyer> validator;
    private static final Logger logger= LogManager.getLogger();

    public BuyerDatabase(Validator<Buyer> validator, Properties props) {
        this.validator = validator;
        dbUtils=new JdbcUtils(props);
        logger.info("Initializing BuyerDatabase");
    }

    public Buyer extractEntity(List<String> attributes) {
        if (attributes.size() < 5)
            throw new ValidationException("Insuficient datas!\n");
        Buyer buyer = new Buyer(Long.parseLong(attributes.get(1)), attributes.get(2),attributes.get(3), Long.parseLong(attributes.get(4)));
        buyer.setId(Long.parseLong(attributes.get(0)));
        return buyer;
    }

    protected String createEntityAsString(Buyer entity) {
        return entity.getId() + ";" + entity.getIdAR()+ ";" + entity.getFirstName() + ";" + entity.getLastName() + ";" + entity.getNoTickets();
    }

    @Override
    public Buyer findOne(Long id) {
        if (id == null)
            throw new IllegalArgumentException("id must be not null");

        Connection con= dbUtils.getConnection();
        try {
            PreparedStatement preStmt = con.prepareStatement("select * from Buyers where id = ?");
            preStmt.setInt(1, Math.toIntExact(id));
            ResultSet result = preStmt.executeQuery();

            if (result.next()) {
                Long idd = result.getLong("id");
                Long idAR = result.getLong("idAR");
                String firstName = result.getString("firstName");
                String lastName = result.getString("lastName");
                Long noTickets = result.getLong("noTickets");

                Buyer buyer = new Buyer(idAR, firstName, lastName, noTickets);
                buyer.setId(idd);

                result.close();
                preStmt.close();

                return buyer;
            }
        } catch (Exception e) {
            System.out.println(e.toString());
        }
        return null;
    }

    @Override
    public Iterable<Buyer> findAll() {
        Connection con= dbUtils.getConnection();
        List<Buyer> buyers= new ArrayList<>();

        try{
            PreparedStatement preStmt = con.prepareStatement("select * from Buyers ");
            ResultSet result = preStmt.executeQuery();
            while(result.next()){
                Long id=result.getLong("id");
                Long idAR=result.getLong("idAR");
                String firstName = result.getString("firstName");
                String lastName = result.getString("lastName");
                Long noTickets = result.getLong("noTickets");

                Buyer buyer = new Buyer(idAR, firstName, lastName, noTickets);
                buyer.setId(id);

                buyers.add(buyer);
            }
            result.close();
            preStmt.close();
        }
        catch(SQLException ex){
            System.err.println("Error DB" + ex);
        }

        return buyers;
    }


    @Override
    public Buyer save(Buyer entity) {
        logger.traceEntry("saving Buyer {}", entity);
        if (entity == null)
            throw new IllegalArgumentException("entity must be not null");
        validator.validate(entity);

        Connection con= dbUtils.getConnection();
        try{
            PreparedStatement preStmt = con.prepareStatement("insert into Buyers (idAR, firstName, lastName, noTickets) values (?,?,?,?)");
            preStmt.setInt(1, Math.toIntExact(entity.getIdAR()));
            preStmt.setString(2, entity.getFirstName());
            preStmt.setString(3,entity.getLastName());
            preStmt.setInt(4, Math.toIntExact(entity.getNoTickets()));
            int result=preStmt.executeUpdate();

            preStmt.close();

            logger.trace("Saved {} instances", result);
            if (result == 0)
                throw new Exception("Insert Failed");
        }
        catch(SQLException ex){
            logger.error(ex);
            System.err.println("Error DB" + ex);
        }
        catch(Exception ex){
            System.err.println(ex);
        }

        logger.traceExit();
        return null;
    }

    @Override
    public Buyer delete(Long id) {
        logger.traceEntry("deleting Buyer with id {}", id);
        if (id == null)
            throw new IllegalArgumentException("Non existent user!");

        Connection con= dbUtils.getConnection();
        try {
            Buyer entity = findOne(id);

            PreparedStatement preStmt = con.prepareStatement("DELETE FROM Buyers WHERE id = ?");
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
    public Buyer update(Buyer entity) {
        logger.traceEntry("updating Buyer {}", entity);
        if (entity == null)
            throw new IllegalArgumentException("entity must be not null!");
        validator.validate(entity);

        if (findOne(entity.getId()) == null)
            return entity;
        Connection con= dbUtils.getConnection();
        try {
            PreparedStatement preStmt = con.prepareStatement("update Buyers set idAR=(?), firstName=(?), lastName=(?), noTickets=(?) where id=(?);");
            preStmt.setInt(1, Math.toIntExact(entity.getIdAR()));
            preStmt.setString(2, entity.getFirstName());
            preStmt.setString(3, entity.getLastName());
            preStmt.setInt(4, Math.toIntExact(entity.getNoTickets()));
            preStmt.setInt(5, Math.toIntExact(entity.getId()));
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
