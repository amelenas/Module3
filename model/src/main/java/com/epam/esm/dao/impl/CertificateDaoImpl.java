package com.epam.esm.dao.impl;

import com.epam.esm.dao.CertificateDao;
import com.epam.esm.dao.entity.Certificate;
import com.epam.esm.dao.entity.CertificateTags;
import com.epam.esm.dao.entity.Tag;
import lombok.RequiredArgsConstructor;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;

@Repository
@RequiredArgsConstructor
@Transactional
public class CertificateDaoImpl implements CertificateDao {
    private final SessionFactory sessionFactory;
    private static final String ID = "id";
    private static final String DESCRIPTION = "description";
    private static final String TAG_ID = "tagId";
    private static final String NAME = "name";
    private static final String GIFT_ID = "giftId";
    private static final String SORTING_DESC = "DESC";

    public List<Certificate> findByAnyParams(List<Tag> tags, String substr, String sortDirection, Integer skip, Integer limit, String[] sort) {
        Session session = sessionFactory.openSession();
        CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
        CriteriaQuery<Certificate> criteriaQuery = criteriaBuilder.createQuery(Certificate.class);
        Root<Certificate> giftCertificateRoot = criteriaQuery.from(Certificate.class);
        Predicate finalPredicate = getFinalPredicate(tags, substr, session, criteriaBuilder, giftCertificateRoot);
        if (finalPredicate != null) {
            criteriaQuery.select(giftCertificateRoot).where(finalPredicate)
                    .groupBy(giftCertificateRoot.get(ID));
            orderByFieldName(sort[0], sortDirection, criteriaBuilder, criteriaQuery, giftCertificateRoot);
        } else {
            orderByFieldName(sort[0], sortDirection, criteriaBuilder, criteriaQuery, giftCertificateRoot);
        }
        orderByFieldName(sort[0], sortDirection, criteriaBuilder, criteriaQuery, giftCertificateRoot);
        return getPagingList(skip, limit, session, criteriaQuery);
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
                .where(criteriaBuilder.equal(tagRoot.get(NAME), tags.get(0).getName()));
        List<Integer> tagIdList = session.createQuery(findTagId).getResultList();
        CriteriaQuery<Integer> findGiftId = criteriaBuilder.createQuery(Integer.class);
        Root<CertificateTags> root = findGiftId.from(CertificateTags.class);
        Predicate having = criteriaBuilder.equal(criteriaBuilder.count(root.get(GIFT_ID)), tags.size());
        findGiftId.select(root.get(GIFT_ID)).where(root.get(TAG_ID).in(tagIdList)).groupBy(root.get(GIFT_ID)).having(having);
        List<Integer> giftIdList = session.createQuery(findGiftId).getResultList();
        Expression<Integer> giftId = giftCertificateRoot.get(ID);
        return giftId.in(giftIdList);
    }

    private Predicate getLikeNameOrDescriptionPredicate(String substr, CriteriaBuilder criteriaBuilder,
                                                        Root<Certificate> giftCertificateRoot) {
        Expression<String> name = giftCertificateRoot.get(NAME);
        Expression<String> description = giftCertificateRoot.get(DESCRIPTION);
        Predicate likeName = criteriaBuilder.like(name, '%' + substr + '%');
        Predicate likeDescription = criteriaBuilder.like(description, '%' + substr + '%');
        return criteriaBuilder.or(likeName, likeDescription);
    }

    private void orderByFieldName(String sort, String sortDirection, CriteriaBuilder criteriaBuilder,
                                  CriteriaQuery<Certificate> criteriaQuery, Root<Certificate> giftCertificateRoot) {
        List<Order> orderList = new ArrayList<>();
        if (sortDirection.contains(SORTING_DESC)) {
            orderList.add(criteriaBuilder.desc(giftCertificateRoot.get(sort)));
        } else {
            orderList.add(criteriaBuilder.asc(giftCertificateRoot.get(sort)));
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
