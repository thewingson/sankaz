package kz.open.sankaz.service;

import kz.open.sankaz.model.SecRole;

public interface RoleService extends CommonService<SecRole> {

    /***
     * for Entity
     */
    SecRole getByName(String name);

    /***
     * for DTO
     */
}
