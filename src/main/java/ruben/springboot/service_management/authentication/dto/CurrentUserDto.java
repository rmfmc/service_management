package ruben.springboot.service_management.authentication.dto;

public class CurrentUserDto {

    private Long currentUserId;
    private String currentUserName;
    
    public CurrentUserDto(Long currentUserId, String currentUserName) {
        this.currentUserId = currentUserId;
        this.currentUserName = currentUserName;
    }

    public CurrentUserDto() {
    }

    public Long getCurrentUserId() {
        return currentUserId;
    }

    public void setCurrentUserId(Long currentUserId) {
        this.currentUserId = currentUserId;
    }

    public String getCurrentUserName() {
        return currentUserName;
    }

    public void setCurrentUserName(String currentUserName) {
        this.currentUserName = currentUserName;
    }

}
