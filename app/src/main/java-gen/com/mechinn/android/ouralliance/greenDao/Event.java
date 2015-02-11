package com.mechinn.android.ouralliance.greenDao;

import java.util.List;
import com.mechinn.android.ouralliance.greenDao.dao.DaoSession;
import de.greenrobot.dao.DaoException;

import com.mechinn.android.ouralliance.greenDao.dao.EventDao;
import com.mechinn.android.ouralliance.greenDao.dao.EventTeamDao;
import com.mechinn.android.ouralliance.greenDao.dao.MatchDao;

// THIS CODE IS GENERATED BY greenDAO, EDIT ONLY INSIDE THE "KEEP"-SECTIONS

// KEEP INCLUDES - put your custom includes here
// KEEP INCLUDES END
/**
 * Entity mapped to table EVENT.
 */
public class Event extends com.mechinn.android.ouralliance.OurAllianceObject  implements Comparable<Event>, java.io.Serializable {

    private Long id;
    /** Not-null value. */
    private java.util.Date modified;
    /** Not-null value. */
    private String shortName;
    /** Not-null value. */
    private String eventCode;
    private int eventType;
    private int eventDistrict;
    private int year;
    private String venueAddress;
    private String website;
    private java.util.Date startDate;
    private java.util.Date endDate;
    private Boolean official;

    /** Used to resolve relations */
    private transient DaoSession daoSession;

    /** Used for active entity operations. */
    private transient EventDao myDao;

    private List<EventTeam> teams;
    private List<Match> matches;

    // KEEP FIELDS - put your custom fields here
    public final static String TAG = "Event";
    // KEEP FIELDS END

    public Event() {
    }

    public Event(Long id) {
        this.id = id;
    }

    public Event(Long id, java.util.Date modified, String shortName, String eventCode, int eventType, int eventDistrict, int year, String venueAddress, String website, java.util.Date startDate, java.util.Date endDate, Boolean official) {
        this.id = id;
        this.modified = modified;
        this.shortName = shortName;
        this.eventCode = eventCode;
        this.eventType = eventType;
        this.eventDistrict = eventDistrict;
        this.year = year;
        this.venueAddress = venueAddress;
        this.website = website;
        this.startDate = startDate;
        this.endDate = endDate;
        this.official = official;
    }

    /** called by internal mechanisms, do not call yourself. */
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getEventDao() : null;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    /** Not-null value. */
    public java.util.Date getModified() {
        return modified;
    }

    /** Not-null value; ensure this value is available before it is saved to the database. */
    public void setModified(java.util.Date modified) {
        this.modified = modified;
    }

    /** Not-null value. */
    public String getShortName() {
        return shortName;
    }

    /** Not-null value; ensure this value is available before it is saved to the database. */
    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    /** Not-null value. */
    public String getEventCode() {
        return eventCode;
    }

    /** Not-null value; ensure this value is available before it is saved to the database. */
    public void setEventCode(String eventCode) {
        this.eventCode = eventCode;
    }

    public int getEventType() {
        return eventType;
    }

    public void setEventType(int eventType) {
        this.eventType = eventType;
    }

    public int getEventDistrict() {
        return eventDistrict;
    }

    public void setEventDistrict(int eventDistrict) {
        this.eventDistrict = eventDistrict;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public String getVenueAddress() {
        return venueAddress;
    }

    public void setVenueAddress(String venueAddress) {
        this.venueAddress = venueAddress;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public java.util.Date getStartDate() {
        return startDate;
    }

    public void setStartDate(java.util.Date startDate) {
        this.startDate = startDate;
    }

    public java.util.Date getEndDate() {
        return endDate;
    }

    public void setEndDate(java.util.Date endDate) {
        this.endDate = endDate;
    }

    public Boolean getOfficial() {
        return official;
    }

    public void setOfficial(Boolean official) {
        this.official = official;
    }

    /** To-many relationship, resolved on first access (and after reset). Changes to to-many relations are not persisted, make changes to the target entity. */
    public List<EventTeam> getTeams() {
        if (teams == null) {
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            EventTeamDao targetDao = daoSession.getEventTeamDao();
            List<EventTeam> teamsNew = targetDao._queryEvent_Teams(id);
            synchronized (this) {
                if(teams == null) {
                    teams = teamsNew;
                }
            }
        }
        return teams;
    }

    /** Resets a to-many relationship, making the next get call to query for a fresh result. */
    public synchronized void resetTeams() {
        teams = null;
    }

    /** To-many relationship, resolved on first access (and after reset). Changes to to-many relations are not persisted, make changes to the target entity. */
    public List<Match> getMatches() {
        if (matches == null) {
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            MatchDao targetDao = daoSession.getMatchDao();
            List<Match> matchesNew = targetDao._queryEvent_Matches(id);
            synchronized (this) {
                if(matches == null) {
                    matches = matchesNew;
                }
            }
        }
        return matches;
    }

    /** Resets a to-many relationship, making the next get call to query for a fresh result. */
    public synchronized void resetMatches() {
        matches = null;
    }

    /** Convenient call for {@link AbstractDao#delete(Object)}. Entity must attached to an entity context. */
    public void delete() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }    
        myDao.delete(this);
    }

    /** Convenient call for {@link AbstractDao#update(Object)}. Entity must attached to an entity context. */
    public void update() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }    
        myDao.update(this);
    }

    /** Convenient call for {@link AbstractDao#refresh(Object)}. Entity must attached to an entity context. */
    public void refresh() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }    
        myDao.refresh(this);
    }

    // KEEP METHODS - put your custom methods here
    public String toString() {
        return (getOfficial()?"Official":"Unofficial")+" | "+this.getShortName();
    }
    public boolean equals(Event data) {
        return  getYear()==data.getYear() &&
                getShortName().equals(data.getShortName()) &&
                getEventCode().equals(data.getEventCode());
    }
    public int compareTo(Event another) {
        int compare = (this.getOfficial()?1:0)-(another.getOfficial()?1:0);
        if(0==compare) {
            compare = this.getShortName().compareTo(another.getShortName());
        }
        return compare;
    }
    // KEEP METHODS END

}
