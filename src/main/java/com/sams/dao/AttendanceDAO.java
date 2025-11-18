package com.sams.dao;

import com.sams.entity.Attendance;
import com.sams.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.util.Date;
import java.util.List;

public class AttendanceDAO {
    public void save(Attendance attendance) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.persist(attendance);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null)
                transaction.rollback();
            e.printStackTrace();
        }
    }

    public Attendance findById(int id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.get(Attendance.class, id);
        }
    }

    public List<Attendance> findByClassSchedule(int scheduleId) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Attendance> query = session.createQuery("FROM Attendance WHERE classSchedule.id = :scheduleId",
                    Attendance.class);
            query.setParameter("scheduleId", scheduleId);
            return query.list();
        }
    }

    public List<Attendance> findByStudentAndDateRange(int studentId, Date startDate, Date endDate) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Attendance> query = session.createQuery(
                    "FROM Attendance WHERE student.id = :studentId AND classSchedule.date BETWEEN :start AND :end",
                    Attendance.class);
            query.setParameter("studentId", studentId);
            query.setParameter("start", startDate);
            query.setParameter("end", endDate);
            return query.list();
        }
    }

    public void update(Attendance attendance) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.merge(attendance);
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
            Attendance attendance = session.get(Attendance.class, id);
            if (attendance != null)
                session.remove(attendance);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null)
                transaction.rollback();
            e.printStackTrace();
        }
    }

    public List<Attendance> findAll() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("FROM Attendance", Attendance.class).list();
        }
    }
}