package com.crud.dao.impl;

import com.crud.dao.BaseDao;
import com.crud.dto.CreateUserDto;
import com.crud.entity.User;
import com.crud.exceptions.DaoException;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.hibernate.query.Query;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;
import java.util.logging.Logger;

public class UserDao implements BaseDao<User, Long> {
    private static final Logger LOGGER = Logger.getLogger(UserDao.class.getName());

    private static final SessionFactory SESSION_FACTORY = new Configuration().configure().buildSessionFactory();

    public static final AtomicReference<UserDao> USER_DAO = new AtomicReference<>();

    public static UserDao getInstance() {
        UserDao current = USER_DAO.get();
        if (current == null) {
            UserDao newUserDao = new UserDao();
            if (USER_DAO.compareAndSet(null, newUserDao)) {
                return newUserDao;
            } else {
                return USER_DAO.get();
            }
        }
        return current;
    }

    @Override
    public void save(User entity) {
        try (Session session = SESSION_FACTORY.openSession()) {
            Transaction tx = session.beginTransaction();
            session.persist(entity);
            tx.commit();
        } catch (Exception e) {
            LOGGER.severe("Error while saving user: " + e.getMessage());
            throw new DaoException("Error while saving user");
        }
    }

    @Override
    public Optional<User> findById(Long id) {
        try (Session session = SESSION_FACTORY.openSession()) {
            User user = session.get(User.class, id);
            return Optional.ofNullable(user);
        } catch (Exception e) {
            LOGGER.severe("Error while finding user by ID: " + e.getMessage());
            throw new DaoException("Error while finding user by ID");
        }
    }

    @Override
    public User update(User entity) {

        Transaction tx = null;
        try (Session session = SESSION_FACTORY.openSession()) {
            tx = session.beginTransaction();
            User userToUpdate = session.get(User.class, entity.getId());
            if (userToUpdate != null) {
                userToUpdate.setName(entity.getName());
                userToUpdate.setRegistrationDate(entity.getRegistrationDate());
                userToUpdate.setLogin(entity.getLogin());
                userToUpdate.setRoleId(entity.getRoleId());
                session.update(userToUpdate);
                tx.commit();
            } else {
                throw new IllegalArgumentException("No user found with ID: " + entity.getId());
            }
        } catch (HibernateException e) {
            LOGGER.severe("Error while updating user");
            if (tx != null) tx.rollback();
            throw e;
        }
        return entity;
    }


    public User updateWithDto(CreateUserDto createUserDto) {
        if (createUserDto.getId() == null) {
            throw new IllegalArgumentException("User id is null");
        }

        Transaction tx = null;
        try (Session session = SESSION_FACTORY.openSession()) {
            tx = session.beginTransaction();

            User userToUpdate = session.get(User.class, createUserDto.getId());
            if (userToUpdate != null) {
                userToUpdate.setName(createUserDto.getName());
                userToUpdate.setRegistrationDate(createUserDto.getRegistrationDate());
                userToUpdate.setLogin(createUserDto.getLogin());
                userToUpdate.setRoleId(Integer.valueOf(createUserDto.getRole()));
                session.merge(userToUpdate);
                tx.commit();
            } else {
                throw new IllegalArgumentException("No user found with ID: " + createUserDto.getId());
            }

            return userToUpdate;

        } catch (HibernateException e) {
            LOGGER.severe("Error while updating user");
            if (tx != null) tx.rollback();
            throw e;
        }
    }

    @Override
    public void delete(Long id) {
        try (Session session = SESSION_FACTORY.openSession()) {
            Transaction tx = session.beginTransaction();
            User user = session.get(User.class, id);
            if (user != null) {
                session.delete(user);
                tx.commit();
            }
        } catch (Exception e) {
            LOGGER.severe("Error while deleting user: " + e.getMessage());
            throw new DaoException("Error while deleting user");
        }
    }

    @Override
    public List<User> findAll() {
        try (Session session = SESSION_FACTORY.openSession()) {
            return session.createQuery("from User", User.class).list();
        } catch (Exception e) {
            LOGGER.severe("Error while finding all users: " + e.getMessage());
            throw new DaoException("Error while finding all users");
        }
    }

    public Optional<User> findByLogin(String login) {
        try (Session session = SESSION_FACTORY.openSession()) {
            String hql = "FROM User WHERE login = :login";
            Query<User> query = session.createQuery(hql, User.class);
            query.setParameter("login", login);
            return Optional.ofNullable(query.uniqueResult());
        } catch (Exception e) {
            LOGGER.severe("Error finding user by login: " + e.getMessage());
            throw new DaoException("Error while finding user by login");
        }
    }



}
