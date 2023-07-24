package org.xer.beerfermcontrol.core.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import org.xer.beerfermcontrol.core.bean.User;
import org.xer.beerfermcontrol.core.dao.UserDao;

/**
 *
 * @author Achlys
 */
@Repository
public class UserDaoImpl implements UserDao {
    
    @Autowired
    private NamedParameterJdbcTemplate jdbc;

    @Override
    public User getUsuario(String userName) {
        String sql = "SELECT * FROM USER WHERE USERNAME = :username";
        return jdbc.queryForObject(sql, Collections.singletonMap("username", userName), new RowMapper<User>(){
            @Override
            public User mapRow(ResultSet rs, int rowNum) throws SQLException {
                User user = new User();
                user.setId(rs.getInt("ID"));
                user.setUsername(rs.getString("USERNAME").trim());
                user.setPassword(rs.getString("PASSWORD").trim());
                user.setStartDate(rs.getDate("START_DATE"));
                user.setEndDate(rs.getDate("END_DATE"));
                return user;
            }
        });
    }
    
}
