package org.xer.beerfermcontrol.core.facade.impl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.xer.beerfermcontrol.core.bean.Config;
import org.xer.beerfermcontrol.core.bean.Hydrom;
import org.xer.beerfermcontrol.core.bean.Range;
import org.xer.beerfermcontrol.core.bean.Reading;
import org.xer.beerfermcontrol.core.bean.Tplink;
import org.xer.beerfermcontrol.core.bean.User;
import org.xer.beerfermcontrol.core.dao.ConfigDao;
import org.xer.beerfermcontrol.core.dao.HydromDao;
import org.xer.beerfermcontrol.core.dao.RangeDao;
import org.xer.beerfermcontrol.core.dao.ReadingDao;
import org.xer.beerfermcontrol.core.dao.TplinkDao;
import org.xer.beerfermcontrol.core.dao.UserDao;
import org.xer.beerfermcontrol.core.facade.BeerFermControlFacade;
import org.xer.beerfermcontrol.core.util.CoreConstants;

/**
 *
 * @author Achlys
 */
@Service
public class BeerFermControlFacadeImpl implements BeerFermControlFacade {

    @Autowired
    private ConfigDao configDao;
    @Autowired
    private HydromDao hydromDao;
    @Autowired
    private RangeDao rangeDao;
    @Autowired
    private ReadingDao readingDao;
    @Autowired
    private TplinkDao tplinkDao;
    @Autowired
    private UserDao userDao;

    @Override
    public User getUser(String username, String password) {
        User user = userDao.getUsuario(username);
        if (user == null || !user.getPassword().equals(password)) {
            return null;
        } else {
            return user;
        }
    }

    @Override
    public List<Config> getUsersConfigs(Integer userId) {
        List<Config> configList = configDao.getUsersConfigs(userId);
        if (configList != null) {
            for (Config config : configList) {
                config.setTplinkCold(tplinkDao.getTplinkByConfig(config.getId(), CoreConstants.TPLINK_TYPE_COLD));
                config.setTplinkWarm(tplinkDao.getTplinkByConfig(config.getId(), CoreConstants.TPLINK_TYPE_WARM));
                config.setHydrom(hydromDao.getHydromByConfig(config.getId()));
            }
        }
        return configList;
    }

    @Override
    @Transactional
    public void addConfig(Config newConfig) {
        configDao.addConfig(newConfig);
    }

    @Override
    @Transactional
    public void removeConfig(Integer id, Integer userId) {
        Config config = this.getFullConfig(id, userId);
        configDao.removeConfig(id, userId);
        if (config.getHydrom() != null) {
            this.removeHydrom(config.getHydrom().getId(), id, userId);
        }
        if (config.getTplinkCold() != null) {
            this.removeTplink(config.getTplinkCold().getId(), id, userId);
        }
        if (config.getTplinkWarm() != null) {
            this.removeTplink(config.getTplinkWarm().getId(), id, userId);
        }
        if (config.getRanges() != null) {
            for (Range range : config.getRanges()) {
                this.removeRange(range.getId(), id, userId);
            }
        }
    }

    @Override
    public Config getConfig(Integer id, Integer userId) {
        return configDao.getConfig(id, userId);
    }

    @Override
    @Transactional
    public void updateConfig(Config config) {
        configDao.updateConfig(config);
    }

    @Override
    public Config getFullConfig(Integer id, Integer userId) {
        Config config = configDao.getConfig(id, userId);
        config.setTplinkCold(tplinkDao.getTplinkByConfig(config.getId(), CoreConstants.TPLINK_TYPE_COLD));
        config.setTplinkWarm(tplinkDao.getTplinkByConfig(config.getId(), CoreConstants.TPLINK_TYPE_WARM));
        config.setHydrom(hydromDao.getHydromByConfig(config.getId()));
        config.setRanges(rangeDao.getRangesByConfig(config.getId()));
        return config;
    }

    private void checkConfigForUser(Integer configId, Integer userId) {
        Config config = configDao.getConfig(configId, userId);
        if (config == null) {
            throw new RuntimeException("Ha intentado modificar un config que no es suyo!!!");
        }
    }

    @Override
    @Transactional
    public void addHydrom(Hydrom newHydrom, Integer userId) {
        this.checkConfigForUser(newHydrom.getConfigId(), userId);
        hydromDao.addHydrom(newHydrom);
    }

    @Override
    @Transactional
    public void removeHydrom(Integer id, Integer configId, Integer userId) {
        this.checkConfigForUser(configId, userId);
        hydromDao.removeHydrom(id, configId);
    }

    @Override
    public Hydrom getHydrom(Integer id, Integer configId, Integer userId) {
        this.checkConfigForUser(configId, userId);
        return hydromDao.getHydrom(id, configId);
    }

    @Override
    @Transactional
    public void updateHydrom(Hydrom hydrom, Integer userId) {
        this.checkConfigForUser(hydrom.getConfigId(), userId);
        hydromDao.updateHydrom(hydrom);
    }

    @Override
    @Transactional
    public void addTplink(Tplink tplink, Integer userId) {
        this.checkConfigForUser(tplink.getConfigId(), userId);
        tplinkDao.addTplink(tplink);
    }

    @Override
    @Transactional
    public void removeTplink(Integer id, Integer configId, Integer userId) {
        this.checkConfigForUser(configId, userId);
        tplinkDao.removeTplink(id, configId);
    }

    @Override
    public Tplink getTplink(Integer id, Integer configId, Integer userId) {
        this.checkConfigForUser(configId, userId);
        return tplinkDao.getTplink(id, configId);
    }

