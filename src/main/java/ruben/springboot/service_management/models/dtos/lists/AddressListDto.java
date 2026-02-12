package ruben.springboot.service_management.models.dtos.lists;

public class AddressListDto {

    public Long id;
    public String address;
    public String city;
    public String province;
    public String postal_code;
    
    public AddressListDto(Long id, String address, String city, String province, String postal_code) {
        this.id = id;
        this.address = address;
        this.city = city;
        this.province = province;
        this.postal_code = postal_code;
    }

    public AddressListDto() {
    }
    
}
