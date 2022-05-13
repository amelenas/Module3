package com.epam.esm.service.impl;

import com.epam.esm.dao.entity.Certificate;
import com.epam.esm.dao.entity.Tag;
import com.epam.esm.dao.impl.CertificateDaoImpl;
import com.epam.esm.service.dto.DtoConverter;
import com.epam.esm.service.dto.entity.CertificateDto;
import com.epam.esm.service.dto.impl.CertificateDtoConverterImpl;
import com.epam.esm.service.exception.ServiceException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
class CertificateServiceTest {
   private Certificate certificate = new Certificate(2, "Pet Store",
            "Pet Store", 200.0, 12, null, null);
   private Certificate certificateNotValid = new Certificate( null,
            null, 0, 0, null );

    @Mock
    private CertificateDaoImpl certificateDao = Mockito.mock(CertificateDaoImpl.class);
    @Mock
    DtoConverter<Certificate, CertificateDto> dtoConverter = Mockito.mock(CertificateDtoConverterImpl.class);

    @InjectMocks
    private CertificateServiceImpl certificateService;


    @Test
    void createCertificatesException_NotValidEntity_Test() {
       Assertions.assertThrows(ServiceException.class, () -> certificateService.create(dtoConverter.convertToDto(certificateNotValid)));
    }

    @Test
    void updateCertificate_Parts_PositiveTest() throws ServiceException {
        Set<Tag> tagNames = new HashSet<>();
        tagNames.add(new Tag("Animal"));
        Certificate certificateChanged = new Certificate("Dogs", "1 dog",
                12.00, 12,  tagNames);

        Map<String, Object> values = new HashMap<>();
        values.put("name", "Dogs");
        values.put("description", "1 dog");
        values.put("price", 200.0);
        values.put("duration", 12);
        values.put("tagNames", tagNames);
        Mockito.when(certificateDao.update(2, values)).
                thenReturn(Optional.of(certificateChanged));
        certificateService.update(2, values);
        Mockito.verify(certificateDao).update(2, values);
    }

    @Test
    void findCertificates_PositiveTest () throws ServiceException {
        ArrayList<Certificate>certificates = new ArrayList<>();
        certificates.add(certificate);
        certificates.add(certificateNotValid);
        Mockito.when(certificateDao.findAll(1,15)).thenReturn(certificates);
        certificateService.findAll(1,15);
        Mockito.verify(certificateDao).findAll(1,15);
    }

    @Test
    void deleteCertificate_PositiveTest() throws ServiceException {
        certificateService.delete(2);
        Mockito.verify(certificateDao).delete(certificate.getId());
    }

    @Test
    void deleteCertificateException_NotValidDataTest(){
        assertThrows(ServiceException.class, () -> certificateService.delete(certificateNotValid.getId()));
    }

    @Test
    void findCertificate_PositiveTest () throws ServiceException {
        Mockito.when(certificateDao.find(2)).thenReturn(Optional.of(certificate));
        certificateService.find(2);
        Mockito.verify(certificateDao).find(2);
    }
}
