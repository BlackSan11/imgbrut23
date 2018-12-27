package ru.red.db;


import org.hibernate.LockOptions;
import org.hibernate.Session;
import org.hibernate.Transaction;
import ru.red.parser.SinglePhoto;


public class SinglePhotoDao {

    public SinglePhoto findById(int id) {
        return HibernateSessionFactoryUtil.getSessionFactory().openSession().get(SinglePhoto.class, id);
    }

    public SinglePhoto findB() {
        return (SinglePhoto) HibernateSessionFactoryUtil.getSessionFactory().openSession().createQuery("select * from ids where url is null;");
    }

    public void save(SinglePhoto singlePhoto) {
        Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession();
        Transaction tx1 = session.beginTransaction();
        session.save(singlePhoto);
        tx1.commit();
        session.close();
    }

    public void update(SinglePhoto singlePhoto) {
        Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession();
        Transaction tx1 = session.beginTransaction();
        session.update(singlePhoto);
        tx1.commit();
        session.close();
    }

    public void delete(SinglePhoto singlePhoto) {
        Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession();
        Transaction tx1 = session.beginTransaction();
        session.delete(singlePhoto);
        tx1.commit();
        session.close();
    }

}