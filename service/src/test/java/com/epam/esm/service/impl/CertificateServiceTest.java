package com.epam.esm.service.impl;

import com.epam.esm.dao.CertificateRepository;
import com.epam.esm.dao.entity.Certificate;
import com.epam.esm.dao.entity.Tag;
import com.epam.esm.service.dto.DtoConverter;
import com.epam.esm.service.dto.entity.CertificateDto;
import com.epam.esm.service.dto.impl.CertificateDtoConverterImpl;
import com.epam.esm.service.exception.ServiceException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.Instant;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CertificateServiceTest {
    private final Certificate certificate = new Certificate(2, "Pet Store",
            "Pet Store", 200.0, 12, Instant.EPOCH, Instant.EPOCH);
    private final CertificateDto certificateDtoValid = CertificateDto.builder().description("Pet Store").name("Pet Store").price(200.0)
            .duration(12).lastUpdateDate(Instant.EPOCH).createDate(Instant.EPOCH).build();

    private final CertificateDto certificateDtoNotValid = CertificateDto.builder().id(2).name(null)
            .description(null).price(-200.0).duration(36).createDate(Instant.EPOCH)
            .lastUpdateDate(Instant.EPOCH).build();
    @InjectMocks
    private DtoConverter<Certificate, CertificateDto> dtoConverter = Mockito.mock(CertificateDtoConverterImpl.class);
    private final Map<String, Object> values = new HashMap<>();
    private final Set<Tag> tagNames = new HashSet<>();

    @Mock
    private CertificateRepository certificateRepository;

    @InjectMocks
    private CertificateServiceImpl certificateService;

    @BeforeEach
    void addValues() {

        tagNames.add(new Tag("Animal"));
        values.put("name", "Dogs");
        values.put("description", "1 dog");
        values.put("price", 200.0);
        values.put("duration", 12);
        values.put("tagNames", tagNames);
    }

    @Test
    void createCertificatesException_NotValidEntity_Test() {
        when(dtoConverter.convertToEntity(certificateDtoValid)).thenReturn(certificate);
        assertThrows(ServiceException.class, () ->
                certificateService.create(certificateDtoNotValid));
    }

    @Test
    void updateCertificate_NotValid_Test() throws ServiceException {
        assertThrows(ServiceException.class, () -> certificateService.update(2, values));
    }
    @Test
    void findCertificates_PositiveTest() throws ServiceException {
        Pageable pageable = PageRequest.of(2, 2);
        when(certificateRepository.findAll(pageable)).thenReturn(mockPage());
        assertEquals(expectedPage().getTotalElements(), certificateService.findAll(pageable).getTotalElements());

    }

    @Test
    void deleteCertificate_PositiveTest() throws ServiceException {
        when(certificateRepository.existsById(2)).thenReturn(true);
        certificateService.delete(2);
        Mockito.verify(certificateRepository).deleteById(certificate.getId());
    }

    @Test
    void deleteCertificateException_NotValidDataTest() {
         assertThrows(ServiceException.class, () -> certificateService.delete(-1));
    }

    @Test
    void findCertificate_PositiveTest() throws ServiceException {
        Mockito.when(certificateRepository.findById(2)).thenReturn(Optional.of(certificate));
        certificateService.find(2);
        Mockito.verify(certificateRepository).findById(2);
    }

    private Page<Certificate> mockPage() {
        List<Certificate> mockList = new ArrayList<>();
        mockList.add(Certificate.builder().id(1).name("Gift1").price(125.3).duration(125).description("new1").build());
        mockList.add(Certificate.builder().id(2).name("Gift2").price(125.5).duration(56).description("new2").build());
        mockList.add(Certificate.builder().id(3).name("Gift3").price(125.7).duration(75).description("new3").build());
        return new PageImpl<>(mockList);
    }

    private Page<CertificateDto>  expectedPage() {
        List<CertificateDto> expList = new ArrayList<>();
        expList.add(CertificateDto.builder().id(1).name("Gift1").price(125.3).duration(125).description("new1").build());
        expList.add(CertificateDto.builder().id(2).name("Gift2").price(125.5).duration(56).description("new2").build());
        expList.add(CertificateDto.builder().id(3).name("Gift3").price(125.7).duration(75).description("new3").build());
        return new PageImpl<>(expList);
    }
}
