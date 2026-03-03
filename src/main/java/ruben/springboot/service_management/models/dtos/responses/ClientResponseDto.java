package ruben.springboot.service_management.models.dtos.responses;

import java.time.LocalDateTime;
import java.util.List;

import ruben.springboot.service_management.models.dtos.lists.AddressListDto;
import ruben.springboot.service_management.models.dtos.lists.ApplianceListDto;
import ruben.springboot.service_management.models.dtos.lists.WorkOrderListDto;

public class ClientResponseDto {
    
    public Long id;
    public String name;
    public String phone;
    public String phone2;
    public String phone3;
    public String phone4;
    public String email;
    public String notes;
    public LocalDateTime createdAt;

    public List<WorkOrderListDto> workOrders;
    public List<AddressListDto> addresses;
    public List<ApplianceListDto> appliances;

}
