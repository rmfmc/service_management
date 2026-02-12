package ruben.springboot.service_management.models.dtos.lists;

public class ClientListDto {

    public Long id;
    public String name;
    public String phone;
    public String phone2;
    
    public ClientListDto(Long id, String name, String phone, String phone2) {
        this.id = id;
        this.name = name;
        this.phone = phone;
        this.phone2 = phone2;
    }

    public ClientListDto() {
    }
    
}
