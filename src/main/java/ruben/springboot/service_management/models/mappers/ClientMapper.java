package ruben.springboot.service_management.models.mappers;

import java.util.ArrayList;
import java.util.Collections;
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
    private WorkOrderRepository workOrderRepository;

    @Autowired
    private ApplianceRepository applianceRepository;

    public Client toEntity(ClientRequestDto dto) {
        Client c = new Client();
        resolve(c, dto);
        return c;
    }

    public Client update(ClientRequestDto dto, Client c) {
        resolve(c, dto);
        return c;
    }

    public ClientResponseDto toResponse(Client c) {

        ClientResponseDto dto = new ClientResponseDto();
        resolveToDto(dto, c);

        List<WorkOrder> workOrders = workOrderRepository.findByClientId(c.getId());

        if (!workOrders.isEmpty()) {
            List<WorkOrderListDto> workOrdersList = new ArrayList<>();
            for (WorkOrder wo : workOrders) {
                workOrdersList.add(WorkOrderMapper.toList(wo));
            }
            dto.workOrders = workOrdersList;
        }

        if (c.getAddresses() != null) {
            List<AddressListDto> addressesList = new ArrayList<>();
            List<Long> addressIds = new ArrayList<>();

            for (Address ad : c.getAddresses()) {
                addressesList.add(AddressMapper.toList(ad));
                addressIds.add(ad.getId());
            }

            List<ApplianceListDto> appliancesList = new ArrayList<>();
            for (Appliance ap : findAppliancesByAddressIds(addressIds)) {
                appliancesList.add(ApplianceMapper.toList(ap));
            }

            dto.addresses = addressesList;
            dto.appliances = appliancesList;
        }

        return dto;
    }

    public static ClientOnlyResponseDto toOnlyResponse(Client c) {
        
        ClientOnlyResponseDto dto = new ClientOnlyResponseDto();
        resolveToOnlyDto(dto, c);
        return dto;
    }

    public static ClientListDto toList(Client c) {
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

    // HELPER
    private List<Appliance> findAppliancesByAddressIds(List<Long> addressIds) {
        if (addressIds == null || addressIds.isEmpty()) {
            return Collections.emptyList();
        }

        return applianceRepository.findByAddressIdInWithTypeAndBrand(addressIds);
    }

    // HELPER
    private static Client resolve(Client c, ClientRequestDto dto) {
        c.setName(dto.name);
        c.setPhone(dto.phone.trim());
        c.setPhone2(dto.phone2);
        c.setPhone3(dto.phone3);
        c.setPhone4(dto.phone4);
        c.setEmail(dto.email);
        c.setNotes(dto.notes);
        return c;
    }

    // HELPER
    private static ClientResponseDto resolveToDto(ClientResponseDto dto, Client c) {
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

    // HELPER
    private static ClientOnlyResponseDto resolveToOnlyDto(ClientOnlyResponseDto dto, Client c) {
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

}
