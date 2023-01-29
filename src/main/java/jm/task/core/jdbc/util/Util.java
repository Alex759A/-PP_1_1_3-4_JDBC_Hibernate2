package jm.task.core.jdbc.util;

import jm.task.core.jdbc.model.User;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class Util {
    private static Util instance = null;
    private static SessionFactory sessionFactory;
    private Util() {
    }
    public static SessionFactory getSessionFactory() {

        if (sessionFactory == null) {
            try  {
                sessionFactory = new Configuration()
                .addAnnotatedClass(User.class)
                .buildSessionFactory();
            } catch (Exception e) {
                System.out.println("Исключение!" + e);
            }
        }
        return sessionFactory;
    }
    public static Util getInstance() { // возврат объект класса Util
        if (null == instance) {
            instance = new Util();
        }
        return instance;
    }
}


