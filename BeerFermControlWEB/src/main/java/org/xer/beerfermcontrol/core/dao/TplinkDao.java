package org.xer.beerfermcontrol.core.dao;

import org.xer.beerfermcontrol.core.bean.Tplink;

/**
 *
 * @author Achlys
 */
public interface TplinkDao {
    
    Tplink getTplinkByConfig(Integer configId, String type);
    
}
