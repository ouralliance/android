package com.mechinn.android.ouralliance.data.frc2014;

import android.app.Activity;
import android.util.Log;
import com.mechinn.android.ouralliance.data.*;
import org.supercsv.io.CsvBeanWriter;
import org.supercsv.prefs.CsvPreference;
import se.emilsjolander.sprinkles.CursorList;
import se.emilsjolander.sprinkles.Query;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class ExportTeamScouting2014 extends Export {
    public static final String TAG = "ExportTeamScouting2014";

	public ExportTeamScouting2014(Activity activity) {
		super(activity);
	}
    public ExportTeamScouting2014(Activity activity, OutputStream output) {
        super(activity, output);
    }

	public String work() {
        String result = null;
        if(isFileWrite()) {
            setFilename(getDirectory()+ Import.Type.TEAMSCOUTING2014.path());
            new File(getFilename()).mkdirs();
            setFilename(getFilename()+File.separator+getCompetition().getEventCode()+CSV);
            try {
                setWriter(new FileWriter(getFilename()));
            } catch (IOException e) {
                Log.e(TAG, e.toString());
                result = "Error opening writable file: "+getFilename();
            }
        }
        if(null==result) {
            CursorList<TeamScouting2014> teams = Query.many(TeamScouting2014.class,
                    "SELECT " + TeamScouting2014.TAG + ".*" +
                            " FROM " + TeamScouting2014.TAG +
                            " INNER JOIN " + CompetitionTeam.TAG +
                            " ON " + TeamScouting2014.TAG + "." + TeamScouting2014.TEAM + "=" + CompetitionTeam.TAG + "." + CompetitionTeam.TEAM +
                            " AND " + CompetitionTeam.COMPETITION + "=?", getPrefs().getComp()
            ).get();
            List<MoveTeamScouting2014> movingTeams = new ArrayList<MoveTeamScouting2014>();
            CursorList<TeamScoutingWheel> wheels;
            CompetitionTeam competitionTeam;
            for (TeamScouting2014 team : teams) {
                competitionTeam = Query.one(CompetitionTeam.class,
                        "SELECT *" +
                                " FROM " + CompetitionTeam.TAG +
                                " WHERE " + CompetitionTeam.COMPETITION + "=?" +
                                " AND " + CompetitionTeam.TEAM + "=?",
                        getPrefs().getComp(), team.getTeam().getId()
                ).get();
                wheels = Query.many(TeamScoutingWheel.class,
                        "SELECT *" +
                                " FROM " + TeamScoutingWheel.TAG +
                                " WHERE " + TeamScoutingWheel.YEAR + "=?" +
                                " AND " + TeamScoutingWheel.TEAM + "=?",
                        getPrefs().getYear(), team.getTeam().getId()
                ).get();
                if (null != wheels && wheels.size() > 0) {
                    for (TeamScoutingWheel wheel : wheels) {
                        movingTeams.add(new MoveTeamScouting2014(team, wheel,competitionTeam));
                    }
                } else {
                    movingTeams.add(new MoveTeamScouting2014(team,competitionTeam));
                }
            }
            CsvBeanWriter beanWriter = null;
            try {
                beanWriter = new CsvBeanWriter(getWriter(), CsvPreference.EXCEL_PREFERENCE);

                // write the header
                beanWriter.writeHeader(MoveTeamScouting2014.FIELD_MAPPING);

                // write the beans
                for (MoveTeamScouting2014 move : movingTeams) {
                    Log.d(TAG, "writing: " + move.toString());
                    beanWriter.write(move, MoveTeamScouting2014.FIELD_MAPPING, MoveTeamScouting2014.writeProcessor);
                }
            } catch (IOException e) {
                Log.e(TAG, e.toString());
                if (isFileWrite()) {
                    result = "Error writing to : " + getFilename();
                } else {
                    result = "Error sending to bluetooth device";
                }
            } finally {
                if (beanWriter != null) {
                    try {
                        beanWriter.close();
                    } catch (IOException e) {
                        Log.e(TAG, "error closing writer",e);
                        if (null == result) {
                            result = "Error transfering data";
                        }
                    }
                }
            }
        }
        return result;
	}
}
