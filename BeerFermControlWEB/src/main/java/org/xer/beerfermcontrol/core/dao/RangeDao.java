package org.xer.beerfermcontrol.core.dao;

import java.util.List;

import org.xer.beerfermcontrol.core.bean.Range;

/**
 *
 * @author Achlys
 */
public interface RangeDao {

	List<Range> getRangesByConfig(Integer configId);

	void addRange(Range range);

	void removeRange(Integer id, Integer configId);

	Range getRange(Integer id, Integer configId);

	void updateRange(Range range);

	Boolean overlapsOtherRange(Range range, boolean exceptItself);

	Range getApplicableRange(Integer configId, Double gravity);

}
