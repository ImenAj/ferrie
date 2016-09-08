package pfe.imen.isg;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

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
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.ParseException;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class Panier  extends Activity{
	ListView listPanier ;
	String iddPRD, iddCLT, quantt, prx , id_pan;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.panier);
		
		listPanier  = ( ListView)findViewById(R.id.listVpanier);
		 String result = null;
	     InputStream is = null;
	     JSONObject json_data=null;
	     ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(); 
	     nameValuePairs.add(new BasicNameValuePair("id_C",Espace_Client.id_client));
	     nameValuePairs.add(new BasicNameValuePair("id_CLT",Authentification.id_client));
	      ArrayList<String> donnees = new ArrayList<String>();
	     
	     
	     ArrayList<HashMap<String, Object>> listItem = new ArrayList<HashMap<String, Object>>();
	     HashMap<String, Object> mapPPPP=null;
	     try{
	     //commandes httpClient
	     HttpClient httpclient = new DefaultHttpClient();
	        HttpPost httppost = new HttpPost("http://10.0.2.2/Vente_imen/panier.php");
	        httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
	        HttpResponse response = httpclient.execute(httppost);
	        HttpEntity entity = response.getEntity();
	        is = entity.getContent();
	     }
	     catch(Exception e){
	      Log.i("taghttppost",""+e.toString());
	            Toast.makeText(getBaseContext(),e.toString() ,Toast.LENGTH_LONG).show();
	       }
	   
	      
	    
	        try
	        {
	         BufferedReader reader = new BufferedReader(new InputStreamReader(is,"UTF-8"));
	        
	         StringBuilder sb  = new StringBuilder();
	        
	         String line = null;
	        
	         while ((line = reader.readLine()) != null)
	         {
	         sb.append(line + "\n");
	         }
	        
	         is.close();
	        
	         result = sb.toString();
	         Log.i("result",result);
	        }
	        catch(Exception e)
	        {
	         Log.i("tagconvertstr",""+e.toString());
	        }
	        
	        try{
	          JSONArray jArray = new JSONArray(result);
	           
	             for(int i=0;i<jArray.length();i++)
	             {
	            	 
	              json_data = jArray.getJSONObject(i); 
	                    String nom = json_data.getString("Image_prod");
	              
	                 String url = "http://10.0.2.2/vente_imen/images/"+nom;
	               	 Log.e("url",url); // test de l'url
					Bitmap bitmap = null;
	        		try {
						bitmap = BitmapFactory.decodeStream((InputStream) new URL(url).getContent()); // mrttre le contenue de l'url dans le variable bitmap
					} catch (MalformedURLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
					 mapPPPP = new HashMap<String, Object>();
					
	                   mapPPPP.put("img",resize(bitmap, 50, 50));
	                   mapPPPP.put("nom",json_data.getString("Nom_prod")); 
	                   mapPPPP.put("prix_tot",json_data.getString("Prix_totale")); 
	                   mapPPPP.put("quantite",json_data.getString("quantite")); 
	                   mapPPPP.put("ID_prod",json_data.getString("Id_prod")); 
	                   mapPPPP.put("client",json_data.getString("Client_Id_cl")); 
	                   mapPPPP.put("panier",json_data.getString("Id_pan")); 
	                   listItem.add(mapPPPP);
	                    
					
	               }
	             
	            }
	            catch(JSONException e){
	             Log.i("tagjsonexp",""+e.toString());
	            } catch (ParseException e) {
	             Log.i("tagjsonpars",""+e.toString());
	       }
	            ArrayList<HashMap<String, Object>> listItema = listItem;
	            SimpleAdapter tab = new SimpleAdapter(this.getBaseContext(), listItema, R.layout.item_panier,
	                   new String[] {"img", "nom", "prix_tot", "quantite"}, new int[] {R.id.img_prod, R.id.tv_nom_prod_panier, R.id.tv_prix_tot_panier , R.id.tv_quantite_panier});
	            tab.setViewBinder(new MyViewBinder());
	           listPanier.setAdapter(tab);
	            
	           listPanier.setOnItemClickListener(new OnItemClickListener() {
	    			@Override
					@SuppressWarnings("unchecked")
	             	public void onItemClick(AdapterView<?> a, View v, int position, long id) {
	    				
	    				final HashMap<String, Object> map = (HashMap<String, Object>) listPanier.getItemAtPosition(position);
	    				 String item = listPanier.getItemAtPosition(position).toString();
	    				 
	    				 iddPRD = (String) map.get("ID_prod") ;
	    				 iddCLT = (String) map.get("client") ;
	    				 quantt = (String) map.get("quantite") ;
	    				 prx = (String) map.get("prix_tot") ;
	    				 id_pan =   (String) map.get("panier") ;
	    				 
	    				 // alerte dialogue 
	    				 AlertDialog.Builder alert = new AlertDialog.Builder(Panier.this);
	    				 alert.setTitle("Ligne Panier");
	    				  alert.setIcon(R.drawable.ico_pan);
	    				 alert.setItems(R.array.action, new DialogInterface.OnClickListener() {
							
							@Override
							public void onClick(DialogInterface arg0, int arg1) {
								// TODO Auto-generated method stub
								switch(arg1){
								case 0 : ValiderLignePanier() ; break;
								case 1 : ViderLignePanier(); break;
								default : Toast.makeText(Panier.this, "Erreur", Toast.LENGTH_SHORT).show();
								}
							}

							private void ViderLignePanier() {
								

								
									InputStream is = null;
									String result = "";
								
									ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(); 
									nameValuePairs.add(new BasicNameValuePair("ligne_panier",id_pan));
									 
									
									try{
										HttpClient httpclient = new DefaultHttpClient();
										HttpPost httppost = new HttpPost("http://10.0.2.2/Vente_imen/supprmimer_ligne_panier.php");
										httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
										HttpResponse response = httpclient.execute(httppost);
										HttpEntity entity = response.getEntity();
										 

									}catch(Exception e){
										Log.e("log_tag", "Error in http connection " + e.toString());
									}
									
									finish();
									////////////////////////////
									// Convertion de la requÃªte en string
								
									
									/*try{
										BufferedReader reader = new BufferedReader(new InputStreamReader(is,"iso-8859-1"),8);
										StringBuilder sb = new StringBuilder();
										String line = null;
										while ((line = reader.readLine()) != null) {
											sb.append(line + "\n");
										}
										is.close();
										result=sb.toString();
									}catch(Exception e){
										Log.e("log_tag", "Error converting result " + e.toString());
									}
									
									if(result.contains("Ok")){
										// si la modification a etes effectué on va afficher un message de succces
										 Toast.makeText(Panier.this.getApplicationContext(),"Ligne Panier Supprimée", Toast.LENGTH_LONG).show();
								}
									else 
									{Toast t ;
									t = Toast.makeText(Panier.this.getApplicationContext(), "Erreur Connectn", Toast.LENGTH_LONG);
									t.show();
									 
									} */
									
									///////////////////////////////////////
									
									
									
									
							}

							private void ValiderLignePanier() {

								ArrayList<NameValuePair> postParameters = new ArrayList<NameValuePair>();
							    postParameters.add(new BasicNameValuePair("QUANTITE", quantt)); 
							   	postParameters.add(new BasicNameValuePair("PRODUIT", iddPRD )); 
							   	postParameters.add(new BasicNameValuePair("CLIENT", iddCLT)); 
							   	postParameters.add(new BasicNameValuePair("TOTALE",  prx)); 
							   	
							   	String response = null;

									 try {
							   	    response = CustomHttpClient.executeHttpPost("http://10.0.2.2/Vente_imen/valider_ligne_panier.php", postParameters);
							   	    String res=response.toString();
							   	   // res = res.trim();
							   	    res= res.replaceAll("\\s+","");         	              	 
							   	    //error.setText(res);
							   	    if(res.equals("1")){ 
							   	    	Toast.makeText(Panier.this, "Ligne Panier validée avec succées", Toast.LENGTH_SHORT).show();
										 Intent i = new Intent(Panier.this,Menu_Clients.class);
										startActivity(i); // demmarer l'action
									 
										return;
							   	    }
							   	} catch (Exception e) {
							   		 
							   	}
								
							}
						}); 
	    				 
	    				 alert.show();	    				
	    		 	}
	            	
	            });
	            
	           

	}
	private Bitmap resize(Bitmap bm, int w, int h)
	{
		int width = bm.getWidth();
		int height = bm.getHeight();
		int newWidth = w;
		int newHeight = h;
		float scaleWidth = ((float) newWidth) / width;
		float scaleHeight = ((float) newHeight) / height;

		Matrix matrix = new Matrix();
		matrix.postScale(scaleWidth, scaleHeight);
		Bitmap resizedBitmap = Bitmap.createBitmap(bm, 0, 0, width, height, matrix, true);

		return resizedBitmap;
	}
		
		
	}
