package com.mechinn.frcscouting;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class FRCScouting extends Activity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        final Button scouting = (Button)  findViewById(R.id.scouting);
        scouting.setOnClickListener(new View.OnClickListener() {
        	public void onClick(View v) {
                // Perform action on click
//            	Toast.makeText(FRCScouting.this, "clicked", Toast.LENGTH_SHORT).show();
//        		Log.e("DebugInfo","button pressed"); 
        		String actionName = "com.mechinn.frcscouting.OpenScouting";
            	Intent intent = new Intent(actionName);
            	startActivity(intent);
            }
		});
        final Button teams = (Button)  findViewById(R.id.teams);
        teams.setOnClickListener(new View.OnClickListener() {
        	public void onClick(View v) {
                // Perform action on click
//            	Toast.makeText(FRCScouting.this, "clicked", Toast.LENGTH_SHORT).show();
//        		Log.e("DebugInfo","button pressed"); 
        		String actionName = "com.mechinn.frcscouting.OpenTeams";
            	Intent intent = new Intent(actionName);
            	startActivity(intent);
            }
		});
        final Button settings = (Button)  findViewById(R.id.settings);
        settings.setOnClickListener(new View.OnClickListener() {
        	public void onClick(View v) {
                // Perform action on click
//            	Toast.makeText(FRCScouting.this, "clicked", Toast.LENGTH_SHORT).show();
//        		Log.e("DebugInfo","button pressed"); 
        		String actionName = "com.mechinn.frcscouting.OpenSettings";
            	Intent intent = new Intent(actionName);
            	startActivity(intent);
            }
		});
    }
}