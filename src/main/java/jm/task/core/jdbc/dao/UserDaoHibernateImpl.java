package jm.task.core.jdbc.dao;


import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.util.Util;
import org.hibernate.*;
import org.hibernate.query.Query;

import javax.persistence.PersistenceException;
import java.util.ArrayList;
import java.util.List;

public class UserDaoHibernateImpl implements UserDao {

    private static final Util utilInstance = Util.getInstance();
    private static final SessionFactory sessionFactory = utilInstance.getSessionFactory();
    private Transaction tx = null;
    private String sql;


    public UserDaoHibernateImpl() {

    }


    @Override
    public void createUsersTable() {
        try (Session session = sessionFactory.openSession()) {
            Transaction tx = session.beginTransaction();

            sql = "CREATE TABLE IF NOT EXISTS users " +
                    "(id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY, " +
                    "name VARCHAR(255) NOT NULL, lastName VARCHAR(255) NOT NULL, " +
                    "age TINYINT NOT NULL)";

            SQLQuery query = session.createSQLQuery(sql);
            query.executeUpdate();

            tx.commit();
        } catch (PersistenceException e) {
            e.printStackTrace();

        }
    }

    @Override
    public void dropUsersTable() {
        try (Session session = sessionFactory.openSession()) {

            tx = session.beginTransaction();
            session.createSQLQuery("DROP TABLE IF EXISTS users")
                    .executeUpdate();
            tx.commit();
            session.close();

        } catch (PersistenceException e) {
            e.printStackTrace();

        }
    }

    @Override
    public void saveUser(String name, String lastName, byte age) {
        try (Session session = sessionFactory.openSession()) {

            Transaction tx = session.beginTransaction();
            session.persist(new User(name, lastName, (byte) age));

            tx.commit();

        } catch (PersistenceException e) {
            e.printStackTrace();
            if (null != tx) {
                tx.rollback();
            }
        }
    }

    @Override
    public void removeUserById(long id) {
        try (Session session = sessionFactory.openSession()) {
            tx = session.beginTransaction();
            User user = (User) session.get(User.class, id);
            if (null != user) {
                session.remove(user);
            }
            tx.commit();
        } catch (PersistenceException e) {
            e.printStackTrace();
            if (null != tx) {
                tx.rollback();
            }
        }
    }

    @Override
    public List<User> getAllUsers() {
        try (Session session = sessionFactory.openSession()) {
            Transaction tx = session.beginTransaction();

            Query query = session.createQuery("FROM User");
            List<User> users = query.list();
            tx.commit();
            return users;

        } catch (HibernateException e) {
            e.printStackTrace();
            if (null != tx) {
                tx.rollback();
            }
        }
        return new ArrayList<>();
    }


    @Override
    public void cleanUsersTable() {
        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = null;
            try {
                transaction = session.beginTransaction();;
                SQLQuery query = session.createSQLQuery("DELETE FROM users");
                query.executeUpdate();
                transaction.commit();
            } catch (Exception e) {
                if (transaction != null) {
                    transaction.rollback();
                }
                e.printStackTrace();
            }
        }

    }
}
