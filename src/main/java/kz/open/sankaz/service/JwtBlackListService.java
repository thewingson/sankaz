package kz.open.sankaz.service;

import kz.open.sankaz.model.JwtBlackList;

import java.util.List;

public interface JwtBlackListService extends CommonService<JwtBlackList> {
    /***
     * for Entity
     */
    List<JwtBlackList> getAllByUsername(String username);
}
