package jm.task.core.jdbc.dao;

import jakarta.persistence.PersistenceException;
import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.util.Util;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

public class UserDaoHibernateImpl implements UserDao {

    private static final Util utilInstance = Util.getInstance();
    private static final SessionFactory sessionFactory = utilInstance.getSessionFactory();
    private Transaction tx = null;


    public UserDaoHibernateImpl() {

    }


    @Override
    public void createUsersTable() {

    }

    @Override
    public void dropUsersTable() {

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
                session.delete(user);
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
            return session.createQuery("from User").list();
        } catch (HibernateException e) {
            e.printStackTrace();
        }

        return new ArrayList<>();
    }


    // Очистка содержания таблицы
    @Override
    public void cleanUsersTable() {
        try {
            Session session = sessionFactory.getCurrentSession();
            session.beginTransaction();
            session.createQuery("delete User ").executeUpdate();
            session.getTransaction().commit();
        } finally {
            sessionFactory.close();
        }
    }
}
