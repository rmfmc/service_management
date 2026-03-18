package ruben.springboot.service_management.config;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import ruben.springboot.service_management.models.ApplianceType;
import ruben.springboot.service_management.models.Brand;
import ruben.springboot.service_management.models.CommonFault;
import ruben.springboot.service_management.models.User;
import ruben.springboot.service_management.models.enums.UserRole;
import ruben.springboot.service_management.repositories.ApplianceTypeRepository;
import ruben.springboot.service_management.repositories.BrandRepository;
import ruben.springboot.service_management.repositories.CommonFaultRepository;
import ruben.springboot.service_management.repositories.UserRepository;

@Configuration
@EnableConfigurationProperties(AppStartupProperties.class)
public class InitialDataConfig {

    private static final Logger log = LoggerFactory.getLogger(InitialDataConfig.class);

    @Bean
    ApplicationRunner initialDataRunner(
            UserRepository userRepository,
            BrandRepository brandRepository,
            ApplianceTypeRepository applianceTypeRepository,
            CommonFaultRepository commonFaultRepository,
            PasswordEncoder passwordEncoder,
            AppStartupProperties properties) {

        return args -> runInitialization(
                userRepository,
                brandRepository,
                applianceTypeRepository,
                commonFaultRepository,
                passwordEncoder,
                properties);
    }

    @Transactional
    void runInitialization(
            UserRepository userRepository,
            BrandRepository brandRepository,
            ApplianceTypeRepository applianceTypeRepository,
            CommonFaultRepository commonFaultRepository,
            PasswordEncoder passwordEncoder,
            AppStartupProperties properties) {

        createInitialAdminIfNeeded(userRepository, passwordEncoder, properties.getInitialAdmin());
        createDemoCatalogsIfEnabled(brandRepository, applianceTypeRepository, commonFaultRepository, properties.getDemoData());
    }

    private void createInitialAdminIfNeeded(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder,
            AppStartupProperties.InitialAdmin initialAdmin) {

        if (!initialAdmin.isEnabled()) {
            log.info("Creación automática del admin inicial deshabilitada por configuración");
            return;
        }

        if (userRepository.count() > 0) {
            log.info("Admin inicial omitido: ya existen usuarios en la base de datos");
            return;
        }

        User admin = new User();
        admin.setName(initialAdmin.getName().trim());
        admin.setPhone(initialAdmin.getPhone().trim());
        admin.setUsername(initialAdmin.getUsername().trim());
        admin.setPasswordHash(passwordEncoder.encode(initialAdmin.getPassword()));
        admin.setRole(UserRole.ADMIN);
        admin.setActive(true);

        userRepository.save(admin);

        log.warn("Usuario administrador inicial creado automáticamente con username='{}'", admin.getUsername());
    }

    private void createDemoCatalogsIfEnabled(
            BrandRepository brandRepository,
            ApplianceTypeRepository applianceTypeRepository,
            CommonFaultRepository commonFaultRepository,
            AppStartupProperties.DemoData demoData) {

        if (!demoData.isEnabled()) {
            return;
        }

        Brand balay = getOrCreateBrand(brandRepository, "Balay");
        Brand bosch = getOrCreateBrand(brandRepository, "Bosch");
        Brand fagor = getOrCreateBrand(brandRepository, "Fagor");

        ApplianceType washingMachine = getOrCreateApplianceType(applianceTypeRepository, "Lavadora");
        ApplianceType fridge = getOrCreateApplianceType(applianceTypeRepository, "Frigorífico");
        ApplianceType dishwasher = getOrCreateApplianceType(applianceTypeRepository, "Lavavajillas");

        createCommonFaultIfMissing(commonFaultRepository, washingMachine, "No centrifuga");
        createCommonFaultIfMissing(commonFaultRepository, washingMachine, "Pierde agua");
        createCommonFaultIfMissing(commonFaultRepository, fridge, "No enfría");
        createCommonFaultIfMissing(commonFaultRepository, fridge, "Hace mucho ruido");
        createCommonFaultIfMissing(commonFaultRepository, dishwasher, "No desagua");
        createCommonFaultIfMissing(commonFaultRepository, dishwasher, "No calienta");

        log.info(
                "Datos demo verificados/cargados: marcas={}, tipos={}",
                List.of(balay.getName(), bosch.getName(), fagor.getName()),
                List.of(washingMachine.getName(), fridge.getName(), dishwasher.getName()));
    }

    private Brand getOrCreateBrand(BrandRepository brandRepository, String name) {
        return brandRepository.findByNameIgnoreCase(name)
                .orElseGet(() -> brandRepository.save(new Brand(null, name.trim())));
    }

    private ApplianceType getOrCreateApplianceType(ApplianceTypeRepository applianceTypeRepository, String name) {
        return applianceTypeRepository.findByNameIgnoreCase(name)
                .orElseGet(() -> applianceTypeRepository.save(new ApplianceType(null, name.trim())));
    }

    private void createCommonFaultIfMissing(
            CommonFaultRepository commonFaultRepository,
            ApplianceType applianceType,
            String faultName) {

        boolean exists = commonFaultRepository.findByApplianceTypeId(applianceType.getId()).stream()
                .anyMatch(fault -> fault.getName().equalsIgnoreCase(faultName));

        if (exists) {
            return;
        }

        commonFaultRepository.save(new CommonFault(null, applianceType, faultName.trim()));
    }
}