package ruben.springboot.service_management.models.dto;

public class ClientListDto {

    public Long id;
    public String name;
    public String phone;
    public String phone2;
    public String address;
    public String city;
    
    public ClientListDto(Long id, String name, String phone, String phone2, String address, String city) {
        this.id = id;
        this.name = name;
        this.phone = phone;
        this.phone2 = phone2;
        this.address = address;
        this.city = city;
    }

    public ClientListDto() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPhone2() {
        return phone2;
    }

    public void setPhone2(String phone2) {
        this.phone2 = phone2;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }
    
    
}
