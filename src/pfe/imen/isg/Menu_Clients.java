package pfe.imen.isg;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;

public class Menu_Clients extends Activity{
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.menu_client);
		
		ImageButton panier = (ImageButton)findViewById(R.id.bt_panier);
		ImageButton commande= (ImageButton)findViewById(R.id.bt_commande);
		
		panier.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent i = new Intent(getApplicationContext(), Panier.class);
				startActivity(i);
			}
		});
		
			commande.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent i = new Intent(getApplicationContext(), Commandes.class);
				startActivity(i);
			}
		});
	}

}
