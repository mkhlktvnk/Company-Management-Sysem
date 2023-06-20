package com.digitalchief.companymanagement.mapper;

import com.digitalchief.companymanagement.entity.Department;
import com.digitalchief.companymanagement.entity.Employee;
import com.digitalchief.companymanagement.model.DepartmentModel;
import com.digitalchief.companymanagement.model.EmployeeModel;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Mappings;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.util.List;

@Mapper
public interface DepartmentMapper {

    @Mappings({
            @Mapping(target = "id", ignore = true),
            @Mapping(target = "company", ignore = true),
            @Mapping(target = "employees", ignore = true),
    })
    void copyAllFields(@MappingTarget Department target, Department source);

    @Mappings({
            @Mapping(target = "id", ignore = true),
            @Mapping(target = "company", ignore = true),
            @Mapping(target = "employees", ignore = true),
    })
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void copyNotNullFields(@MappingTarget Department target, Department source);

    DepartmentModel toModel(Department entity);

    List<DepartmentModel> toModel(List<Department> entities);

    Department toEntity(DepartmentModel model);

    List<Department> toEntity(List<DepartmentModel> models);
}
