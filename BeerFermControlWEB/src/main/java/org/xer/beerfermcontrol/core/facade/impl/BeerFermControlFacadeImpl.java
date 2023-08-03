package org.xer.beerfermcontrol.core.facade.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.xer.beerfermcontrol.core.bean.Config;
import org.xer.beerfermcontrol.core.bean.User;
import org.xer.beerfermcontrol.core.dao.ConfigDao;
import org.xer.beerfermcontrol.core.dao.UserDao;
import org.xer.beerfermcontrol.core.facade.BeerFermControlFacade;

/**
 *
 * @author Achlys
 */
@Service
public class BeerFermControlFacadeImpl implements BeerFermControlFacade {

    @Autowired
    private ConfigDao configDao;
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
        return configDao.getUsersConfigs(userId);
    }

}
