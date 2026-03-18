package ruben.springboot.service_management.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "app")
public class AppStartupProperties {

    private final InitialAdmin initialAdmin = new InitialAdmin();
    private final DemoData demoData = new DemoData();

    public InitialAdmin getInitialAdmin() {
        return initialAdmin;
    }

    public DemoData getDemoData() {
        return demoData;
    }

    public static class InitialAdmin {
        private boolean enabled = true;
        private String name = "Administrador inicial";
        private String phone = "600000000";
        private String username = "admin";
        private String password = "admin123456";

        public boolean isEnabled() {
            return enabled;
        }

        public void setEnabled(boolean enabled) {
            this.enabled = enabled;
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

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }
    }

    public static class DemoData {
        private boolean enabled = false;

        public boolean isEnabled() {
            return enabled;
        }

        public void setEnabled(boolean enabled) {
            this.enabled = enabled;
        }
    }
}