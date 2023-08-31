package org.xer.beerfermcontrol.core.dao;

import java.util.List;
import org.xer.beerfermcontrol.core.bean.Ulog;

/**
 *
 * @author Achlys
 */
public interface UlogDao {

    void addEvent(Integer configId, String event);

    List<Ulog> getEventList(Integer configId);

}
