package com.pti_sa.inventory_system.infrastructure.config;

import com.pti_sa.inventory_system.application.*;
import com.pti_sa.inventory_system.domain.port.*;
import com.pti_sa.inventory_system.infrastructure.mapper.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BeanConfiguration {

    @Bean
    public UserService userService(IUserRepository iUserRepository,
                                   ILocationRepository iLocationRepository,
                                   UserMapper userMapper,
                                   LocationMapper locationMapper) {
        return new UserService(iUserRepository, iLocationRepository, userMapper, locationMapper);
    }

    @Bean
    public DeviceService deviceService(IDeviceRepository iDeviceRepository, DeviceMapper deviceMapper){
        return new DeviceService(iDeviceRepository, deviceMapper);
    }

    @Bean
    public LocationService locationService(ILocationRepository iLocationRepository, LocationMapper locationMapper){
        return new LocationService(iLocationRepository, locationMapper);
    }

    @Bean
    public LogbookService logbookService(ILogbookRepository iLogbookRepository, LogbookMapper logbookMapper){
        return new LogbookService(iLogbookRepository, logbookMapper);
    }

    @Bean
    public MaintenanceService maintenanceService(IMaintenanceRepository iMaintenanceRepository, MaintenanceMapper maintenanceMapper){
        return new MaintenanceService(iMaintenanceRepository, maintenanceMapper);
    }

    @Bean
    public StatusService statusService(IStatusRepository iStatusRepository, StatusMapper statusMapper){
        return new StatusService(iStatusRepository, statusMapper);
    }
}
