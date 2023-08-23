package org.xer.beerfermcontrol.core.dao;

import org.xer.beerfermcontrol.core.bean.Tplink;

/**
 *
 * @author Achlys
 */
public interface TplinkDao {
    
    Tplink getTplinkByConfig(Integer configId, String type);

    void addTplink(Tplink tplink);

    void removeTplink(Integer id, Integer configId);

    Tplink getTplink(Integer id, Integer configId);

    void updateTplink(Tplink tplink);
    
}
