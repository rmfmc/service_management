package ruben.springboot.service_management.models.mappers;

import ruben.springboot.service_management.authentication.SecurityUtils;
import ruben.springboot.service_management.models.WorkOrderCharge;
import ruben.springboot.service_management.models.dtos.lists.WorkOrderChargeListDto;
import ruben.springboot.service_management.models.dtos.requests.WorkOrderChargeRequestDto;
import ruben.springboot.service_management.models.dtos.responses.WorkOrderChargeOnlyResponseDto;
import ruben.springboot.service_management.models.dtos.responses.WorkOrderChargeResponseDto;

public class WorkOrderChargeMapper {

    public static WorkOrderCharge toEntity(WorkOrderChargeRequestDto dto) {

        Long currentUserId = SecurityUtils.currentUserId();
        String currentUserName = SecurityUtils.currentUserName();

        WorkOrderCharge woc = new WorkOrderCharge();
        woc.setCreatedUserId(currentUserId);
        woc.setCreatedUserName(currentUserName);
        woc.setChargeType(dto.chargeType);
        woc.setPaymentMethod(dto.paymentMethod);
        woc.setDescription(dto.description);
        woc.setPrice(dto.price);
        woc.setPayer(dto.payer);
        woc.setPaid(dto.paid != null ? dto.paid : true);

        return woc;
    }

    public static WorkOrderCharge update(WorkOrderChargeRequestDto dto, WorkOrderCharge woc) {

        woc.setChargeType(dto.chargeType);
        woc.setPaymentMethod(dto.paymentMethod);
        woc.setDescription(dto.description);
        woc.setPrice(dto.price);
        woc.setPayer(dto.payer);
        woc.setPaid(dto.paid != null ? dto.paid : true);

        return woc;
    }

    public static WorkOrderChargeResponseDto toResponse(WorkOrderCharge charge) {

        WorkOrderChargeResponseDto dto = new WorkOrderChargeResponseDto();

        dto.id = charge.getId();
        dto.chargeType = charge.getChargeType().getLabelEs();
        dto.paymentMethod = charge.getPaymentMethod().getLabelEs();
        dto.description = charge.getDescription();
        dto.price = charge.getPrice();
        dto.payer = charge.getPayer();
        dto.paid = charge.getPaid();
        dto.workOrder = WorkOrderMapper.toList(charge.getWorkOrder());
        dto.client = ClientMapper.toList(charge.getWorkOrder().getClient());
        dto.createdUserId = charge.getCreatedUserId();
        dto.createdUserName = charge.getCreatedUserName();
        dto.createdAt = charge.getCreatedAt();

        return dto;
    }

    public static WorkOrderChargeOnlyResponseDto toOnlyResponse(WorkOrderCharge charge) {

        WorkOrderChargeOnlyResponseDto dto = new WorkOrderChargeOnlyResponseDto();

        dto.id = charge.getId();
        dto.chargeType = charge.getChargeType().getLabelEs();
        dto.paymentMethod = charge.getPaymentMethod().getLabelEs();
        dto.description = charge.getDescription();
        dto.price = charge.getPrice();
        dto.payer = charge.getPayer();
        dto.paid = charge.getPaid();
        dto.createdUserId = charge.getCreatedUserId();
        dto.createdUserName = charge.getCreatedUserName();
        dto.createdAt = charge.getCreatedAt();

        return dto;
    }

    public static WorkOrderChargeListDto toList(WorkOrderCharge charge) {

        WorkOrderChargeListDto dto = new WorkOrderChargeListDto();

        dto.id = charge.getId();
        dto.workOrderId = charge.getWorkOrder().getId();
        dto.chargeType = charge.getChargeType().getLabelEs();
        dto.paymentMethod = charge.getPaymentMethod().getLabelEs();
        dto.description = charge.getDescription();
        dto.price = charge.getPrice();
        dto.payer = charge.getPayer();
        dto.paid = charge.getPaid();
        dto.createdUserId = charge.getCreatedUserId();
        dto.createdUserName = charge.getCreatedUserName();
        dto.createdAt = charge.getCreatedAt();

        return dto;
    }

}
