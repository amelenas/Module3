package com.epam.esm.dao.impl;

import com.epam.esm.dao.TagDao;
import com.epam.esm.dao.entity.Certificate;
import com.epam.esm.dao.entity.Order;
import com.epam.esm.dao.entity.Tag;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Repository
public class TagDaoImpl implements TagDao {
    private final SessionFactory sessionFactory;

    private static final String LOCK = "lock";
    private static final String USER_ID = "userId";
    private static final String TAG_ID = "id";
    private static final String NAME = "name";

    private static final Integer LOCK_VALUE_0 = 0;
    private static final Integer LOCK_VALUE_1 = 1;

    @Autowired
    public TagDaoImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public Optional<Tag> create(Tag tag) {
        Tag result;
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        session.saveOrUpdate(tag);
        session.getTransaction().commit();
        result = session.load(Tag.class, session.getIdentifier(tag));
        session.close();
        return Optional.of(result);
    }

    @Override
    public List<Tag> findAll(int page, int size) {
        Session session = sessionFactory.openSession();
        CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
        CriteriaQuery<Tag> criteriaQuery = criteriaBuilder.createQuery(Tag.class);
        Root<Tag> root = criteriaQuery.from(Tag.class);
        criteriaQuery.select(root).where(criteriaBuilder.equal(root.get(LOCK), LOCK_VALUE_0)).
                orderBy(criteriaBuilder.asc(root.get(TAG_ID)));
        TypedQuery<Tag> typedQuery = session.createQuery(criteriaQuery);
        typedQuery.setFirstResult(page);
        typedQuery.setMaxResults(size);
        List<Tag> list = typedQuery.getResultList();
        session.close();
        return list;
    }


    @Override
    public Optional<Tag> find(long id) {
        Session session = sessionFactory.openSession();
        CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
        CriteriaQuery<Tag> criteriaQuery = criteriaBuilder.createQuery(Tag.class);
        Root<Tag> root = criteriaQuery.from(Tag.class);
        criteriaQuery.select(root).where(criteriaBuilder.equal(root.get(TAG_ID), id),
                criteriaBuilder.equal(root.get(LOCK), LOCK_VALUE_0));
        Optional<Tag> tag = session.createQuery(criteriaQuery).getResultStream().findFirst();
        session.close();
        return tag;
    }

    @Override
    public Optional<Tag> update(long id, Tag tag) {
        Session session = sessionFactory.openSession();
        CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
        CriteriaUpdate<Tag> criteriaUpdate = criteriaBuilder.createCriteriaUpdate(Tag.class);
        Root<Tag> root = criteriaUpdate.from(Tag.class);
        criteriaUpdate.set(NAME, tag.getName());
        criteriaUpdate.where(criteriaBuilder.equal(root.get(TAG_ID), id));
        session.beginTransaction();
        session.createQuery(criteriaUpdate).executeUpdate();
        session.getTransaction().commit();
        session.close();
        return find(id);
    }

    @Override
    public boolean delete(long id) {
        Session session = sessionFactory.openSession();
        CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
        CriteriaUpdate<Tag> criteriaUpdate = criteriaBuilder.createCriteriaUpdate(Tag.class);
        Root<Tag> root = criteriaUpdate.from(Tag.class);
        criteriaUpdate.set("lock", LOCK_VALUE_1).
                where(criteriaBuilder.equal(root.get(TAG_ID), id));
        session.beginTransaction();
        int size = session.createQuery(criteriaUpdate).executeUpdate();
        session.getTransaction().commit();
        session.close();
        return size > 0;
    }

    @Override
    public Optional<Tag> findByName(String name) {
        Session session = sessionFactory.openSession();
        CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
        CriteriaQuery<Tag> criteriaQuery = criteriaBuilder.createQuery(Tag.class);
        Root<Tag> root = criteriaQuery.from(Tag.class);
        criteriaQuery.select(root).where(criteriaBuilder.equal(root.get("name"), name),
                criteriaBuilder.equal(root.get(LOCK), LOCK_VALUE_0));
        Optional<Tag> tag = session.createQuery(criteriaQuery).getResultStream().findFirst();
        session.close();
        return tag;
    }

    @Override
    public Optional<Tag> findMostPopularTagOfUserWithHighestCostOfOrder() {
        Long maxOccurredElement = findCertificatesIdByUserId(findIdOfUserWhoHaveTheHighestCostOfOrders());

        Session session = sessionFactory.openSession();
        CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
        CriteriaQuery<Tag> criteriaQuery = criteriaBuilder.createQuery(Tag.class);
        Root<Tag> root = criteriaQuery.from(Tag.class);
        Join<Tag, Certificate> join = root.join("certificates", JoinType.INNER);
        Expression<Long> exp = join.get(TAG_ID);
        criteriaQuery.select(root).where(exp.in(maxOccurredElement),
                        criteriaBuilder.equal(root.get(LOCK), LOCK_VALUE_0));
        Query<Tag> query = session.createQuery(criteriaQuery);
        query.setFirstResult(0);
        query.setMaxResults(1);
        Tag tag = query.getSingleResult();
        session.close();
        return Optional.of(tag);
    }

    @Override
    public long findSize() {
        Session session = sessionFactory.openSession();
        CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
        CriteriaQuery<Long> criteriaQuery = criteriaBuilder.createQuery(Long.class);
        Root<Tag> root = criteriaQuery.from(Tag.class);
        criteriaQuery.select(criteriaBuilder.count(root.get(TAG_ID))).
                where(criteriaBuilder.equal(root.get(LOCK), LOCK_VALUE_0));
        Long total = session.createQuery(criteriaQuery).getSingleResult();
        session.close();
        return total;
    }

    private Long findIdOfUserWhoHaveTheHighestCostOfOrders() {
        Session session = sessionFactory.openSession();
        CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
        CriteriaQuery<Long> criteriaQuery = criteriaBuilder.createQuery(Long.class);
        Root<Order> root = criteriaQuery.from(Order.class);
        criteriaQuery.select(root.get(USER_ID)).where(criteriaBuilder.equal(root.get(LOCK), LOCK_VALUE_0))
                .groupBy(root.get(USER_ID))
                .orderBy(criteriaBuilder.desc(criteriaBuilder.sum(root.get("cost"))));
        Query<Long> query = session.createQuery(criteriaQuery);
        query.setFirstResult(0);
        query.setMaxResults(1);
        Long userId = query.getSingleResult();
        session.close();
        return userId;
    }

    private Long findCertificatesIdByUserId(long userId) {
        Session session = sessionFactory.openSession();
        CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
        CriteriaQuery<Long> criteriaQuery = criteriaBuilder.createQuery(Long.class);
        Root<Order> root = criteriaQuery.from(Order.class);
        criteriaQuery.select(root.get("certificateId")).where(criteriaBuilder.equal(root.get(USER_ID), userId),
                criteriaBuilder.equal(root.get(LOCK), LOCK_VALUE_0));
        List<Long> resultList = session.createQuery(criteriaQuery).getResultList();
        session.close();
        return resultList.stream()
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()))
                .entrySet().stream().max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey).orElse(null);
    }
}
