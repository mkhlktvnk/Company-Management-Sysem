package com.digitalchief.companymanagement.mapper;

import com.digitalchief.companymanagement.entity.Employee;
import com.digitalchief.companymanagement.model.EmployeeModel;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Mappings;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.util.List;

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

    EmployeeModel toModel(Employee entity);

    List<EmployeeModel> toModel(List<Employee> entities);

    Employee toEntity(EmployeeModel model);

    List<Employee> toEntity(List<EmployeeModel> models);

}
