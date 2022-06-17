
package com.epam.esm.service;

import com.epam.esm.service.dto.entity.TagDto;

public interface TagService extends CRUDService<TagDto> {

    TagDto findMostPopularTagOfUserWithHighestCostOfOrder();

}

