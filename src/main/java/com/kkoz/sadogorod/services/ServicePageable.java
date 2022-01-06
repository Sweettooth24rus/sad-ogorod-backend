package com.kkoz.sadogorod.services;

import com.kkoz.sadogorod.utils.OrderUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
public class ServicePageable {
    public Pageable getPageConfig(Integer page, Integer size, String sort) {
        Set<String> sortingFields = new LinkedHashSet<>(
                Arrays.asList(
                        StringUtils.split(
                                StringUtils.defaultIfEmpty(sort, ""),
                                ","
                        )
                )
        );

        List<Sort.Order> sortingOrder = sortingFields.stream()
                .map(OrderUtil::getOrder)
                .collect(Collectors.toList());

        Sort sortData = sortingOrder.isEmpty() ? null : Sort.by(sortingOrder);

        return (sortData != null) ? PageRequest.of(page, size, sortData) : PageRequest.of(page, size);
    }
}
