
package com.epam.esm.service;

import com.epam.esm.service.dto.entity.CertificateDto;
import com.epam.esm.service.exception.ServiceException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public interface CertificateService extends CRUDService<CertificateDto> {

    CertificateDto update(Integer id, Map<String, Object> updates) throws ServiceException;

    List<CertificateDto> findCertificatesByAnyParams(String[] tagNames, String substr, String[] sort, String sortDirection,
                                                  int skip, int limit);
}
