package org.xer.beerfermcontrol.core.dao;

import java.util.List;
import org.xer.beerfermcontrol.core.bean.Config;

/**
 *
 * @author Achlys
 */
public interface ConfigDao {
    
    List<Config> getUsersConfigs(Integer userId);

    void addConfig(Config newConfig);
    
}
