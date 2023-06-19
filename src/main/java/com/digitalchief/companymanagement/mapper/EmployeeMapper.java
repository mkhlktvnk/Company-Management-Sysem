package com.digitalchief.companymanagement.mapper;

import com.digitalchief.companymanagement.entity.Employee;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Mappings;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper
public interface EmployeeMapper {

    @Mappings({
            @Mapping(target = "id", ignore = true),
            @Mapping(target = "department", ignore = true)
    })
    void copyAllFields(@MappingTarget Employee target, Employee source);

    @Mappings({
            @Mapping(target = "id", ignore = true),
            @Mapping(target = "department", ignore = true),
    })
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void copyNotNullFields(@MappingTarget Employee target, Employee source);

}
