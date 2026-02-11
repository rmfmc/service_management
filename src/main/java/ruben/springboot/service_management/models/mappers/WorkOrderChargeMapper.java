package ruben.springboot.service_management.models.mappers;

import ruben.springboot.service_management.models.WorkOrderCharge;
import ruben.springboot.service_management.models.dtos.requests.WorkOrderChargeRequestDto;

public class WorkOrderChargeMapper {

    public WorkOrderCharge toEntity(WorkOrderChargeRequestDto dto, Long createdUserId) {
        
        WorkOrderCharge woc = new WorkOrderCharge();
        woc.setCreatedUserId(createdUserId);
        woc.setChargeType(dto.chargeType);
        woc.setPaymentMethod(dto.paymentMethod);
        woc.setDescription(dto.description);
        woc.setPrice(dto.price);
        woc.setPayer(dto.payer);
        woc.setPaid(dto.paid != null ? dto.paid : true);

        return woc;
    }

}
