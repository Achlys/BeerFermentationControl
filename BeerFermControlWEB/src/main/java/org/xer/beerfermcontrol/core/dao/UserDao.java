package org.xer.beerfermcontrol.core.dao;

import org.xer.beerfermcontrol.core.bean.User;

/**
 *
 * @author Achlys
 */
public interface UserDao {
    
    User getUsuario(String userName);
    
}
