package ruben.springboot.service_management.models.mappers;

import org.springframework.data.domain.Page;

import ruben.springboot.service_management.models.dtos.responses.PageResponseDto;

public class PageMapper {

    public static <T> PageResponseDto<T> toResponse(Page<T> page) {
        PageResponseDto<T> dto = new PageResponseDto<>();
        dto.content = page.getContent();
        dto.page = page.getNumber();
        dto.size = page.getSize();
        dto.totalElements = page.getTotalElements();
        dto.totalPages = page.getTotalPages();
        dto.first = page.isFirst();
        dto.last = page.isLast();
        return dto;
    }
}
