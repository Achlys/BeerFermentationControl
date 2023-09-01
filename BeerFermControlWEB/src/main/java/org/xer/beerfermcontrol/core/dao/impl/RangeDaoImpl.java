package org.xer.beerfermcontrol.core.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import org.xer.beerfermcontrol.core.bean.Range;
import org.xer.beerfermcontrol.core.dao.RangeDao;

/**
 *
 * @author Achlys
 */
@Repository
public class RangeDaoImpl implements RangeDao {

    @Autowired
    private NamedParameterJdbcTemplate jdbc;

    RowMapper<Range> rowMapper = new RowMapper<Range>() {
        @Override
        public Range mapRow(ResultSet rs, int rowNum) throws SQLException {
            Range range = new Range();
            range.setId(rs.getInt("ID"));
            range.setConfigId(rs.getInt("CONFIG_ID"));
            range.setTopGravity(rs.getDouble("TOP_GRAVITY"));
            range.setBottomGravity(rs.getDouble("BOTTOM_GRAVITY"));
            range.setAimedTemp(rs.getDouble("AIMED_TEMP"));
            return range;
        }
    };

    @Override
    public List<Range> getRangesByConfig(Integer configId) {
        String sql = "SELECT * FROM TEMPRANGE WHERE CONFIG_ID = :configid ORDER BY TOP_GRAVITY DESC";
        return jdbc.query(sql, Collections.singletonMap("configid", configId), rowMapper);
    }

    @Override
    public void addRange(Range range) {
        String sql = "SELECT MAX(ID) FROM TEMPRANGE";
        Integer id = jdbc.query(sql, new ResultSetExtractor<Integer>() {
            @Override
            public Integer extractData(ResultSet rs) throws SQLException, DataAccessException {
                if (rs.next()) {
                    return rs.getInt(1) + 1;
                } else {
                    return 1;
                }
            }
        });
        sql = "INSERT INTO TEMPRANGE (ID, CONFIG_ID, TOP_GRAVITY, BOTTOM_GRAVITY, AIMED_TEMP) VALUES (:id, :configId, :topGravity, :bottomGravity, :aimedTemp)";
        Map parameters = new HashMap();
        parameters.put("id", id);
        parameters.put("configId", range.getConfigId());
        parameters.put("topGravity", range.getTopGravity());
        parameters.put("bottomGravity", range.getBottomGravity());
        parameters.put("aimedTemp", range.getAimedTemp());
        jdbc.update(sql, parameters);
    }

    @Override
    public void removeRange(Integer id, Integer configId) {
        String sql = "DELETE FROM TEMPRANGE WHERE ID = :id AND CONFIG_ID = :configId";
        Map parameters = new HashMap();
        parameters.put("id", id);
        parameters.put("configId", configId);
        if (jdbc.update(sql, parameters) == 0) {
            throw new RuntimeException("Ha intentado eliminar un Range ID que no coincidia con el Config ID!!!");
        }
    }

    @Override
    public Range getRange(Integer id, Integer configId) {
        String sql = "SELECT * FROM TEMPRANGE WHERE ID = :id AND CONFIG_ID = :configId";
        Map parameters = new HashMap();
        parameters.put("id", id);
        parameters.put("configId", configId);
        try {
            return jdbc.queryForObject(sql, parameters, rowMapper);
        } catch (EmptyResultDataAccessException e) {
            // The Range doesn't exist yet
            return null;
        }
    }

    @Override
    public void updateRange(Range range) {
        String sql = "UPDATE TEMPRANGE SET TOP_GRAVITY = :topGravity, BOTTOM_GRAVITY = :bottomGravity, AIMED_TEMP = :aimedTemp WHERE ID = :id AND CONFIG_ID = :configId";
        Map parameters = new HashMap();
        parameters.put("id", range.getId());
        parameters.put("configId", range.getConfigId());
        parameters.put("topGravity", range.getTopGravity());
        parameters.put("bottomGravity", range.getBottomGravity());
        parameters.put("aimedTemp", range.getAimedTemp());
        if (jdbc.update(sql, parameters) == 0) {
            throw new RuntimeException("Ha intentado actualizar una Range con ID que no coincidia con el Config ID!!!");
        }
    }

    @Override
    public boolean overlapsOtherRange(Range range, boolean exceptItself) {
        String sql = "SELECT * FROM TEMPRANGE WHERE CONFIG_ID = :configId AND ((TOP_GRAVITY >= :topGravity AND BOTTOM_GRAVITY < :topGravity) OR (TOP_GRAVITY > :bottomGravity AND BOTTOM_GRAVITY <= :bottomGravity))";
        Map parameters = new HashMap();
        parameters.put("configId", range.getConfigId());
        parameters.put("topGravity", range.getTopGravity());
        parameters.put("bottomGravity", range.getBottomGravity());
        if(exceptItself){
            sql += " AND ID <> :id";
            parameters.put("id", range.getId());
        }
        return jdbc.query(sql, parameters, new ResultSetExtractor<Boolean>() {
            @Override
            public Boolean extractData(ResultSet rs) throws SQLException, DataAccessException {
                if (rs.next()) {
                    return Boolean.TRUE;
                } else {
                    return Boolean.FALSE;
                }
            }
        });
    }

    @Override
    public Range getApplicableRange(Integer configId, Double gravity) {
        String sql = "SELECT * FROM TEMPRANGE WHERE CONFIG_ID = :configId AND TOP_GRAVITY >= :gravity AND BOTTOM_GRAVITY < :gravity";
        Map parameters = new HashMap();
        parameters.put("configId", configId);
        parameters.put("gravity", gravity);
        try {
            return jdbc.queryForObject(sql, parameters, rowMapper);
        } catch (EmptyResultDataAccessException e) {
            // The Range doesn't exist yet
            return null;
        }
    }

}
