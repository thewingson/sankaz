package kz.open.sankaz.service.impl;

import kz.open.sankaz.exception.EntityNotFoundException;
import kz.open.sankaz.model.Organization;
import kz.open.sankaz.model.SecUser;
import kz.open.sankaz.repo.OrganizationRepo;
import kz.open.sankaz.service.OrganizationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
@Slf4j
@Transactional
public class OrganizationServiceImpl extends AbstractService<Organization, OrganizationRepo> implements OrganizationService {

    private final OrganizationRepo organizationRepo;

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
    protected Class getCurrentClass() {
        return Organization.class;
    }

}
