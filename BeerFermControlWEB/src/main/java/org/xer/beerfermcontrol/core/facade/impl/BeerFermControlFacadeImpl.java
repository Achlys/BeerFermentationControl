package org.xer.beerfermcontrol.core.facade.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.xer.beerfermcontrol.core.bean.Config;
import org.xer.beerfermcontrol.core.bean.User;
import org.xer.beerfermcontrol.core.dao.ConfigDao;
import org.xer.beerfermcontrol.core.dao.HydromDao;
import org.xer.beerfermcontrol.core.dao.TplinkDao;
import org.xer.beerfermcontrol.core.dao.UserDao;
import org.xer.beerfermcontrol.core.facade.BeerFermControlFacade;
import org.xer.beerfermcontrol.core.util.CoreConstants;

/**
 *
 * @author Achlys
 */
@Service
public class BeerFermControlFacadeImpl implements BeerFermControlFacade {

    @Autowired
    private ConfigDao configDao;
    @Autowired
    private HydromDao hydromDao;
    @Autowired
    private TplinkDao tplinkDao;
    @Autowired
    private UserDao userDao;

    @Override
    public User getUser(String username, String password) {
        User user = userDao.getUsuario(username);
        if (user == null || !user.getPassword().equals(password)) {
            return null;
        } else {
            return user;
        }
    }

    @Override
    public List<Config> getUsersConfigs(Integer userId) {
        List<Config> configList = configDao.getUsersConfigs(userId);
        if (configList != null) {
            for (Config config : configList) {
                config.setTplinkCold(tplinkDao.getTplinkByConfig(config.getId(), CoreConstants.TPLINK_TYPE_COLD));
                config.setTplinkWarm(tplinkDao.getTplinkByConfig(config.getId(), CoreConstants.TPLINK_TYPE_WARM));
                config.setHydrom(hydromDao.getHydromByConfig(config.getId()));
            }
        }
        return configList;
    }

    @Override
    public void addConfig(Config newConfig) {
        configDao.addConfig(newConfig);
    }

}
