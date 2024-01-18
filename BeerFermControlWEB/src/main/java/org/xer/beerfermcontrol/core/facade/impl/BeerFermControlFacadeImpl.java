package org.xer.beerfermcontrol.core.facade.impl;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.xer.beerfermcontrol.core.bean.Config;
import org.xer.beerfermcontrol.core.bean.Hydrom;
import org.xer.beerfermcontrol.core.bean.Range;
import org.xer.beerfermcontrol.core.bean.Reading;
import org.xer.beerfermcontrol.core.bean.Tplink;
import org.xer.beerfermcontrol.core.bean.Ulog;
import org.xer.beerfermcontrol.core.bean.User;
import org.xer.beerfermcontrol.core.dao.ConfigDao;
import org.xer.beerfermcontrol.core.dao.HydromDao;
import org.xer.beerfermcontrol.core.dao.RangeDao;
import org.xer.beerfermcontrol.core.dao.ReadingDao;
import org.xer.beerfermcontrol.core.dao.TplinkDao;
import org.xer.beerfermcontrol.core.dao.UlogDao;
import org.xer.beerfermcontrol.core.dao.UserDao;
import org.xer.beerfermcontrol.core.facade.BeerFermControlFacade;
import org.xer.beerfermcontrol.core.util.CoreConstants;
import org.xer.beerfermcontrol.core.util.TPLinkControl;

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
    private UlogDao ulogDao;
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
        ulogDao.addEvent(newConfig.getId(), "CONFIG CREATED: " + new JSONObject(newConfig));
    }

    @Override
    @Transactional
    public void removeConfig(Integer id, Integer userId) {
        Config config = this.getFullConfig(id, userId);
        configDao.removeConfig(id, userId);
        ulogDao.addEvent(id, "CONFIG DELETED: " + id);
        if (config.getHydrom() != null) {
            this.removeHydrom(config.getHydrom().getId(), id, userId);
            ulogDao.addEvent(id, "HYDROM DELETED: " + new JSONObject(config.getHydrom()));
        }
        if (config.getTplinkCold() != null) {
            this.removeTplink(config.getTplinkCold().getId(), id, userId);
            ulogDao.addEvent(id, "TPLINK DELETED: " + new JSONObject(config.getTplinkCold()));
        }
        if (config.getTplinkWarm() != null) {
            this.removeTplink(config.getTplinkWarm().getId(), id, userId);
            ulogDao.addEvent(id, "TPLINK DELETED: " + new JSONObject(config.getTplinkWarm()));
        }
        if (config.getRanges() != null) {
            for (Range range : config.getRanges()) {
                this.removeRange(range.getId(), id, userId);
                ulogDao.addEvent(id, "RANGE DELETED: " + new JSONObject(range));
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
        ulogDao.addEvent(config.getId(), "CONFIG UPDATED: " + new JSONObject(config));
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
            ulogDao.addEvent(configId, "USER " + userId + " TRYED TO ACCESS WITHOUT PERMISSION!");
            throw new RuntimeException("NO PERMISSION!");
        }
    }

    @Override
    @Transactional
    public void addHydrom(Hydrom newHydrom, Integer userId) {
        this.checkConfigForUser(newHydrom.getConfigId(), userId);
        hydromDao.addHydrom(newHydrom);
        ulogDao.addEvent(newHydrom.getConfigId(), "HYDROM CREATED: " + new JSONObject(newHydrom));
    }

    @Override
    @Transactional
    public void removeHydrom(Integer id, Integer configId, Integer userId) {
        this.checkConfigForUser(configId, userId);
        hydromDao.removeHydrom(id, configId);
        ulogDao.addEvent(configId, "HYDROM REMOVED: " + id);
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
        ulogDao.addEvent(hydrom.getConfigId(), "HYDROM UPDATED: " + new JSONObject(hydrom));
    }

    @Override
    @Transactional
    public void addTplink(Tplink tplink, Integer userId) {
        this.checkConfigForUser(tplink.getConfigId(), userId);
        tplinkDao.addTplink(tplink);
        ulogDao.addEvent(tplink.getConfigId(), "TPLINK CREATED: " + new JSONObject(tplink));
    }

    @Override
    @Transactional
    public void removeTplink(Integer id, Integer configId, Integer userId) {
        this.checkConfigForUser(configId, userId);
        tplinkDao.removeTplink(id, configId);
        ulogDao.addEvent(configId, "TPLINK REMOVED: " + id);
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
        ulogDao.addEvent(tplink.getConfigId(), "TPLINK UPDATED: " + new JSONObject(tplink));
    }

    @Override
    @Transactional
    public void addRange(Range range, Integer userId) {
        this.checkConfigForUser(range.getConfigId(), userId);
        rangeDao.addRange(range);
        ulogDao.addEvent(range.getConfigId(), "RANGE CREATED: " + new JSONObject(range));
    }

    @Override
    @Transactional
    public void removeRange(Integer id, Integer configId, Integer userId) {
        this.checkConfigForUser(configId, userId);
        rangeDao.removeRange(id, configId);
        ulogDao.addEvent(configId, "RANGE REMOVED: " + id);
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
        ulogDao.addEvent(range.getConfigId(), "RANGE UPDATED: " + new JSONObject(range));
    }

    @Override
    public boolean overlapsOtherRange(Range range, boolean exceptItself) {
        return rangeDao.overlapsOtherRange(range, exceptItself);
    }

    @Override
    @Transactional
    public void newReading(String deviceName, Double temperature, Double stGravity, String json) throws Exception {
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
            ulogDao.addEvent(0, "THERE'S NO HYDROM NAMED " + deviceName + "!!!");
        } else {
            Date today = Calendar.getInstance().getTime();
            for (Hydrom hydrom : hydroms) {
                Config config = configDao.getConfig(hydrom.getConfigId());
                if (!today.before(config.getStartDate()) && !today.after(config.getEndDate())) {
                    Range range = rangeDao.getApplicableRange(hydrom.getConfigId(), stGravity);
                    if (range == null) {
                        ulogDao.addEvent(config.getId(), "THERE'S NO RANGE COVERING " + stGravity + " GRAVITY!!!");
                    } else {
                        Tplink tpLinkWarm = tplinkDao.getTplinkByConfig(hydrom.getConfigId(), CoreConstants.TPLINK_TYPE_WARM);
                        Tplink tpLinkCold = tplinkDao.getTplinkByConfig(hydrom.getConfigId(), CoreConstants.TPLINK_TYPE_COLD);
                        if (temperature > range.getAimedTemp() + config.getTolerance()) {
                            // We have to cool the worth
                            if (tpLinkWarm != null) {
                                TPLinkControl tplinkControl = new TPLinkControl(tpLinkWarm.getIp(),
                                        tpLinkWarm.getUuid(), tpLinkWarm.getEmail(), tpLinkWarm.getPassword());
                                tplinkControl.turnOff();
                            }
                            if (tpLinkCold != null) {
                                TPLinkControl tplinkControl = new TPLinkControl(tpLinkCold.getIp(),
                                        tpLinkCold.getUuid(), tpLinkCold.getEmail(), tpLinkCold.getPassword());
                                tplinkControl.turnOn();
                            }
                            ulogDao.addEvent(config.getId(), "WARM TURNED OFF AND COLD TURNED ON");
                        } else if (temperature < range.getAimedTemp() - config.getTolerance()) {
                            // We have to heat the worth
                            if (tpLinkWarm != null) {
                                TPLinkControl tplinkControl = new TPLinkControl(tpLinkWarm.getIp(),
                                        tpLinkWarm.getUuid(), tpLinkWarm.getEmail(), tpLinkWarm.getPassword());
                                tplinkControl.turnOn();
                            }
                            if (tpLinkCold != null) {
                                TPLinkControl tplinkControl = new TPLinkControl(tpLinkCold.getIp(),
                                        tpLinkCold.getUuid(), tpLinkCold.getEmail(), tpLinkCold.getPassword());
                                tplinkControl.turnOff();
                            }
                            ulogDao.addEvent(config.getId(), "WARM TURNED ON AND COLD TURNED OFF");
                        } else {
                            ulogDao.addEvent(config.getId(), "WE ARE OK, NO TEMPERATURE CHANGE NEEDED");
                        }
                    }
                }
            }
        }
    }

    @Override
    public List<Ulog> getEventList(Integer configId, Integer userId) {
        this.checkConfigForUser(configId, userId);
        return ulogDao.getEventList(configId);
    }

    @Override
    public List<Reading> getReadingList(Integer configId, Integer userId, Date startDate) {
        this.checkConfigForUser(configId, userId);
        Hydrom hydrom = hydromDao.getHydromByConfig(configId);
        if (hydrom != null) {
            return readingDao.getReadingList(hydrom.getName(), startDate);
        } else {
            return null;
        }
    }

}
