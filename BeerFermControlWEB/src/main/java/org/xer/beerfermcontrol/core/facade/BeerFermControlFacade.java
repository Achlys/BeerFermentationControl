package org.xer.beerfermcontrol.core.facade;

import java.util.List;
import org.xer.beerfermcontrol.core.bean.Config;
import org.xer.beerfermcontrol.core.bean.User;

/**
 *
 * @author Achlys
 */
public interface BeerFermControlFacade {
    
    User getUser(String username, String password);
    
    List<Config> getUsersConfigs(Integer userId);
    
}