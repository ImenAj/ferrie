package pfe.imen.isg;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.spec.PSSParameterSpec;
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
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class Authentification  extends Activity{
	String quant ; 
	String username , password ;
	public static String id_client ; 
	EditText ed_log, ed_pass ;
	int total ;
	String url_login ="http://10.0.2.2/Vente_imen/authentification.php"; // attention de mettre localhost ou 127.0.0.1 car 
	// sa marche pas , il faut mettre 10.0.2.2. ( l'adresse de l'emulateur ) ou bien votre adresse ip 
	// et puisque l'adresse IP n'est pas fixe se change selon réseaux donc sa sera mieux mettre 10.0.2.2
	@Override
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
				Intent i = new Intent(Authentification.this, Inscription.class);
				startActivity(i);
			}
		});
		cnx.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// recuperation de contenue de champs login et mot passe 
				username = ed_log.getText().toString(); // recuperation de contenue de champs login 
				password = ed_pass.getText().toString();
				//verification si un ou les deux champs sont vides 
				if ( username.equals("") || password.equals("")){
					//afficher un message d'alerte en cas ou il seont vides
					Toast.makeText(Authentification.this, "SVP, remplir tous les champs", Toast.LENGTH_SHORT).show() ;
					return ; // pour arreter ici si l'un des deux champs est vides 
				}
				
				// comunication avec la base de données et verification de l'existante de user 
				
				    
            	Log.d("login saisi", username); // juste pour tester ( affichage dans logCat ) et pas dans l'emulateur
            	Log.d("PASS saisi",password);
            	String result =null;
            	InputStream is = null;
            	StringBuilder sb = new StringBuilder();
            	ArrayList<NameValuePair> nameValuePairs=new ArrayList<NameValuePair> ();
            	// ici on va envoyer le login et password saisi par le client au fichier php pour l'utiliser dans la requete sql 
            	// exactement condition
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
				  // json_data.getString("nom de champs a recuperer dans la base de données");
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
						 
						  openDialog(); // appel au methode qui va afficher l une alerte pour saisir la quantite 
					   }
				   else {
					   
					   Toast.makeText(Authentification.this, "Verifier vos cordonnées ", Toast.LENGTH_LONG).show();
				   }
				
				
		   }catch(JSONException e){
			 //  Log.e("log_tag", "Error parsing data " + e.toString());
			   Toast.makeText(Authentification.this, "Erreur Connexion", Toast.LENGTH_LONG).show();
		   }
			
 			 
			}
		});
	}

	
	//// N'oublier pas d'ajouter la persmission d'Internet    car il est obligatoire pour que la connexion avec 
	//la base de données sa marche.
	
	
/////////////////////////Affichage d'alerte  pour saisir la quantité ////////////////////////////////////
	 public void openDialog(){

	     LinearLayout linearLayout = new LinearLayout(this); // declaration d'un linear layout  text avec java 
	     linearLayout.setLayoutParams(new LayoutParams( // specifier la position et parametrre de linear layout
	                LayoutParams.FILL_PARENT,
	                LayoutParams.FILL_PARENT));
	     linearLayout.setPadding(30, 0, 30, 0);

	    final EditText saveas = new EditText(this); // declaration d'edit text avec java 
	    saveas.setLayoutParams(new LayoutParams(
	            LayoutParams.FILL_PARENT,
	            LayoutParams.FILL_PARENT));
	    
	   // saveas.setImeOptions(EditorInfo.IME_ACTION_DONE);
	   // saveas.setInputType(InputType.TYPE_TEXT_FLAG_CAP_SENTENCES);
	    saveas.setHint("Quantites");
	    AlertDialog.Builder dialog = new AlertDialog.Builder(this); // creation d'alert dialogue
	    dialog.setTitle("Ajout au panier");
	    dialog.setMessage("Saisir la quantite SVP :");

	    linearLayout.addView(saveas);// mettrre l'edit text de quantite dans le layout

	    dialog.setView(linearLayout); // mettre le layout créer dans l'alerte diaogue

	    dialog.setPositiveButton("ajouter", new DialogInterface.OnClickListener()  // ajout d'un bouton  ajouter
	    
	    {
	        public void onClick(DialogInterface dialoginterface, int buttons) 
	        {
                   quant =  saveas.getText().toString();// recuperer le contenue de champs quantité 
                   if (quant.equals("")) {
                	   Toast.makeText(Authentification.this, "Saisir la quentite SVP", Toast.LENGTH_SHORT).show(); 
                	   return ;
                   } 
                   Log.e("quantite demande", quant);  // affichage dans logCat c juste pour tester
                   int quant_demande = Integer.valueOf(quant);
                   int quantite_produit_dispo = Integer.valueOf(Detail_Produits.quantite_dispo.toString()); //  recuperer et convertir enti integerla quentité de produit disponible dans le stock
                   Log.e("quantite disponible ", Detail_Produits.quantite_dispo.toString());
                   // verificatipon si la quantité saisi est disponible 
                   
                   if(quant_demande > quantite_produit_dispo) {
                	   Toast.makeText(Authentification.this, "Stock non disponible", Toast.LENGTH_SHORT).show();
                	   Intent i = new Intent(Authentification.this, Detail_Produits.class);
                	   startActivity(i);
                	   return ;
                   } else {
                	    total = quant_demande * Integer.valueOf(Detail_Produits.prix_prod_choisit);
                	   Log.e("totale: ", String.valueOf(total));
                       ajoutPanier();// appel au methode qui va remplir mon panier par le produit choisit et la quantité saisie
                   }
                   }

			private void ajoutPanier() {
				ArrayList<NameValuePair> postParameters = new ArrayList<NameValuePair>();
			    postParameters.add(new BasicNameValuePair("QUANTITE", quant)); 
			   	postParameters.add(new BasicNameValuePair("PRODUIT", Detail_Produits.id_prod_choisit )); 
			   	postParameters.add(new BasicNameValuePair("CLIENT", id_client ));
			   	postParameters.add(new BasicNameValuePair("TOTALE", String.valueOf(total))); 
			   	
			   	String response = null;

					 try {
			   	    response = CustomHttpClient.executeHttpPost("http://10.0.2.2/Vente_imen/ajout_panier.php", postParameters);
			   	    String res=response.toString();
			   	   
			   	    res= res.replaceAll("\\s+","");         	              	 
			   	    
			   	    if(res.equals("1")){ 
							// afficher une alerte dialogue si l'inscription a tes effectue avec succes
							  AlertDialog.Builder alerte = new AlertDialog.Builder(Authentification.this); // declaration d'une alerte dialogue
							  alerte.setTitle("Ajout Produit au Panier"); // titre de l'alerte
							  alerte.setIcon(R.drawable.icons_trick); // icone de l'alerte
							 
							  alerte.setMessage("L'ajout de ce produit au panier a ete effectuée avec succées");
							   
							  alerte.setPositiveButton("Fermer", new DialogInterface.OnClickListener(){
								   
								   @Override
								public void onClick(DialogInterface dialog, int which) {
								
									   Intent i = new Intent(Authentification.this,Menu_Clients.class);
								    startActivity(i); // demmarer l'action
							   }});
							    
							   alerte.show();
							   return;
			   	    }
			   	} catch (Exception e) {
			   		 
			   	}
				}	}); 
			 

	    dialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() 
	    {
	        public void onClick(DialogInterface arg0, int buttons) 
	        {
	        }
	    });
	    dialog.show();
	 
  }  
	 //////////////////////////////////////////////////////////////////////////////////////////
}
