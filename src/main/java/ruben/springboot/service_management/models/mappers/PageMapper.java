package ruben.springboot.service_management.models.mappers;

import org.springframework.data.domain.Page;

import ruben.springboot.service_management.models.dtos.responses.PageResponse;

public class PageMapper {

    public static <T> PageResponse<T> toResponse(Page<T> page) {
        PageResponse<T> dto = new PageResponse<>();
        dto.content = page.getContent();
        dto.currentPage = page.getNumber();
        dto.pageSize = page.getSize();
        dto.totalItems = page.getTotalElements();
        dto.itemsInPage = page.getNumberOfElements();
        dto.totalPages = page.getTotalPages();
        dto.first = page.isFirst();
        dto.last = page.isLast();
        return dto;
    }
}
