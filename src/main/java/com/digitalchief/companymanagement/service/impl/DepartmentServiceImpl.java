package com.digitalchief.companymanagement.service.impl;

import com.digitalchief.companymanagement.entity.Company;
import com.digitalchief.companymanagement.entity.Department;
import com.digitalchief.companymanagement.mapper.DepartmentMapper;
import com.digitalchief.companymanagement.message.key.DepartmentMessageKey;
import com.digitalchief.companymanagement.message.source.MessagesSource;
import com.digitalchief.companymanagement.repository.DepartmentRepository;
import com.digitalchief.companymanagement.service.CompanyService;
import com.digitalchief.companymanagement.service.DepartmentService;
import com.digitalchief.companymanagement.service.exception.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.mapstruct.factory.Mappers;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class DepartmentServiceImpl implements DepartmentService {
    private final DepartmentRepository departmentRepository;
    private final CompanyService companyService;
    private final MessagesSource messagesSource;
    private final DepartmentMapper mapper = Mappers.getMapper(DepartmentMapper.class);

    @Override
    public List<Department> findAllByCompanyIdWithPagination(Long companyId, Pageable pageable) {
        return departmentRepository.findAllByCompanyId(companyId, pageable);
    }

    @Override
    public Department findByCompanyAndDepartmentId(Long companyId, Long departmentId) {
        return departmentRepository.findByCompanyIdAndId(companyId, departmentId)
                .orElseThrow(() -> new EntityNotFoundException(
                        messagesSource.getMessage(DepartmentMessageKey.DEPARTMENT_NOT_FOUND_BY_ID, departmentId))
                );
    }

    @Override
    public Department findById(Long departmentId) {
        return departmentRepository.findById(departmentId)
                .orElseThrow(() -> new EntityNotFoundException(
                        messagesSource.getMessage(DepartmentMessageKey.DEPARTMENT_NOT_FOUND_BY_ID, departmentId)
                ));
    }

    @Override
    @Transactional
    public Department createDepartmentInCompany(Department department, Long companyId) {
        Company companyToCreateDepartmentIn = companyService.findById(companyId);

        department.setCompany(companyToCreateDepartmentIn);

        return departmentRepository.save(department);
    }

    @Override
    @Transactional
    public void updateDepartmentInCompanyById(Long companyId, Long departmentId, Department updateDepartment) {
        Department departmentToUpdate = departmentRepository.findByCompanyIdAndId(companyId, departmentId)
                .orElseThrow(() -> new EntityNotFoundException(
                        messagesSource.getMessage(DepartmentMessageKey.DEPARTMENT_NOT_FOUND_BY_ID, departmentId)
                ));
        mapper.copyAllFields(departmentToUpdate, updateDepartment);
        departmentRepository.save(departmentToUpdate);
    }

    @Override
    @Transactional
    public void updateDepartmentInCompanyPartiallyById(
            Long companyId, Long departmentId, Department updateDepartment) {
        Department departmentToUpdate = departmentRepository.findByCompanyIdAndId(companyId, departmentId)
                .orElseThrow(() -> new EntityNotFoundException(
                        messagesSource.getMessage(DepartmentMessageKey.DEPARTMENT_NOT_FOUND_BY_ID, departmentId)
                ));
        mapper.copyNotNullFields(departmentToUpdate, updateDepartment);
        departmentRepository.save(departmentToUpdate);
    }

    @Override
    @Transactional
    public void deleteDepartmentFromCompanyById(Long companyId, Long departmentId) {
        Department departmentToDelete = departmentRepository.findByCompanyIdAndId(companyId, departmentId)
                .orElseThrow(() -> new EntityNotFoundException(
                        messagesSource.getMessage(DepartmentMessageKey.DEPARTMENT_NOT_FOUND_BY_ID,
                                departmentId)
                ));
        departmentRepository.delete(departmentToDelete);
    }
}
