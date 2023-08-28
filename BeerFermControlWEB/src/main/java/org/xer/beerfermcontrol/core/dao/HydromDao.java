package org.xer.beerfermcontrol.core.dao;

import java.util.List;
import org.xer.beerfermcontrol.core.bean.Hydrom;

/**
 *
 * @author Achlys
 */
public interface HydromDao {
    
    Hydrom getHydromByConfig(Integer configId);

    void addHydrom(Hydrom hydrom);

    void removeHydrom(Integer id, Integer configId);

    Hydrom getHydrom(Integer id, Integer configId);

    void updateHydrom(Hydrom hydrom);

    List<Hydrom> getHydromsByName(String deviceName);

}
