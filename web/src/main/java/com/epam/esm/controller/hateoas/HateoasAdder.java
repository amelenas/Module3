package com.epam.esm.controller.hateoas;

import org.springframework.hateoas.RepresentationModel;

public interface HateoasAdder <T extends RepresentationModel<T>> {
    void addLinks(T entity);

}
