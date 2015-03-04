package com.mechinn.android.ouralliance.fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.activeandroid.ActiveAndroid;
import com.activeandroid.Model;
import com.mechinn.android.ouralliance.Prefs;
import com.mechinn.android.ouralliance.R;
import com.mechinn.android.ouralliance.Utility;
import com.mechinn.android.ouralliance.adapter.MatchTeamSelectAdapter;
import com.mechinn.android.ouralliance.data.Event;
import com.mechinn.android.ouralliance.data.EventTeam;
import com.mechinn.android.ouralliance.data.Team;
import com.mechinn.android.ouralliance.data.TeamScouting;
import com.mechinn.android.ouralliance.data.frc2014.MatchScouting2014;
import com.mechinn.android.ouralliance.data.frc2014.TeamScouting2014;
import com.mechinn.android.ouralliance.data.frc2015.TeamScouting2015;

import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.EventBus;
import de.greenrobot.event.util.AsyncExecutor;

public class InsertTeamDialogFragment extends DialogFragment {
    public static final String TAG = "InsertTeamDialogFrag";
    public static final String RANK_ARG = "rank";

    private View dialog;
    private TextView teamNumber;
    private Team team;
    private Prefs prefs;
    private int rank;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        prefs = new Prefs(activity);
    }
	
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		setRetainInstance(true);
		// Use the Builder class for convenient dialog construction
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		LayoutInflater inflater = getActivity().getLayoutInflater();
		dialog = inflater.inflate(R.layout.dialog_team_insert, null);
		teamNumber = (TextView) dialog.findViewById(R.id.editTeamNumber);
        rank = getArguments().getInt(RANK_ARG);
		int yes;
        team = new Team();
        yes = R.string.create;
        Log.d(TAG, "insert");
		builder.setView(dialog)
			.setPositiveButton(yes, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    team.setTeamNumber(Utility.getIntFromText(teamNumber.getText()));
                    AsyncExecutor.create().execute(new AsyncExecutor.RunnableEx() {
                        @Override
                        public void run() throws Exception {
                            ActiveAndroid.beginTransaction();
                            try {
                                team.saveMod();
                                Log.d(TAG, "saving team id: " + team.getId());
                                Log.d(TAG, "saving: " + team);
                                Log.d(TAG, "competition id: " + prefs.getComp());
                                EventTeam eventTeam = new EventTeam();
                                Event event = Model.load(Event.class, prefs.getComp());
                                eventTeam.setEvent(event);
                                eventTeam.setTeam(team);
                                eventTeam.setRank(rank);
                                eventTeam.saveMod();
                                Log.d(TAG, "saving event team id: " + eventTeam.getId());
                                TeamScouting scouting = null;
                                switch (prefs.getYear()) {
                                    case 2014:
                                        scouting = new TeamScouting2014();
                                        break;
                                    case 2015:
                                        scouting = new TeamScouting2015();
                                        break;
                                }
                                scouting.setTeam(team);
                                scouting.saveMod();
                                ActiveAndroid.setTransactionSuccessful();
                                EventBus.getDefault().post(team);
                                EventBus.getDefault().post(eventTeam);
                                switch (prefs.getYear()) {
                                    case 2014:
                                        EventBus.getDefault().post(new TeamScouting2014());
                                        break;
                                    case 2015:
                                        EventBus.getDefault().post(new TeamScouting2015());
                                        break;
                                }
                            } finally {
                                ActiveAndroid.endTransaction();
                            }
                        }
                    });
                }
            }).setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        });
		// Create the AlertDialog object and return it
		return builder.create();
	}
}
