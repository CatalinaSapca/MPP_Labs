package ticket.persistance.jdbc.ORM;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
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
import java.util.Date;
import java.util.List;
import java.util.Properties;

public class BuyerDatabaseORM implements BuyerRepository {

    private final JdbcUtils dbUtils;
    final private Validator<Buyer> validator;
    public SessionFactory sessionFactory;
    private static final Logger logger= LogManager.getLogger();

    public BuyerDatabaseORM(Validator<Buyer> validator, Properties props, SessionFactory sessionFactory) {
        this.validator = validator;
        dbUtils=new JdbcUtils(props);
        this.sessionFactory = sessionFactory;
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

        try(Session session = sessionFactory.openSession()) {
            Transaction tx = null;
            try {
                tx = session.beginTransaction();
                Buyer buyer = session.createQuery("from Buyer b where id=:id", Buyer.class).uniqueResult();
                tx.commit();
                return buyer;
            } catch (RuntimeException ex) {
                if (tx != null)
                    tx.rollback();
            }
        }
        return null;
    }

    @Override
    public Iterable<Buyer> findAll() {
        List<Buyer> buyers= new ArrayList<>();


        try(Session session = sessionFactory.openSession()) {
            Transaction tx = null;
            try {
                tx = session.beginTransaction();
                buyers = session.createQuery("from Buyer", Buyer.class).list();
                tx.commit();
            } catch (RuntimeException ex) {
                if (tx != null)
                    tx.rollback();
            }
        }
        return buyers;
    }

    @Override
    public Buyer save(Buyer entity) {
        logger.traceEntry("saving Buyer {}", entity);
        if (entity == null)
            throw new IllegalArgumentException("entity must be not null");
        validator.validate(entity);

        try(Session session = sessionFactory.openSession()) {
            Transaction tx = null;
            try {
                tx = session.beginTransaction();
                session.save(entity);
                tx.commit();
                logger.trace("Saved {} instances", entity);
            } catch (RuntimeException ex) {
                if (tx != null)
                    tx.rollback();
            }
        }
        logger.traceExit();
        return null;
    }

    @Override
    public Buyer delete(Long id) {
        logger.traceEntry("deleting Buyer with id {}", id);
        if (id == null)
            throw new IllegalArgumentException("The value for id is null --- illegal");

        try(Session session = sessionFactory.openSession()) {
            Transaction tx = null;
            try {
                tx = session.beginTransaction();

                Buyer entity = session.createQuery("from Buyer where id=:id", Buyer.class)
                        .setMaxResults(1)
                        .uniqueResult();
                session.delete(entity);
                tx.commit();

                logger.trace("Deleted {} instances", 1);
                if (entity != null)
                    return entity;
                else
                    return null;
            } catch (RuntimeException ex) {
                logger.error(ex);
                System.out.println(ex.toString());
                if (tx != null)
                    tx.rollback();
            }
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
        try(Session session = sessionFactory.openSession()){
            Transaction tx=null;
            try{
                tx = session.beginTransaction();
                session.update(entity);
                tx.commit();
                logger.trace("Updated {} instances", 1);
                return null;
            } catch(RuntimeException ex){
                logger.error(ex);
                System.out.println(ex.toString());
                if (tx!=null)
                    tx.rollback();
            }
        }
        logger.traceExit();
        return entity;
    }


}
