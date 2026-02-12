package ruben.springboot.service_management.models.mappers;

import ruben.springboot.service_management.authentication.SecurityUtils;
import ruben.springboot.service_management.models.WorkOrderCharge;
import ruben.springboot.service_management.models.dtos.requests.WorkOrderChargeRequestDto;

public class WorkOrderChargeMapper {

    public static WorkOrderCharge toEntity(WorkOrderChargeRequestDto dto) {

        Long currentUserId = SecurityUtils.currentUserId();

        WorkOrderCharge woc = new WorkOrderCharge();
        woc.setCreatedUserId(currentUserId);
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

}
