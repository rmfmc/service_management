package ruben.springboot.service_management.models.mappers;

import ruben.springboot.service_management.models.Appliance;
import ruben.springboot.service_management.models.Client;
import ruben.springboot.service_management.models.dtos.requests.ApplianceRequestDto;
import ruben.springboot.service_management.models.dtos.responses.ApplianceResponseDto;

public class ApplianceMapper {

    public static Appliance toEntity(ApplianceRequestDto req, Client client) {
        Appliance a = new Appliance();
        a.setId(req.id);
        a.setClient(client);
        a.setApplianceType(req.applianceType);
        a.setBrand(req.brand);
        a.setModel(req.model);
        a.setSerialNumber(req.serialNumber);

        if (req.active != null)
            a.setActive(req.active);

        return a;
    }

    public static void updateEntity(Appliance a, ApplianceRequestDto req, Client client) {
        a.setClient(client);
        a.setApplianceType(req.applianceType);
        a.setBrand(req.brand);
        a.setModel(req.model);
        a.setSerialNumber(req.serialNumber);

        if (req.active != null)
            a.setActive(req.active);
    }

    public static ApplianceResponseDto toResponse(Appliance a) {
        ApplianceResponseDto dto = new ApplianceResponseDto();
        dto.id = a.getId();

        dto.clientId = a.getClient().getId();
        dto.clientName = a.getClient().getName();
        dto.clientPhone = a.getClient().getPhone();

        dto.applianceType = a.getApplianceType();
        dto.brand = a.getBrand();
        dto.model = a.getModel();
        dto.serialNumber = a.getSerialNumber();
        dto.active = a.isActive();
        return dto;
    }

}
