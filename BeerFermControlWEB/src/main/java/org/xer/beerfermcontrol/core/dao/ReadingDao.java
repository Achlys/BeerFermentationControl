package org.xer.beerfermcontrol.core.dao;

import java.util.Date;
import java.util.List;
import org.xer.beerfermcontrol.core.bean.Reading;

/**
 *
 * @author Achlys
 */
public interface ReadingDao {

    void addReading(Reading reading);

    List<Reading> getReadingList(String hydromName, Date startDate);

}
