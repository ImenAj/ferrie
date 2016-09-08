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
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class Detail_Produits  extends Activity{
	String url_detaille_produit  = "http://10.0.2.2/Vente_imen/detaille_produits.php" ;
	TextView nom, prix, etat, description ;
	public static String quantite_dispo, id_prod_choisit, prix_prod_choisit  ; 
	ImageView img ;
	String image ;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.detail_produits);
		nom= (TextView)findViewById(R.id.nom_prod);
		prix= (TextView)findViewById(R.id.prix_prod);
		etat= (TextView)findViewById(R.id.etat_prod);
		description= (TextView)findViewById(R.id.description_prod);
		img = (ImageView)findViewById(R.id.image_prod);
		ImageButton add_panier =(ImageButton)findViewById(R.id.bt_add_panier);
		ImageButton retour_menu =(ImageButton)findViewById(R.id.bt_menu);
		
		 
    	String result =null;
    	InputStream is = null;
    	StringBuilder sb = new StringBuilder();
    	ArrayList<NameValuePair> nameValuePairs=new ArrayList<NameValuePair> ();
    	// ici on va envoyer le login et password saisi par le client au fichier php pour l'utiliser dans la requete sql 
    	// exactement condition
    	nameValuePairs.add(new BasicNameValuePair("prod_choisit",Produits.num_prod_choisit)); 
	//Execute HTTP Post Request
	try{
    	// execution de la requete sql qui se trouve dans le ficgier php (authentification.php) en utilisant la methode POST et le protocole HTTP
		HttpClient httpclient = new DefaultHttpClient();
		HttpPost httppost = new HttpPost(url_detaille_produit);
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
		   nom.setText(json_data.getString("Nom_prod").toString()); 
		   prix.setText(json_data.getString("Prix").toString()); 
		   etat.setText(json_data.getString("Etat_prod").toString()); 
		   description.setText(json_data.getString("Description").toString()); 
		   image = json_data.getString("Image_prod").toString();
		   
		   quantite_dispo = json_data.getString("Quantite").toString(); // recuperer la quentite de produit dsiponible
		   id_prod_choisit = json_data.getString("Id_prod").toString();  
		   prix_prod_choisit = json_data.getString("Prix").toString();  
		  
		// recuperation de l'image de produit  
			Drawable drawable = LoadImageFromWebOperations("http://10.0.2.2/vente_imen/images/"+image);
					img.setImageDrawable(drawable); 
					
		
		
   }catch(JSONException e){
	 //  Log.e("log_tag", "Error parsing data " + e.toString());
	   Toast.makeText(Detail_Produits.this, "Erreur Connexion", Toast.LENGTH_LONG).show();
   }
 
	
	
///////// action de bouton ajouter au panier 
	
  retour_menu.setOnClickListener(new OnClickListener() {
		
		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			Intent i = new Intent(Detail_Produits.this, Menu_Principale.class);
			startActivity(i);
		}
	});
	 
	add_panier.setOnClickListener(new OnClickListener() {
		
		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			Intent i = new Intent(Detail_Produits.this, Authentification.class);
			startActivity(i);
		}
	});
	}
 
	// metjode qui va charger une image apartir d'un URL 
	private Drawable LoadImageFromWebOperations(String url){
		try{
		InputStream is = (InputStream) new URL(url).getContent();
		Drawable d = Drawable.createFromStream(is, "src name");
		return d;
		}catch (Exception e) {
		System.out.println("Exc="+e);
		return null;
		}
  }
}
