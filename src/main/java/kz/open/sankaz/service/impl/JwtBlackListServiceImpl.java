package kz.open.sankaz.service.impl;

import kz.open.sankaz.model.SecUserToken;
import kz.open.sankaz.repo.SecUserTokenRepo;
import kz.open.sankaz.service.JwtBlackListService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@Transactional
public class JwtBlackListServiceImpl extends AbstractService<SecUserToken, SecUserTokenRepo> implements JwtBlackListService {

    private final SecUserTokenRepo blackListRepo;

    public JwtBlackListServiceImpl(SecUserTokenRepo blackListRepo) {
        super(blackListRepo);
        this.blackListRepo = blackListRepo;
    }

    @Override
    protected Class getCurrentClass() {
        return SecUserToken.class;
    }
}
