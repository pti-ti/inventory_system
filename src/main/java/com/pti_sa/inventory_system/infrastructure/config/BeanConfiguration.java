package com.pti_sa.inventory_system.infrastructure.config;

import com.pti_sa.inventory_system.application.*;
import com.pti_sa.inventory_system.domain.port.*;
import com.pti_sa.inventory_system.infrastructure.mapper.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class BeanConfiguration {

    @Bean
    public UserService userService(IUserRepository iUserRepository,
                                   ILocationRepository iLocationRepository,
                                   UserMapper userMapper) {
        return new UserService(iUserRepository, iLocationRepository, userMapper);
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
    public LogbookService logbookService(ILogbookRepository iLogbookRepository, LogbookMapper logbookMapper, IDeviceRepository iDeviceRepository){
        return new LogbookService(iLogbookRepository, iDeviceRepository, logbookMapper);
    }

    @Bean
    public MaintenanceService maintenanceService(IMaintenanceRepository iMaintenanceRepository, IUserRepository iUserRepository,IDeviceRepository iDeviceRepository, IItemRepository iItemRepository,  MaintenanceMapper maintenanceMapper){
        return new MaintenanceService(iMaintenanceRepository, maintenanceMapper,iUserRepository,iDeviceRepository, iItemRepository);
    }

    @Bean
    public StatusService statusService(IStatusRepository iStatusRepository, StatusMapper statusMapper){
        return new StatusService(iStatusRepository, statusMapper);
    }

    @Bean
    public ItemService itemService(IItemRepository iItemRepository, ItemMapper itemMapper) {
        return new ItemService(iItemRepository, itemMapper);
    }
}
