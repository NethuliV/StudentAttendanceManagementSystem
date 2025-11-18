package com.sams.dao;

import com.sams.entity.ClassSchedule;
import com.sams.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.util.Date;
import java.util.List;

public class ClassScheduleDAO {
    public void save(ClassSchedule classSchedule) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.persist(classSchedule);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null)
                transaction.rollback();
            e.printStackTrace();
        }
    }

    public ClassSchedule findById(int id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.get(ClassSchedule.class, id);
        }
    }

    public ClassSchedule findByCourseAndDate(int courseId, Date date) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<ClassSchedule> query = session.createQuery(
                    "FROM ClassSchedule WHERE course.id = :courseId AND date = :date", ClassSchedule.class);
            query.setParameter("courseId", courseId);
            query.setParameter("date", date);
            return query.uniqueResult();
        }
    }

    public void update(ClassSchedule classSchedule) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.merge(classSchedule);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null)
                transaction.rollback();
            e.printStackTrace();
        }
    }

    public void delete(int id) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            ClassSchedule classSchedule = session.get(ClassSchedule.class, id);
            if (classSchedule != null)
                session.remove(classSchedule);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null)
                transaction.rollback();
            e.printStackTrace();
        }
    }

    public List<ClassSchedule> findAll() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("FROM ClassSchedule", ClassSchedule.class).list();
        }
    }
}