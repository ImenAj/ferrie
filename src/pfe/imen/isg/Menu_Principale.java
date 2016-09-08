package pfe.imen.isg;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;

public class Menu_Principale  extends Activity{
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.menu_principale);
		// declaration des bouton
		ImageButton prodtuit = (ImageButton)findViewById(R.id.bt_produit);
		ImageButton promotion = (ImageButton)findViewById(R.id.bt_promo);
		ImageButton compte = (ImageButton)findViewById(R.id.bt_compte); 
		ImageButton boutique = (ImageButton)findViewById(R.id.bt_boutique); 
		// action de bouton 
		prodtuit.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// action a faire lors de clique sur le bouton 
				Intent i = new Intent(Menu_Principale.this, Categories.class);
				startActivity(i);
				
			}
		});
		
         promotion.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				
				Intent i = new Intent(Menu_Principale.this, Promotion.class);
				startActivity(i);
				
			}
		});
         
       
         
         compte.setOnClickListener(new OnClickListener() {
 			
 			@Override
 			public void onClick(View arg0) {
 				
 				Intent i = new Intent(Menu_Principale.this, Espace_Client.class);
 				startActivity(i);
 				
 			}
 		});
         
         boutique.setOnClickListener(new OnClickListener() {
 			
 			@Override
 			public void onClick(View arg0) {
 				// action a faire lors de clique sur le bouton 
 				Intent i = new Intent(Menu_Principale.this, Boutiques.class);
 				startActivity(i);
 				
 			}
 		});
	}

}

