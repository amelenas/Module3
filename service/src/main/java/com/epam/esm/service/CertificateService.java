package com.epam.esm.service;

import com.epam.esm.service.dto.entity.CertificateDto;
import com.epam.esm.service.exception.ServiceException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public interface CertificateService extends CRUDService<CertificateDto> {

    /**
     * @param updates is a list with values for updates
     * @return updated Certificate
     */
    CertificateDto update(long id, Map<String, Object> updates) throws ServiceException;

    /**
     * @param tagNames search by tag name
     * @param substr search by part of certificate name
     * @param skip values to display
     * @param limit quantity displayed
     * @param sort is name of sorting column method and sorting direction
     * @return Certificate which was found by parameters
     */
    List<CertificateDto> findCertificatesByAnyParams(String[] tagNames, String substr, String[] sort,
                                                  int skip, int limit);

}