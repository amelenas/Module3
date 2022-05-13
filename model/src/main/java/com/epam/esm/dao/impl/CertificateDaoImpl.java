package com.epam.esm.dao.impl;

import com.epam.esm.dao.CertificateDao;
import com.epam.esm.dao.entity.Certificate;
import com.epam.esm.dao.entity.CertificateTags;
import com.epam.esm.dao.entity.Tag;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public class CertificateDaoImpl implements CertificateDao {

    private final SessionFactory sessionFactory;

    private static final String LOCK = "lock";
    private static final String ID = "id";
    private static final String TAG_NAMES = "tagNames";
    private static final String NAME = "name";
    private static final String GIFT_ID = "giftId";

    private static final Integer LOCK_VALUE_0 = 0;
    private static final Integer LOCK_VALUE_1 = 1;

    @Autowired
    public CertificateDaoImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public Optional<Certificate> create(Certificate certificate) {
        Certificate result;
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        session.saveOrUpdate(certificate);
        session.getTransaction().commit();
        result = session.load(Certificate.class, session.getIdentifier(certificate));
        session.close();
        return Optional.of(result);
    }

    @Override
    public boolean delete(long id) {
        Session session = sessionFactory.openSession();
        CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
        CriteriaUpdate<Certificate> criteriaUpdate = criteriaBuilder.createCriteriaUpdate(Certificate.class);
        Root<Certificate> root = criteriaUpdate.from(Certificate.class);
        criteriaUpdate.set(LOCK, LOCK_VALUE_1);
        criteriaUpdate.where(criteriaBuilder.equal(root.get(ID), id));
        session.beginTransaction();
        int size = session.createQuery(criteriaUpdate).executeUpdate();
        session.getTransaction().commit();
        session.close();
        return size > 0;
    }

    @Override
    public List<Certificate> findAll(int page, int size) {
        Session session = sessionFactory.openSession();
        CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
        CriteriaQuery<Certificate> criteriaQuery = criteriaBuilder.createQuery(Certificate.class);
        Root<Certificate> root = criteriaQuery.from(Certificate.class);
        criteriaQuery.select(root).where(criteriaBuilder.equal(root.get(LOCK), LOCK_VALUE_0)).
                orderBy(criteriaBuilder.asc(root.get(ID)));
        TypedQuery<Certificate> typedQuery = session.createQuery(criteriaQuery);
        typedQuery.setFirstResult(page);
        typedQuery.setMaxResults(size);
        List<Certificate> list = typedQuery.getResultList();
        session.close();
        return list;
    }

    @Override
    public Optional<Certificate> find(long id) {
        Session session = sessionFactory.openSession();
        CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
        CriteriaQuery<Certificate> criteriaQuery = criteriaBuilder.createQuery(Certificate.class);
        Root<Certificate> root = criteriaQuery.from(Certificate.class);
        root.fetch(TAG_NAMES, JoinType.LEFT);
        criteriaQuery.select(root).where(criteriaBuilder.equal(root.get(ID), id),
                criteriaBuilder.equal(root.get(LOCK), LOCK_VALUE_0));
        Optional<Certificate> certificate = session.createQuery(criteriaQuery).getResultStream().findFirst();
        session.close();
        return certificate;
    }

    @Override
    public Optional<Certificate> update(long id, Certificate certificate) {
        Certificate result;
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        session.update(certificate);
        session.getTransaction().commit();
        result = session.load(Certificate.class, session.getIdentifier(certificate));
        session.close();
        return Optional.of(result);
    }

    @Override
    public Optional<Certificate> update(long id, Map<String, Object> updates) {
        updates.put("lastUpdateDate", Instant.now());
        Session session = sessionFactory.openSession();
        CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
        CriteriaUpdate<Certificate> criteriaUpdate = criteriaBuilder.createCriteriaUpdate(Certificate.class);
        Root<Certificate> root = criteriaUpdate.from(Certificate.class);
        updates.forEach(criteriaUpdate::set);
        criteriaUpdate.where(criteriaBuilder.equal(root.get(ID), id));
        session.beginTransaction();
        session.createQuery(criteriaUpdate).executeUpdate();
        session.getTransaction().commit();
        session.close();
        return find(id);
    }

    @Override
    public List<Certificate> findByAnyParams(List<Tag> tags, String substr, Integer skip, Integer limit, String[] sort) {
        Session session = sessionFactory.openSession();
        CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
        CriteriaQuery<Certificate> criteriaQuery = criteriaBuilder.createQuery(Certificate.class);
        Root<Certificate> giftCertificateRoot = criteriaQuery.from(Certificate.class);
        Predicate finalPredicate = getFinalPredicate(tags, substr, session, criteriaBuilder, giftCertificateRoot);
        if (finalPredicate != null) {
            criteriaQuery.select(giftCertificateRoot).where(finalPredicate, criteriaBuilder.equal(giftCertificateRoot.get(LOCK), LOCK_VALUE_0))
                    .groupBy(giftCertificateRoot.get(ID));
            orderByFieldName(sort, criteriaBuilder, criteriaQuery, giftCertificateRoot);
        }else {
            orderByFieldName(sort, criteriaBuilder, criteriaQuery.where(criteriaBuilder.equal(giftCertificateRoot.get(LOCK), LOCK_VALUE_0)), giftCertificateRoot);
        }
        orderByFieldName(sort, criteriaBuilder, criteriaQuery, giftCertificateRoot);
        List<Certificate> finalList = getPagingList(skip, limit, session, criteriaQuery);
        session.close();
        return finalList;

    }

    @Override
    public long findSize() {
        Session session = sessionFactory.openSession();
        CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
        CriteriaQuery<Long> criteriaQuery = criteriaBuilder.createQuery(Long.class);
        Root<Certificate> giftCertificateRoot = criteriaQuery.from(Certificate.class);
        criteriaQuery.select(criteriaBuilder.count(giftCertificateRoot.get(ID)))
                .where(criteriaBuilder.equal(giftCertificateRoot.get(LOCK), LOCK_VALUE_0));
        Long total = session.createQuery(criteriaQuery).getSingleResult();
        session.close();
        return total;
    }

    private Predicate getFinalPredicate(List<Tag> tags, String substr, Session session, CriteriaBuilder criteriaBuilder,
                                        Root<Certificate> giftCertificateRoot) {
        Predicate finalPredicate = null;
        Predicate certificateIdIn = (tags != null) ? getGiftIdInPredicate(tags, session, criteriaBuilder, giftCertificateRoot) : null;
        Predicate likeNameOrDescription = (substr != null) ? getLikeNameOrDescriptionPredicate(substr, criteriaBuilder,
                giftCertificateRoot) : null;
        if (certificateIdIn != null && likeNameOrDescription != null) {
            finalPredicate = criteriaBuilder.and(certificateIdIn, likeNameOrDescription);
        } else if (certificateIdIn == null && likeNameOrDescription != null) {
            finalPredicate = likeNameOrDescription;
        } else if (certificateIdIn != null) {
            finalPredicate = certificateIdIn;
        }
        return finalPredicate;
    }

    private Predicate getGiftIdInPredicate(List<Tag> tags, Session session, CriteriaBuilder criteriaBuilder,
                                           Root<Certificate> giftCertificateRoot) {

        CriteriaQuery<Integer> findTagId = criteriaBuilder.createQuery(Integer.class);
        Root<Tag> tagRoot = findTagId.from(Tag.class);
        findTagId.select(tagRoot.get(ID))
                .where(criteriaBuilder.equal(tagRoot.get(NAME), tags.get(0).getName()),
                        criteriaBuilder.equal(tagRoot.get(LOCK), LOCK_VALUE_0));
        List<Integer> tagIdList = session.createQuery(findTagId).getResultList();
        CriteriaQuery<Integer> findGiftId = criteriaBuilder.createQuery(Integer.class);
        Root<CertificateTags> root = findGiftId.from(CertificateTags.class);
        Predicate having = criteriaBuilder.equal(criteriaBuilder.count(root.get(GIFT_ID)), tags.size());
        findGiftId.select(root.get(GIFT_ID)).where(root.get("tagId").in(tagIdList)).groupBy(root.get(GIFT_ID)).having(having);
        List<Integer> giftIdList = session.createQuery(findGiftId).getResultList();
        Expression<Integer> giftId = giftCertificateRoot.get(ID);
        return giftId.in(giftIdList);
    }

    private Predicate getLikeNameOrDescriptionPredicate(String substr, CriteriaBuilder criteriaBuilder,
                                                        Root<Certificate> giftCertificateRoot) {
        Expression<String> name = giftCertificateRoot.get(NAME);
        Expression<String> description = giftCertificateRoot.get("description");
        Predicate likeName = criteriaBuilder.like(name, '%' + substr + '%');
        Predicate likeDescription = criteriaBuilder.like(description, '%' + substr + '%');
        return criteriaBuilder.or(likeName, likeDescription);
    }

    private void orderByFieldName(String[] sort, CriteriaBuilder criteriaBuilder,
                                  CriteriaQuery<Certificate> criteriaQuery, Root<Certificate> giftCertificateRoot) {
        List<Order> orderList = new ArrayList<>();
        for (String s : sort) {
            if (s.startsWith("-")) {
                orderList.add(criteriaBuilder.desc(giftCertificateRoot.get(s.substring(1))));
            } else {
                orderList.add(criteriaBuilder.asc(giftCertificateRoot.get(s)));
            }
        }
        criteriaQuery.orderBy(orderList);
    }

    private List<Certificate> getPagingList(Integer skip, Integer limit, Session session,
                                            CriteriaQuery<Certificate> criteriaQuery) {
        TypedQuery<Certificate> typedQuery = session.createQuery(criteriaQuery);
        typedQuery.setFirstResult(skip);
        typedQuery.setMaxResults(limit);
        return typedQuery.getResultList();
    }

}
