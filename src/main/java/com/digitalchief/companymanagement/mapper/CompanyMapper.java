package com.digitalchief.companymanagement.mapper;

import com.digitalchief.companymanagement.entity.Company;
import com.digitalchief.companymanagement.entity.Employee;
import com.digitalchief.companymanagement.model.CompanyModel;
import com.digitalchief.companymanagement.model.EmployeeModel;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Mappings;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.util.List;

@Mapper
public interface CompanyMapper {

    @Mappings({
            @Mapping(target = "id", ignore = true),
            @Mapping(target = "numberOfEmployees", ignore = true),
            @Mapping(target = "departments", ignore = true)
    })
    void copyAllFields(@MappingTarget Company target, Company source);

    @Mappings({
            @Mapping(target = "id", ignore = true),
            @Mapping(target = "numberOfEmployees", ignore = true),
            @Mapping(target = "departments", ignore = true),
    })
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void copyNotNullFields(@MappingTarget Company target, Company source);

    CompanyModel toModel(Company entity);

    List<CompanyModel> toModel(List<Company> entities);

    Company toEntity(CompanyModel model);

    List<Company> toEntity(List<CompanyModel> models);

}
