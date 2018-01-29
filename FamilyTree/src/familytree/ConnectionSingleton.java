/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package familytree;

import javaapplication3.HibernateUtil;
import org.hibernate.Session;

/**
 *
 * @author Milan
 */
public class ConnectionSingleton {
    private static class SingletonHolder
    {
        public static final Session instance = HibernateUtil.getSessionFactory().openSession();
    }
    
    public static Session getSession()
    {
        return SingletonHolder.instance;
    }
}