    @Override
    @Transactional
    public void updateTplink(Tplink tplink, Integer userId) {
        this.checkConfigForUser(tplink.getConfigId(), userId);
        tplinkDao.updateTplink(tplink);
    }

    @Override
    @Transactional
    public void addRange(Range range, Integer userId) {
        this.checkConfigForUser(range.getConfigId(), userId);
        rangeDao.addRange(range);
    }

    @Override
    @Transactional
    public void removeRange(Integer id, Integer configId, Integer userId) {
        this.checkConfigForUser(configId, userId);
        rangeDao.removeRange(id, configId);
    }

    @Override
    public Range getRange(Integer id, Integer configId, Integer userId) {
        this.checkConfigForUser(configId, userId);
        return rangeDao.getRange(id, configId);
    }

    @Override
    @Transactional
    public void updateRange(Range range, Integer userId) {
        this.checkConfigForUser(range.getConfigId(), userId);
        rangeDao.updateRange(range);
    }

    @Override
    public boolean overlapsOtherRange(Range range, boolean exceptItself) {
        return rangeDao.overlapsOtherRange(range, exceptItself);
    }

    @Override
    public String encender(Integer id, Integer configId) {
        Tplink tplink = tplinkDao.getTplink(id, configId);
        return this.sendMessage2Tplink(tplink.getIp(), CoreConstants.TPLINK_ON_MESSAGE);
    }

    @Override
    public String apagar(Integer id, Integer configId) {
        Tplink tplink = tplinkDao.getTplink(id, configId);
        return this.sendMessage2Tplink(tplink.getIp(), CoreConstants.TPLINK_OFF_MESSAGE);
    }

    private String sendMessage2Tplink(String ip, String message) {
        Socket clientSocket = null;
        PrintWriter out = null;
        BufferedReader in = null;
        try {
            clientSocket = new Socket(ip, CoreConstants.TPLINK_TCP_PORT);
            out = new PrintWriter(clientSocket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            out.println(new String(this.cypher(message.getBytes(StandardCharsets.UTF_8)), StandardCharsets.UTF_8));
            return new String(this.cypher(in.readLine().getBytes(StandardCharsets.UTF_8)), StandardCharsets.UTF_8);
        } catch (IOException e) {
            return e.getMessage();
        } finally {
            if (out != null) {
                out.close();
            }
            try {
                if (in != null) {
                    in.close();
                }
                if (clientSocket != null) {
                    clientSocket.close();
                }
            } catch (IOException e) {

            }
        }
    }

    public byte[] cypher(byte[] bArr) {
        if (bArr != null && bArr.length > 0) {
            int i = -85;
            for (int i2 = 0; i2 < bArr.length; i2++) {
                byte b = (byte) (i ^ bArr[i2]);
                i = bArr[i2];
                bArr[i2] = b;
            }
        }
        return bArr;
    }

    @Override
    public void newReading(String deviceName, Double temperature, Double stGravity, String json) {
        // We log de reading on DB
        Reading reading = new Reading();
        reading.setMoment(new Timestamp(Calendar.getInstance().getTimeInMillis()));
        reading.setHydromName(deviceName);
        reading.setGravity(stGravity);
        reading.setTemp(temperature);
        reading.setJson(json);
        readingDao.addReading(reading);
        // Let's see if the Hydrom exists
        List<Hydrom> hydroms = hydromDao.getHydromsByName(deviceName);
        if (hydroms == null || hydroms.isEmpty()) {
            // TODO: LOG the error

        } else {
            Date today = Calendar.getInstance().getTime();
            for (Hydrom hydrom : hydroms) {
                Config config = configDao.getConfig(hydrom.getConfigId());
                if (!today.before(config.getStartDate()) && !today.after(config.getEndDate())) {
                    Range range = rangeDao.getApplicableRange(hydrom.getConfigId(), stGravity);
                    if (range == null) {
                        // TODO: LOG the error

                    } else {
                        Tplink tpLinkWarm = tplinkDao.getTplinkByConfig(hydrom.getConfigId(), CoreConstants.TPLINK_TYPE_WARM);
                        Tplink tpLinkCold = tplinkDao.getTplinkByConfig(hydrom.getConfigId(), CoreConstants.TPLINK_TYPE_COLD);
                        if (temperature > range.getAimedTemp() + config.getTolerance()) {
                            // We have to cool the worth
                            if (tpLinkWarm != null) {
                                this.sendMessage2Tplink(tpLinkWarm.getIp(), CoreConstants.TPLINK_OFF_MESSAGE);
                            }
                            if (tpLinkCold != null) {
                                this.sendMessage2Tplink(tpLinkCold.getIp(), CoreConstants.TPLINK_ON_MESSAGE);
                            }
                            // TODO: LOG what we have done
                        } else if (temperature < range.getAimedTemp() - config.getTolerance()) {
                            // We have to heat the worth
                            if (tpLinkWarm != null) {
                                this.sendMessage2Tplink(tpLinkWarm.getIp(), CoreConstants.TPLINK_ON_MESSAGE);
                            }
                            if (tpLinkCold != null) {
                                this.sendMessage2Tplink(tpLinkCold.getIp(), CoreConstants.TPLINK_OFF_MESSAGE);
                            }
                            // TODO: LOG what we have done
                        } else {
                            // TODO: Everithing is allright, LOG we do nothing
                        }
                    }
                }
            }
        }
    }

}
