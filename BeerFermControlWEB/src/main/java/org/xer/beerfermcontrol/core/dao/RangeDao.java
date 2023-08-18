package org.xer.beerfermcontrol.core.dao;

import java.util.List;
import org.xer.beerfermcontrol.core.bean.Range;

/**
 *
 * @author Achlys
 */
public interface RangeDao {
    
    List<Range> getRangesByConfig(Integer configId);
    
}
