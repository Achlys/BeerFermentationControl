package org.xer.beerfermcontrol.core.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
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

    RowMapper<Reading> rowMapper = new RowMapper<Reading>() {
        @Override
        public Reading mapRow(ResultSet rs, int rowNum) throws SQLException {
            Reading reading = new Reading();
            reading.setMoment(rs.getTimestamp("MOMENT"));
            reading.setHydromName(rs.getString("HYDROM_NAME"));
            reading.setGravity(rs.getDouble("GRAVITY"));
            reading.setTemp(rs.getDouble("TEMP"));
            reading.setJson(rs.getString("JSON"));
            return reading;
        }
    };

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

    @Override
    public List<Reading> getReadingList(String hydromName) {
        String sql = "SELECT * FROM READING WHERE HYDROM_NAME = :name ORDER BY MOMENT DESC";
        return jdbc.query(sql, Collections.singletonMap("name", hydromName), rowMapper);
    }

}
