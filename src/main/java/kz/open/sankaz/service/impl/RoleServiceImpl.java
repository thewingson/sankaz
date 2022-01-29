package kz.open.sankaz.service.impl;

import kz.open.sankaz.exception.EntityNotFoundException;
import kz.open.sankaz.model.Room;
import kz.open.sankaz.model.SecRole;
import kz.open.sankaz.repo.RoleRepo;
import kz.open.sankaz.service.RoleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
@Slf4j
@Transactional
public class RoleServiceImpl extends AbstractService<SecRole, RoleRepo> implements RoleService {

    private final RoleRepo roleRepo;


    public RoleServiceImpl(RoleRepo roleRepo) {
        super(roleRepo);
        this.roleRepo = roleRepo;
    }

    @Override
    protected Class getCurrentClass() {
        return Room.class;
    }

    @Override
    public SecRole getByName(String name) {
        Optional<SecRole> entity = repo.findByName(name);
        if(!entity.isPresent()){
            Map<String, Object> params = new HashMap<>();
            params.put("NAME", name);
            throw new EntityNotFoundException(getCurrentClass(), params);
        }
        return entity.get();
    }
}
