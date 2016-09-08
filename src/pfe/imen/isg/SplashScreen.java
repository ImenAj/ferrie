package pfe.imen.isg;

 

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;

public class SplashScreen extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.splash);


		Thread timer = new Thread(){  
			@Override
			public void run(){
				try{
					sleep(5000); 
				}catch(InterruptedException e){
					e.printStackTrace();
				}finally{
				Intent i = new Intent (SplashScreen.this,Menu_Principale.class);
			       startActivity(i);
				}				
			}			
		};
		timer.start(); 
	}
	 


	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause(); 
		finish();
	}

}