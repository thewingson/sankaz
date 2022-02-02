package kz.open.sankaz.service.impl;

import kz.open.sankaz.model.JwtBlackList;
import kz.open.sankaz.repo.JwtBlackListRepo;
import kz.open.sankaz.service.JwtBlackListService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Slf4j
@Transactional
public class JwtBlackListServiceImpl extends AbstractService<JwtBlackList, JwtBlackListRepo> implements JwtBlackListService {

    private final JwtBlackListRepo blackListRepo;

    public JwtBlackListServiceImpl(JwtBlackListRepo blackListRepo) {
        super(blackListRepo);
        this.blackListRepo = blackListRepo;
    }

    @Override
    protected Class getCurrentClass() {
        return JwtBlackList.class;
    }

    @Override
    public List<JwtBlackList> getAllByUsername(String username) {
        return blackListRepo.findAllByUsername(username);
    }
}
