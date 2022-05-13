package com.epam.esm.dao.impl;

import com.epam.esm.dao.UserDao;
import com.epam.esm.dao.entity.User;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
import java.util.List;
import java.util.Optional;

@Repository
@Transactional
public class UserDaoImpl implements UserDao {
    private static final String USER_ID = "userId";
    private static final String LOCK = "lock";
    private static final String USER_NAME = "userName";

    private final SessionFactory sessionFactory;

    private static final Integer LOCK_VALUE_0 = 0;
    private static final Integer LOCK_VALUE_1 = 1;

    @Autowired
    public UserDaoImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public Optional<User> create(User user) {
        User result;
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        session.save(user);
        session.getTransaction().commit();
        result = session.load(User.class, session.getIdentifier(user));
        session.close();
        return Optional.of(result);
    }

    @Override
    public boolean delete(long id) {
        Session session = sessionFactory.openSession();
        CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
        CriteriaUpdate<User> criteriaUpdate = criteriaBuilder.createCriteriaUpdate(User.class);
        Root<User> root = criteriaUpdate.from(User.class);
        criteriaUpdate.set(LOCK, LOCK_VALUE_1).
                where(criteriaBuilder.equal(root.get(USER_ID), id));
        session.beginTransaction();
        int size = session.createQuery(criteriaUpdate).executeUpdate();
        session.getTransaction().commit();
        session.close();
        return size > 0;
    }

    @Override
    public List<User> findAll(int page, int size) {
        Session session = sessionFactory.openSession();
        CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
        CriteriaQuery<User> criteriaQuery = criteriaBuilder.createQuery(User.class);
        Root<User> root = criteriaQuery.from(User.class);
        criteriaQuery.select(root).where(criteriaBuilder.equal(root.get(LOCK), LOCK_VALUE_0))
                .orderBy(criteriaBuilder.asc(root.get(USER_ID)));
        TypedQuery<User> typedQuery = session.createQuery(criteriaQuery);
        typedQuery.setFirstResult(page);
        typedQuery.setMaxResults(size);
        List<User> list = typedQuery.getResultList();
        session.close();
        return list;
    }

    @Override
    public Optional<User> find(long id) {
        Session session = sessionFactory.openSession();
        CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
        CriteriaQuery<User> criteriaQuery = criteriaBuilder.createQuery(User.class);
        Root<User> root = criteriaQuery.from(User.class);
        criteriaQuery.select(root).where(criteriaBuilder.equal(root.get(USER_ID), id),
                criteriaBuilder.equal(root.get(LOCK), LOCK_VALUE_0));
        Optional<User> user = session.createQuery(criteriaQuery).getResultStream().findFirst();
        session.close();
        return user;
    }

    @Override
    public Optional<User> update(long id, User user) {
        Session session = sessionFactory.openSession();
        CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
        CriteriaUpdate<User> criteriaUpdate = criteriaBuilder.createCriteriaUpdate(User.class);
        Root<User> root = criteriaUpdate.from(User.class);
        criteriaUpdate.set(USER_NAME, user.getUserName());
        criteriaUpdate.where(criteriaBuilder.equal(root.get(USER_ID), id));
        session.beginTransaction();
        session.createQuery(criteriaUpdate).executeUpdate();
        session.getTransaction().commit();
        session.close();
        return find(id);
    }

    @Override
    public Long findSize() {
        Session session = sessionFactory.openSession();
        CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
        CriteriaQuery<Long> criteriaQuery = criteriaBuilder.createQuery(Long.class);
        Root<User> root = criteriaQuery.from(User.class);
        criteriaQuery.select(criteriaBuilder.count(root.get(USER_ID)))
                .where(criteriaBuilder.equal(root.get(LOCK), LOCK_VALUE_0));
        Long total = session.createQuery(criteriaQuery).getSingleResult();
        session.close();
        return total;
    }
}
