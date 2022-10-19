package ticket.persistance.jdbc;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ticket.model.Seller;
import ticket.model.validators.ValidationException;
import ticket.model.validators.Validator;
import ticket.persistance.SellerRepository;
import ticket.persistance.jdbc.utils.JdbcUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;


public class SellerDatabase implements SellerRepository {

    private final JdbcUtils dbUtils;
    final private Validator<Seller> validator;
    private static final Logger logger= LogManager.getLogger();

    public SellerDatabase(Validator<Seller> validator, Properties props) {
        this.validator = validator;
        dbUtils=new JdbcUtils(props);
        logger.info("Initializing SellerDatabase");
    }

    public Seller extractEntity(List<String> attributes) {
        if (attributes.size() < 5)
            throw new ValidationException("Insuficient datas!\n");
        Seller seller = new Seller(attributes.get(1), attributes.get(2), attributes.get(4));
        seller.setUsername(attributes.get(3));
        seller.setId(Long.parseLong(attributes.get(0)));
        return seller;
    }

    protected String createEntityAsString(Seller entity) {
        return entity.getId() + ";" + entity.getFirstName() + ";" + entity.getLastName() + ";" + entity.getUsername() + ";" + entity.getPassword();
    }

    @Override
    public Seller findOne(Long id) {
        if (id == null)
            throw new IllegalArgumentException("id must be not null");

        Connection con= dbUtils.getConnection();
        try {
            PreparedStatement preStmt = con.prepareStatement("select * from Sellers where id = ?");
            preStmt.setInt(1, Math.toIntExact(id));
            ResultSet result = preStmt.executeQuery();

            if (result.next()) {
                Long idd = result.getLong("id");
                String firstName = result.getString("firstName");
                String lastName = result.getString("lastName");
                String username = result.getString("username");
                String password = result.getString("password");

                Seller seller = new Seller(firstName, lastName, username, password);
                seller.setId(idd);

                result.close();
                preStmt.close();

                return seller;
            }
        } catch (Exception e) {
            System.out.println(e.toString());
        }
        return null;
    }

    @Override
    public Seller findOneByUsername(String username) {
        if (username == null)
            throw new IllegalArgumentException("username must be not null");

        Connection con= dbUtils.getConnection();
        try {
            PreparedStatement preStmt = con.prepareStatement("select * from Sellers where username = ?");
            preStmt.setString(1, username);
            ResultSet result = preStmt.executeQuery();

            if (result.next()) {
                Long idd = result.getLong("id");
                String firstName = result.getString("firstName");
                String lastName = result.getString("lastName");
                String username1 = result.getString("username");
                String password = result.getString("password");

                Seller seller = new Seller(firstName, lastName, username1, password);
                seller.setId(idd);

                result.close();
                preStmt.close();

                return seller;
            }
        } catch (Exception e) {
            System.out.println(e.toString());
        }
        return null;
    }

    @Override
    public Iterable<Seller> findAll() {
        Connection con= dbUtils.getConnection();
        List<Seller> sellers= new ArrayList<>();

        try{
            PreparedStatement preStmt = con.prepareStatement("select * from Sellers ");
            ResultSet result = preStmt.executeQuery();
            while(result.next()){
                Long id=result.getLong("id");
                String firstName = result.getString("firstName");
                String lastName = result.getString("lastName");
                String username = result.getString("username");
                String password = result.getString("password");

                Seller seller = new Seller(firstName, lastName, username, password);
                seller.setId(id);

                sellers.add(seller);
            }
            result.close();
            preStmt.close();
        }
        catch(SQLException ex){
            System.err.println("Error DB" + ex);
        }

        return sellers;
    }


    @Override
    public Seller save(Seller entity) {
        logger.traceEntry("saving Seller {}", entity);
        if (entity == null)
            throw new IllegalArgumentException("entity must be not null");
        validator.validate(entity);
//        if (findOne(entity.getId()) != null) {
//            return entity;
//        }

        Connection con= dbUtils.getConnection();
        try{
            PreparedStatement preStmt = con.prepareStatement("insert into Sellers (firstName, lastName, username, password) values (?,?,?,?)");
            preStmt.setString(1, entity.getFirstName());
            preStmt.setString(2, entity.getLastName());
            preStmt.setString(3, entity.getUsername());
            preStmt.setString(4, entity.getPassword());
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
    public Seller delete(Long id) {
        logger.traceEntry("deleting Seller with id {}", id);
        if (id == null)
            throw new IllegalArgumentException("Non existent user!");

        Connection con= dbUtils.getConnection();
        try {
            Seller entity = findOne(id);

            PreparedStatement preStmt = con.prepareStatement("DELETE FROM Sellers WHERE id = ?");
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
    public Seller update(Seller entity) {
        logger.traceEntry("updating Seller {}", entity);
        if (entity == null)
            throw new IllegalArgumentException("entity must be not null!");
        validator.validate(entity);

        if (findOne(entity.getId()) == null)
            return entity;
        Connection con= dbUtils.getConnection();
        try {
            PreparedStatement preStmt = con.prepareStatement("update Sellers set firstName=(?), lastName=(?), username=(?), password=(?) where id=(?);");
            preStmt.setString(1, entity.getFirstName());
            preStmt.setString(2, entity.getLastName());
            preStmt.setString(3, entity.getUsername());
            preStmt.setString(4, entity.getPassword());
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
