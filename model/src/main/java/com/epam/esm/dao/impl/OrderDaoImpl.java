package com.epam.esm.dao.impl;

import com.epam.esm.dao.OrderDao;
import com.epam.esm.dao.entity.Order;
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
public class OrderDaoImpl implements OrderDao {
    private final SessionFactory sessionFactory;

    private static final String LOCK = "lock";
    private static final String USER_ID = "userId";
    private static final String ORDER_ID = "orderId";
    private static final String CERTIFICATE_ID = "certificateId";
    private static final Integer LOCK_VALUE_0 = 0;

    @Autowired
    public OrderDaoImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public Optional<Order> create(Order order) {
        Order result;
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        session.save(order);
        session.getTransaction().commit();
        result = session.load(Order.class, session.getIdentifier(order));
        session.close();
        return Optional.of(result);
    }

    @Override
    public boolean delete(long id) {
        Session session = sessionFactory.openSession();
        CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
        CriteriaUpdate<Order> criteriaUpdate = criteriaBuilder.createCriteriaUpdate(Order.class);
        Root<Order> root = criteriaUpdate.from(Order.class);
        criteriaUpdate.set(LOCK, 1).
                where(criteriaBuilder.equal(root.get(ORDER_ID), id));
        session.beginTransaction();
        int size = session.createQuery(criteriaUpdate).executeUpdate();
        session.getTransaction().commit();
        session.close();
        return size > 0;
    }

    @Override
    public List<Order> findAll(int page, int size) {
        Session session = sessionFactory.openSession();
        CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
        CriteriaQuery<Order> criteriaQuery = criteriaBuilder.createQuery(Order.class);
        Root<Order> root = criteriaQuery.from(Order.class);
        criteriaQuery.select(root).where(criteriaBuilder.equal(root.get(LOCK), LOCK_VALUE_0)).
                orderBy(criteriaBuilder.asc(root.get(ORDER_ID)));
        TypedQuery<Order> typedQuery = session.createQuery(criteriaQuery);
        typedQuery.setFirstResult(page);
        typedQuery.setMaxResults(size);
        List<Order> list = typedQuery.getResultList();
        session.close();
        return list;
    }

    @Override
    public Optional<Order> find(long id) {
        Session session = sessionFactory.openSession();
        CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
        CriteriaQuery<Order> criteriaQuery = criteriaBuilder.createQuery(Order.class);
        Root<Order> root = criteriaQuery.from(Order.class);
        criteriaQuery.select(root).where(criteriaBuilder.equal(root.get(ORDER_ID), id),
                criteriaBuilder.equal(root.get(LOCK), LOCK_VALUE_0));
        Optional<Order> order = session.createQuery(criteriaQuery).getResultStream().findFirst();
        session.close();
        return order;
    }

    @Override
    public Optional<Order> update(long id, Order order) {
        Session session = sessionFactory.openSession();
        CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
        CriteriaUpdate<Order> criteriaUpdate = criteriaBuilder.createCriteriaUpdate(Order.class);
        Root<Order> root = criteriaUpdate.from(Order.class);
        criteriaUpdate.set(USER_ID, order.getUserId());
        criteriaUpdate.set(CERTIFICATE_ID, order.getCertificateId());
        criteriaUpdate.set("cost", order.getCost());
        criteriaUpdate.where(criteriaBuilder.equal(root.get(ORDER_ID), id));
        session.beginTransaction();
        session.createQuery(criteriaUpdate).executeUpdate();
        session.getTransaction().commit();
        session.close();
        return find(id);
    }

    @Override
    public Optional<Order> findCostAndDateOfBuyForUserByOrderId(long userId, long orderId) {
        Session session = sessionFactory.openSession();
        CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
        CriteriaQuery<Order> criteriaQuery = criteriaBuilder.createQuery(Order.class);
        Root<Order> root = criteriaQuery.from(Order.class);
        criteriaQuery.multiselect(root.get("cost"), root.get("dateOfBuy"));
        Predicate predicateForUserId = criteriaBuilder.equal(root.get(USER_ID), userId);
        Predicate predicateForOrderId = criteriaBuilder.equal(root.get(ORDER_ID), orderId);
        Predicate finalPredicate = criteriaBuilder.and(predicateForUserId, predicateForOrderId);
        criteriaQuery.where(finalPredicate, criteriaBuilder.equal(root.get(LOCK), LOCK_VALUE_0));
        return session.createQuery(criteriaQuery).getResultStream().findFirst();
    }

    @Override
    public List<Order> findOrdersByUserId(long id, int skip, int limit) {
        Session session = sessionFactory.openSession();
        CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
        CriteriaQuery<Order> criteriaQuery = criteriaBuilder.createQuery(Order.class);
        Root<Order> root = criteriaQuery.from(Order.class);
        criteriaQuery.select(root).where(criteriaBuilder.equal(root.get("userId"), id),
                criteriaBuilder.equal(root.get(LOCK), LOCK_VALUE_0));
        TypedQuery<Order> typedQuery = session.createQuery(criteriaQuery);
        typedQuery.setFirstResult(skip);
        typedQuery.setMaxResults(limit);
        List<Order> list = typedQuery.getResultList();
        session.close();
        return list;
    }

    @Override
    public Long findSize() {
        Session session = sessionFactory.openSession();
        CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
        CriteriaQuery<Long> criteriaQuery = criteriaBuilder.createQuery(Long.class);
        Root<Order> root = criteriaQuery.from(Order.class);
        criteriaQuery.select(criteriaBuilder.count(root.get(ORDER_ID))).
                where(criteriaBuilder.equal(root.get(LOCK), 0));
        Long total = session.createQuery(criteriaQuery).getSingleResult();
        session.close();
        return total;
    }
}
