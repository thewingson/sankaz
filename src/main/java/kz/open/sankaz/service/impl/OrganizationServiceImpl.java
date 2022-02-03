package kz.open.sankaz.service.impl;

import kz.open.sankaz.dto.OrganizationDto;
import kz.open.sankaz.dto.OrganizationFilterDto;
import kz.open.sankaz.exception.EntityNotFoundException;
import kz.open.sankaz.mapper.OrganizationMapper;
import kz.open.sankaz.model.Organization;
import kz.open.sankaz.model.SecUser;
import kz.open.sankaz.repo.OrganizationRepo;
import kz.open.sankaz.service.AuthService;
import kz.open.sankaz.service.OrganizationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@Slf4j
@Transactional
public class OrganizationServiceImpl extends AbstractService<Organization, OrganizationRepo> implements OrganizationService {

    private final OrganizationRepo organizationRepo;

    @Autowired
    private OrganizationMapper organizationMapper;

    @Lazy
    @Autowired
    private AuthService authService;

    public OrganizationServiceImpl(OrganizationRepo organizationRepo) {
        super(organizationRepo);
        this.organizationRepo = organizationRepo;
    }

    @Override
    public Organization getOrganizationByTelNumber(String telNumber) {
        Optional<Organization> organization = repo.findByTelNumberAndDeletedByIsNull(telNumber);
        if(!organization.isPresent()){
            Map<String, Object> params = new HashMap<>();
            params.put("telNumber", telNumber);
            throw new EntityNotFoundException(getCurrentClass(), params);
        }
        return organization.get();
    }

    @Override
    public Organization getOrganizationByUser(SecUser user) {
        Optional<Organization> organization = organizationRepo.findByUserAndDeletedByIsNull(user);
        if(!organization.isPresent()){
            Map<String, Object> params = new HashMap<>();
            params.put("user", user.getUsername());
            throw new EntityNotFoundException(getCurrentClass(), params);
        }
        return organization.get();
    }

    @Override
    public Organization getOrganizationByIban(String iban) {
        Optional<Organization> organization = repo.findByIbanAndDeletedByIsNull(iban);
        if(!organization.isPresent()){
            Map<String, Object> params = new HashMap<>();
            params.put("iban", iban);
            throw new EntityNotFoundException(getCurrentClass(), params);
        }
        return organization.get();
    }

    @Override
    public Organization getOrganizationByIin(String iin) {
        Optional<Organization> organization = repo.findByIinAndDeletedByIsNull(iin);
        if(!organization.isPresent()){
            Map<String, Object> params = new HashMap<>();
            params.put("iin", iin);
            throw new EntityNotFoundException(getCurrentClass(), params);
        }
        return organization.get();
    }

    @Override
    public List<OrganizationDto> getAllByConfirmationStatuses(OrganizationFilterDto organizationFilter) {
        return organizationMapper.organizationToDto(organizationRepo.findAllByConfirmationStatusInAndDeletedByIsNull(organizationFilter.getConfirmationStatuses()));
    }

    @Override
    public void approveOrganizationData(Long orgId) {
        log.info("OrganizationServiceImpl. Starting approve organization id: {}", orgId);
        Organization organization = getOne(orgId);
        if(organization.getConfirmationStatus().equals("CONFIRMED")){
            log.warn("OrganizationServiceImpl. Organization {} already has been confirmed", orgId);
            throw new RuntimeException("Данная организация уже одобрена!");
        }
        organization.setConfirmationStatus("CONFIRMED");
        organization.setConfirmedTs(LocalDateTime.now());
        organization.setConfirmedBy(authService.getCurrentUsername());
        log.info("OrganizationServiceImpl. Organization {} confirmed successfully", orgId);
    }

    @Override
    public void rejectOrganizationData(Long orgId) {
        log.info("OrganizationServiceImpl. Starting reject organization id: {}", orgId);
        Organization organization = getOne(orgId);
        if(organization.getConfirmationStatus().equals("REJECTED")){
            log.warn("OrganizationServiceImpl. Organization {} already has been rejected", orgId);
            throw new RuntimeException("Данная организация уже отклонена!");
        }
        organization.setConfirmationStatus("REJECTED");
        organization.setConfirmedTs(LocalDateTime.now());
        organization.setConfirmedBy(authService.getCurrentUsername());
        log.info("OrganizationServiceImpl. Organization {} rejected successfully", orgId);
    }

    @Override
    protected Class getCurrentClass() {
        return Organization.class;
    }

    @Override
    public OrganizationDto getOneDto(Long id) {
        return organizationMapper.organizationToDto(getOne(id));
    }

    @Override
    public List<OrganizationDto> getAllDto() {
        return organizationMapper.organizationToDto(getAll());
    }

    @Override
    public List<OrganizationDto> getAllDto(Map<String, Object> params) {
        return organizationMapper.organizationToDto(getAll(params));
    }

    @Override
    public Organization addOneDto(OrganizationDto dto) {
        return null;
    }

    @Override
    public Organization updateOneDto(Long id, OrganizationDto dto) {
        return null;
    }

    @Override
    public Organization updateOneDto(Map<String, Object> params, OrganizationDto dto) {
        return null;
    }
}
