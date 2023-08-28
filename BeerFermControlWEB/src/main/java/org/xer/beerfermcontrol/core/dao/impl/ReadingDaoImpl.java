package org.xer.beerfermcontrol.core.dao.impl;

import java.util.HashMap;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import org.xer.beerfermcontrol.core.bean.Reading;
import org.xer.beerfermcontrol.core.dao.ReadingDao;

/**
 *
 * @author Achlys
 */
@Repository
public class ReadingDaoImpl implements ReadingDao {

    @Autowired
    private NamedParameterJdbcTemplate jdbc;
    
    @Override
    public void addReading(Reading reading) {
        String sql = "INSERT INTO READING (MOMENT, HYDROM_NAME, GRAVITY, TEMP, JSON) VALUES (:moment, :hydrom, :gravity, :temp, :json)";
        Map parameters = new HashMap();
        parameters.put("moment", reading.getMoment());
        parameters.put("hydrom", reading.getHydromName());
        parameters.put("gravity", reading.getGravity());
        parameters.put("temp", reading.getTemp());
        parameters.put("json", reading.getJson());
        jdbc.update(sql, parameters);
    }

}