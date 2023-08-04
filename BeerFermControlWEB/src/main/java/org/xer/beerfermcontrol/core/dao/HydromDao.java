package org.xer.beerfermcontrol.core.dao;

import org.xer.beerfermcontrol.core.bean.Hydrom;

/**
 *
 * @author Achlys
 */
public interface HydromDao {
    
    Hydrom getHydromByConfig(Integer configId);
    
}
