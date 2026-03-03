package ruben.springboot.service_management.models.mappers;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ruben.springboot.service_management.models.Address;
import ruben.springboot.service_management.models.Appliance;
import ruben.springboot.service_management.models.Client;
import ruben.springboot.service_management.models.WorkOrder;
import ruben.springboot.service_management.models.dtos.lists.AddressListDto;
import ruben.springboot.service_management.models.dtos.lists.ApplianceListDto;
import ruben.springboot.service_management.models.dtos.lists.ClientListDto;
import ruben.springboot.service_management.models.dtos.lists.WorkOrderListDto;
import ruben.springboot.service_management.models.dtos.requests.ClientRequestDto;
import ruben.springboot.service_management.models.dtos.responses.ClientOnlyResponseDto;
import ruben.springboot.service_management.models.dtos.responses.ClientResponseDto;
import ruben.springboot.service_management.repositories.ApplianceRepository;
import ruben.springboot.service_management.repositories.WorkOrderRepository;

@Service
public class ClientMapper {

    @Autowired
    private AddressMapper addressMapper;

    @Autowired
    private WorkOrderRepository workOrderRepository;

    @Autowired
    private ApplianceRepository applianceRepository;

    public Client toEntity(ClientRequestDto dto) {
        Client c = new Client();
        c.setName(dto.name);
        c.setPhone(dto.phone.trim());
        c.setPhone2(dto.phone2);
        c.setPhone3(dto.phone3);
        c.setPhone4(dto.phone4);
        c.setEmail(dto.email);
        c.setNotes(dto.notes);
        return c;
    }

    public Client update(ClientRequestDto dto, Client c) {
        c.setName(dto.name);
        c.setPhone(dto.phone.trim());
        c.setPhone2(dto.phone2);
        c.setPhone3(dto.phone3);
        c.setPhone4(dto.phone4);
        c.setEmail(dto.email);
        c.setNotes(dto.notes);
        return c;
    }

    public ClientResponseDto toResponse(Client c) {
        ClientResponseDto dto = new ClientResponseDto();

        dto.id = c.getId();
        dto.name = c.getName();
        dto.phone = c.getPhone();
        dto.phone2 = c.getPhone2();
        dto.phone3 = c.getPhone3();
        dto.phone4 = c.getPhone4();
        dto.email = c.getEmail();
        dto.notes = c.getNotes();
        dto.createdAt = c.getCreatedAt();

        List<WorkOrder> workOrders = workOrderRepository.findByClientId(c.getId());
        
        if (!workOrders.isEmpty()) {
            List<WorkOrderListDto> workOrdersList = new ArrayList<>();
            for (WorkOrder wo : workOrders) {
                workOrdersList.add(WorkOrderMapper.toList(wo));
            }
            dto.workOrders = workOrdersList;
        }
        
        if (c.getAddresses() != null) {
            List<AddressListDto> adressesList = new ArrayList<>();
            List<ApplianceListDto> appliancesList = new ArrayList<>();

            for (Address ad : c.getAddresses()) {
                adressesList.add(addressMapper.toList(ad));
                
                if (!applianceRepository.findByAddressId(ad.getId()).isEmpty()) {
                    for (Appliance ap : applianceRepository.findByAddressId(ad.getId())) {
                        appliancesList.add(ApplianceMapper.toList(ap));
                    }
                }
            }
            
            dto.addresses = adressesList;
            dto.appliances = appliancesList;
        }

        return dto;
    }

    public static ClientOnlyResponseDto toOnlyResponse(Client c) {
        ClientOnlyResponseDto dto = new ClientOnlyResponseDto();

        dto.id = c.getId();
        dto.name = c.getName();
        dto.phone = c.getPhone();
        dto.phone2 = c.getPhone2();
        dto.phone3 = c.getPhone3();
        dto.phone4 = c.getPhone4();
        dto.email = c.getEmail();
        dto.notes = c.getNotes();
        dto.createdAt = c.getCreatedAt();

        return dto;
    }

    public ClientListDto toListDto(Client c) {
        ClientListDto dto = new ClientListDto();
        dto.id = c.getId();
        dto.name = c.getName();
        dto.phone = c.getPhone();
        dto.phone2 = c.getPhone2();

        if (c.getAddresses() != null) {

            ArrayList<String> addressesNames = new ArrayList<>();
            ArrayList<String> addressesCities = new ArrayList<>();

            for (Address a : c.getAddresses()) {
                addressesNames.add(a.getAddress());
                addressesCities.add(a.getCity());
            }

            dto.addressesNames = addressesNames;
            dto.addressesCities = addressesCities;

        }

        return dto;
    }

}
