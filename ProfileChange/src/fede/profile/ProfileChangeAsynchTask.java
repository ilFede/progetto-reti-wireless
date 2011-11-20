package fede.profile;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.widget.Toast;

public class ProfileChangeAsynchTask extends AsyncTask<Void, Void, Void>{
	 //private final ProgressDialog dialog = new ProgressDialog(ProfilesSetService.this);

	
	protected Void doInBackground(){
		 // this.dialog.setMessage("Signing in...");
		    // this.dialog.show();
		return null;
	}

	@Override
	protected Void doInBackground(Void... params) {
		//Intent srvIntent = new Intent(getApplicationContext(), ProfilesSetService.class);
  	    //startService(srvIntent);
		//Toast.makeText(getBaseContext(), "ciao", Toast.LENGTH_LONG).show();
		return null;
	}
	
	

}
