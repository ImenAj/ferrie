package pfe.imen.isg;

 

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

 

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

public class Inscription  extends Activity{
	EditText e_nom, e_prenom, e_adresse,e_naissance , e_cin , e_mail, e_tel, e_login, e_password ;
	String nom, prenom, ville_choisit , naissance, tel, email , login , password , cin , adresse;
	Spinner ville ;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.inscription);
		// declaration
		e_nom = (EditText)findViewById(R.id.ed_nom);
		e_prenom = (EditText)findViewById(R.id.ed_prenom);
		e_adresse = (EditText)findViewById(R.id.ed_adresse);
		e_tel = (EditText)findViewById(R.id.ed_gsm);
		e_cin = (EditText)findViewById(R.id.ed_cin);
		e_mail = (EditText)findViewById(R.id.ed_mail);
		e_naissance = (EditText)findViewById(R.id.ed_naissance);
		e_login = (EditText)findViewById(R.id.ed_login);
		e_password = (EditText)findViewById(R.id.ed_password);
		ville = (Spinner)findViewById(R.id.sp_ville);
		Button inscrire = (Button)findViewById(R.id.bt_inscription);
		
		//  charger le spinner ville par la liste des villes 
		 // creation d'un adapter ( tableau ) et on va le remplir par les gouvernera ( sousse ,tunis ... )
	     // depuis le fichier array  exactement de le tableau gouvernera
		 ArrayAdapter adapter = ArrayAdapter.createFromResource(this, R.array.gouvernera, android.R.layout.simple_spinner_item);
	     adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // ce ligne pour ajouter le radio bouton devant chaque item
		 ville.setAdapter(adapter);// ici charger le spinner par le tableau créer
		
		//action de bouton s'inscrire
		inscrire.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// Traitement a faire lors de clique sur le bouton s'inscrire 
				// 1 recuperation de contenue de tous les champs 
				nom = e_nom.getText().toString();
				prenom = e_prenom.getText().toString();
				tel = e_tel.getText().toString();
				cin = e_cin.getText().toString();
				email = e_mail.getText().toString();
				naissance = e_naissance.getText().toString();
				adresse = e_adresse.getText().toString();
				login = e_login.getText().toString();
				password = e_password.getText().toString();
				ville_choisit =  ville.getSelectedItem().toString() ;
				
			
				if ( nom.equals("") || prenom.equals("") || adresse.equals("") || tel.equals("") || email.equals("")  || naissance.equals("") || login.equals("")|| password.equals("")){
					
					Toast.makeText(Inscription.this, "SVP, remplir tous les champs", Toast.LENGTH_SHORT).show() ;
					return ; 
				}
				
				
				Pattern p = Pattern.compile(".+@.+\\.[a-z]+");
				
				Matcher m = p.matcher(email);
				
				if (!m.matches()) { 
					
					Toast.makeText(Inscription.this,"Email nom valide", Toast.LENGTH_SHORT).show();
						return;
				}
				
				
				  
				 ArrayList<NameValuePair> postParameters = new ArrayList<NameValuePair>();
		    postParameters.add(new BasicNameValuePair("cin", cin)); 
		   	postParameters.add(new BasicNameValuePair("nom", nom )); 
		   	postParameters.add(new BasicNameValuePair("prenom", prenom ));
		   	postParameters.add(new BasicNameValuePair("gsm", tel ));
		   	postParameters.add(new BasicNameValuePair("mail", email));
			postParameters.add(new BasicNameValuePair("adresse", adresse)); 
			postParameters.add(new BasicNameValuePair("date_naissance", naissance));
			postParameters.add(new BasicNameValuePair("ville", ville_choisit));
		   	postParameters.add(new BasicNameValuePair("username", login));
		   	postParameters.add(new BasicNameValuePair("password", password));
		   	
		   	String response = null;

				 try {
		   	    response = CustomHttpClient.executeHttpPost("http://10.0.2.2/Vente_imen/inscription.php", postParameters);
		   	    String res=response.toString();
		   	   // res = res.trim();
		   	    res= res.replaceAll("\\s+","");         	              	 
		   	    //error.setText(res);
		   	    if(res.equals("1")){
		   		     // vider les champs apres l'insertion 
						e_nom.setText("");
						e_prenom.setText("");
						e_tel.setText(""); 
						e_login.setText("");
						e_password.setText("");
						e_mail.setText("");
						e_cin.setText("");
						e_naissance.setText("");
						e_adresse.setText("");
						// afficher une alerte dialogue si l'inscription a tes effectue avec succes
						  AlertDialog.Builder alerte = new AlertDialog.Builder(Inscription.this); // declaration d'une alerte dialogue
						  alerte.setTitle("Inscription accepté"); // titre de l'alerte
						  alerte.setIcon(R.drawable.icons_trick); // icone de l'alerte
						  // message de la'lerte 
						  alerte.setMessage("Merci d’être inscrire vous pouvez être accéder à l'application avec votre login et mot de passe.");
						  //ajouter un bouton  
						  alerte.setPositiveButton("Se connecter", new DialogInterface.OnClickListener(){
							   
							   @Override
							public void onClick(DialogInterface dialog, int which) {
							 //   se decplacer vers l'espace authentification
								   Intent i = new Intent(Inscription.this,Authentification.class);
							    startActivity(i); // demmarer l'action
						   }});
						    
						   alerte.show(); // afficher l'alerte 
						   return;
		   	    }
		   	} catch (Exception e) {
		   		 
		   	}
			}	});
				 
			}
}
		 

