package org.xer.beerfermcontrol.core.facade;

import java.util.List;
import org.xer.beerfermcontrol.core.bean.Config;
import org.xer.beerfermcontrol.core.bean.Hydrom;
import org.xer.beerfermcontrol.core.bean.Range;
import org.xer.beerfermcontrol.core.bean.Tplink;
import org.xer.beerfermcontrol.core.bean.User;

/**
 *
 * @author Achlys
 */
public interface BeerFermControlFacade {
    
    User getUser(String username, String password);
    
    List<Config> getUsersConfigs(Integer userId);

    void addConfig(Config newConfig);

    void removeConfig(Integer id, Integer userId);

    Config getConfig(Integer id, Integer userId);

    void updateConfig(Config config);

    Config getFullConfig(Integer id, Integer userId);

    void addHydrom(Hydrom newHydrom, Integer userId);

    void removeHydrom(Integer id, Integer configId, Integer userId);

    Hydrom getHydrom(Integer id, Integer configId, Integer userId);

    void updateHydrom(Hydrom hydrom, Integer userId);

    void addTplink(Tplink tplink, Integer userId);

    void removeTplink(Integer id, Integer configId, Integer userId);

    Tplink getTplink(Integer id, Integer configId, Integer userId);

    void updateTplink(Tplink tplink, Integer userId);

    void addRange(Range range, Integer userId);

    void removeRange(Integer id, Integer configId, Integer userId);

    Range getRange(Integer id, Integer configId, Integer userId);

    void updateRange(Range range, Integer userId);

    boolean overlapsOtherRange(Range range, boolean exceptItself);

    String encender(Integer id, Integer configId);

    String apagar(Integer id, Integer configId);
    
}
