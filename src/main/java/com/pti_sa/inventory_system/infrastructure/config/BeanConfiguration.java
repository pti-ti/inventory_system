package com.pti_sa.inventory_system.infrastructure.config;

import com.pti_sa.inventory_system.application.*;
import com.pti_sa.inventory_system.domain.port.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BeanConfiguration {

    @Bean
    public UserService userService(IUserRepository iUserRepository){
        return new UserService(iUserRepository);
    }

    @Bean
    public DeviceService deviceService(IDeviceRepository iDeviceRepository){
        return new DeviceService(iDeviceRepository);
    }

    @Bean
    public LocationService locationService(ILocationRepository iLocationRepository){
        return new LocationService(iLocationRepository);
    }

    @Bean
    public LogbookService logbookService(ILogbookRepository iLogbookRepository){
        return new LogbookService(iLogbookRepository);
    }

    @Bean
    public MaintenanceService maintenanceService(IMaintenanceRepository iMaintenanceRepository){
        return new MaintenanceService(iMaintenanceRepository);
    }

    @Bean
    public StatusService statusService(IStatusRepository iStatusRepository){
        return new StatusService(iStatusRepository);
    }
}
