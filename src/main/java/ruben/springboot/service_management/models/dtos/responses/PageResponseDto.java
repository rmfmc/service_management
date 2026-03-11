package ruben.springboot.service_management.models.dtos.responses;

import java.util.List;

public class PageResponseDto<T> {
    public List<T> content;
    public int page;
    public int size;
    public long totalElements;
    public int totalPages;
    public boolean first;
    public boolean last;
}
