package pfe.imen.isg;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

public class Detail_Boutique  extends Activity{
	TextView nom, ville, adresse, email, fax, tel , cp ;
	String url_detail_boutique = "http://10.0.2.2/Vente_imen/detaille_boutique.php";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.detail_boutique);
		
		nom = (TextView)findViewById(R.id.nom_boutique);
		ville = (TextView)findViewById(R.id.ville_boutique);
		adresse = (TextView)findViewById(R.id.adresse_boutique);
		cp = (TextView)findViewById(R.id.cp_boutique);
		tel = (TextView)findViewById(R.id.tel_boutique);
		fax = (TextView)findViewById(R.id.fax_boutique);
		email = (TextView)findViewById(R.id.email_boutique);
		ImageButton  cal = (ImageButton)findViewById(R.id.bt_appeler);
		ImageButton  contacter = (ImageButton)findViewById(R.id.bt_contacter);
		
		 
		// action de bouton contacter 
		contacter.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// ouvrire l'interface de contact
				Intent i = new Intent(Detail_Boutique.this, Contact.class);
				startActivity(i);
			}
		});
		 
    	String result =null;
    	InputStream is = null;
    	StringBuilder sb = new StringBuilder();
    	ArrayList<NameValuePair> nameValuePairs=new ArrayList<NameValuePair> ();
    	// ici on va envoyer le login et password saisi par le client au fichier php pour l'utiliser dans la requete sql 
    	// exactement condition
    	nameValuePairs.add(new BasicNameValuePair("bou_choisit",Boutiques.id_bou_choisit)); 
	//Execute HTTP Post Request
	try{
    	// execution de la requete sql qui se trouve dans le ficgier php (authentification.php) en utilisant la methode POST et le protocole HTTP
		HttpClient httpclient = new DefaultHttpClient();
		HttpPost httppost = new HttpPost(url_detail_boutique);
		httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
		HttpResponse response = httpclient.execute(httppost);
		HttpEntity entity = response.getEntity();
		is = entity.getContent();    
	}catch(Exception e){
		Log.e("log_tag", "Error in http connection " + e.toString());
	} 
	try{
		// covertir le resultat en String 
		BufferedReader reader = new BufferedReader(new InputStreamReader(is,"iso-8859-1"),8);

		String line = null;
		while ((line = reader.readLine()) != null) {
			sb.append(line + "\n");
			
		}
		is.close();
		result=sb.toString();

	}catch(Exception e){
		Log.e("log_tag", "Error in http connection " + e.toString());
	}
	String essai=result.substring(0, 4) ;
	try {
		if (result.matches("<br >")){
			 essai=result.substring(0, 2) ; 
		}
		// parser les données JSON 
		JSONArray jArray = new JSONArray(result);
		int b=jArray.length();
		
		  JSONObject json_data = jArray.getJSONObject(0); 
		  // json_data.getString("nom de champs a recuperer dans la base de données");
		   nom.setText(json_data.getString("Nom_bou").toString()); 
		   adresse.setText(json_data.getString("Adresse_bou").toString()); 
		   ville.setText(json_data.getString("Ville_bou").toString()); 
		   cp.setText(json_data.getString("Cp_bou").toString()); 
		   email.setText(json_data.getString("Email_bou").toString()); 
		   tel.setText(json_data.getString("Tel_bou").toString()); 
		   fax.setText(json_data.getString("Fax_bou").toString()); 
		 
		
   }catch(JSONException e){
	 //  Log.e("log_tag", "Error parsing data " + e.toString());
	   Toast.makeText(Detail_Boutique.this, "Erreur Connexion", Toast.LENGTH_LONG).show();
   }
 
	// action de bouton appeler 
			cal.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					String tel_ferre = tel.getText().toString();
					 //appeler le boutique
					Intent i = new Intent("android.intent.action.CALL", Uri.parse("tel:" +tel_ferre));
					startActivity(i);
				}
			});
	
	
	}
}
