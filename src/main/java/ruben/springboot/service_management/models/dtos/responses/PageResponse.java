package ruben.springboot.service_management.models.dtos.responses;

import java.util.List;

public class PageResponse<T> {
    public List<T> content;
    public int currentPage;
    public int pageSize;
    public long totalItems;
    public long itemsInPage;;
    public int totalPages;
    public boolean first;
    public boolean last;
}
