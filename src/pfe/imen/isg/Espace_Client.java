package pfe.imen.isg;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
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
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class Espace_Client extends Activity{
	String username , password ;
	public static String  id_client;
	EditText ed_log, ed_pass ;
	String url_login ="http://10.0.2.2/Vente_imen/authentification.php"; 
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.authentification);
		
		ed_log = (EditText)findViewById(R.id.ed_login);
		ed_pass = (EditText)findViewById(R.id.ed_password);
		Button cnx = (Button)findViewById(R.id.bt_connecter);
		TextView inscrire = (TextView)findViewById(R.id.tv_inscrire) ;
		
		inscrire.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent i = new Intent(Espace_Client.this, Inscription.class);
				startActivity(i);
			}
		});
		cnx.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				
				username = ed_log.getText().toString(); 
				password = ed_pass.getText().toString();
				//verification si un ou les deux champs sont vides 
				if ( username.equals("") || password.equals("")){
					//afficher un message d'alerte en cas ou il seont vides
					Toast.makeText(Espace_Client.this, "SVP, remplir tous les champs", Toast.LENGTH_SHORT).show() ;
					return ; // pour arreter ici si l'un des deux champs est vides 
				}
				
				// comunication avec la base de données et verification de l'existante de user 
				
				    
            	Log.d("login saisi", username); 
            	Log.d("PASS saisi",password);
            	String result =null;
            	InputStream is = null;
            	StringBuilder sb = new StringBuilder();
            	ArrayList<NameValuePair> nameValuePairs=new ArrayList<NameValuePair> ();
            	
            	nameValuePairs.add(new BasicNameValuePair("pseudo",username));
            	nameValuePairs.add(new BasicNameValuePair("password",password));
			//Execute HTTP Post Request
			try{
            	// execution de la requete sql qui se trouve dans le ficgier php (authentification.php) en utilisant la methode POST et le protocole HTTP
        		HttpClient httpclient = new DefaultHttpClient();
        		HttpPost httppost = new HttpPost(url_login);
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
				  
				   String mdp  =	json_data.getString("Mot_de_p").toString(); 
				   String log  =json_data.getString("Login_cl").toString();
				   id_client  =json_data.getString("Id_cl").toString();
				   String nom_client  =json_data.getString("Nom_cl").toString();
				   Log.d("Nom de client est :",nom_client);
				   Log.d("ID de client est :",id_client);
				  
				   // verification d'user 
				   if ((mdp.equals(password)) && (log.equals(username)) ){
					// vider le champs apres l'authentification 
				         ed_log.setText(""); 
						 ed_pass.setText("");
						 		Intent i = new Intent(Espace_Client.this,Menu_Clients.class);
							    startActivity(i);
				   }
				   else {
					   
					   Toast.makeText(Espace_Client.this, "Verifier vos cordonnées ", Toast.LENGTH_LONG).show();
				   }
				
				
		   }catch(JSONException e){
			 //  Log.e("log_tag", "Error parsing data " + e.toString());
			   Toast.makeText(Espace_Client.this, "Erreur Connexion", Toast.LENGTH_LONG).show();
		   }
			
 			 
			}
		});
	}}

	
	
