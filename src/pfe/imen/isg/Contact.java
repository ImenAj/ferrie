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
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

public class Contact  extends Activity{
	EditText e_nomprenom, e_tel, e_msg, e_email ;
	String nom_prenom, tel, msg , mail ,sujet_choisit;
	Spinner subject ;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.contact);
		e_nomprenom = (EditText)findViewById(R.id.ed_nom_prenom);
		e_tel = (EditText)findViewById(R.id.ed_tel_contact);
		e_msg = (EditText)findViewById(R.id.ed_msg);
		e_email = (EditText)findViewById(R.id.ed_email_contact);
		subject = (Spinner)findViewById(R.id.sp_subject);
		ImageButton envoyer = (ImageButton)findViewById(R.id.bt_envoyer);
		// charger le spinner par la liste des sujets 
		 ArrayAdapter adapter = ArrayAdapter.createFromResource(this, R.array.sujet, android.R.layout.simple_spinner_item);
	     adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // ce ligne pour ajouter le radio bouton devant chaque item
		 subject.setAdapter(adapter);
		// action de bouton envoyer 
		envoyer.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// recuperation de  contenu des champs 
				nom_prenom = e_nomprenom.getText().toString();
				tel = e_tel.getText().toString();
				msg = e_msg.getText().toString();
				mail = e_email.getText().toString();
				sujet_choisit = subject.getSelectedItem().toString();
				// verification si les champs sont vides 
				if ( nom_prenom.equals("") || tel.equals("") ||  msg.equals("")){
					// affichage de message 
					Toast.makeText(Contact.this, "SVP remplir tous les champs", Toast.LENGTH_SHORT).show();
					return ; 
				}
				
				// verification sur l'email 
				// On déclare le pattern que l’on doit vérifier
				Pattern p = Pattern.compile(".+@.+\\.[a-z]+");
				// On déclare un matcher, qui comparera le pattern avec la
				// string passée en argument
				Matcher m = p.matcher(mail);
				// Si l’adresse mail saisie ne correspond au format d’une
				// adresse mail on un affiche un message à l'utilisateur
				if (!m.matches()) { 
					 // afficher message d'erreur 
					Toast.makeText(Contact.this,"Email nom valide", Toast.LENGTH_SHORT).show();
						return;
				}
				// insertion dans la base de données 
				 ArrayList<NameValuePair> postParameters = new ArrayList<NameValuePair>();
				    postParameters.add(new BasicNameValuePair("sujet",sujet_choisit)); 
				   	postParameters.add(new BasicNameValuePair("contenue", msg )); 
				   	postParameters.add(new BasicNameValuePair("np", nom_prenom ));
				   	postParameters.add(new BasicNameValuePair("gsm", tel )); 
				   	postParameters.add(new BasicNameValuePair("email", mail )); 
				   	
				   	String response = null;

						 try {
				   	    response = CustomHttpClient.executeHttpPost("http://10.0.2.2/Vente_imen/contact.php", postParameters);
				   	    String res=response.toString();
				   	   // res = res.trim();
				   	    res= res.replaceAll("\\s+","");         	              	 
				   	    //error.setText(res);
				   	    if(res.equals("1")){
				   		     // vider les champs apres l'insertion 
								e_nomprenom.setText("");
								e_msg.setText("");
								e_tel.setText("");  
								// afficher une alerte dialogue si l'inscription a tes effectue avec succes
								 Toast.makeText(Contact.this, "Votre message a etes envoyaée avec succes", Toast.LENGTH_SHORT).show();
								Intent i = new Intent(Contact.this, Menu_Principale.class);
								startActivity(i);
								 return;
				   	    }
				   	} catch (Exception e) {
				   		 
				   	}
					}	});
						 
					}
		}
				 

