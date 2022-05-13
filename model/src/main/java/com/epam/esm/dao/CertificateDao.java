package com.epam.esm.dao;

import com.epam.esm.dao.entity.Certificate;
import com.epam.esm.dao.entity.Tag;

import java.util.List;
import java.util.Map;
import java.util.Optional;
/**
 * Interface {@code CertificateDao} describes operations for working with Certificate database table.
 */
public interface CertificateDao extends CRUDDao<Certificate> {
    /**
     * @param updates is a list with values for updates
     * @return updated Certificate
     */
    Optional<Certificate> update(long id, Map<String, Object> updates);

    /**
     * @param tags search by tag name
     * @param substr search by part of certificate name
     * @param skip values to display
     * @param limit quantity displayed
     * @param sort is name of sorting column method and sorting direction
     * @return Certificate which was found by parameters
     */
    List<Certificate> findByAnyParams(List<Tag> tags, String substr, Integer skip, Integer limit, String... sort);


}
