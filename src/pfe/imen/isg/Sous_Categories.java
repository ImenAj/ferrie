package pfe.imen.isg;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
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
import android.content.Intent;
import android.net.ParseException;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class Sous_Categories  extends Activity{
	public static String  id_sous_categ_choisit ;
	ListView listV ;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.sous_categ);

		 listV =(ListView) findViewById(R.id.listView1);
	        
			
			// connexion a la base et recuperration des categories
	        String result = null;
		     InputStream is = null;
		     JSONObject json_data=null;
		     ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(); 
		     //anvoyer l'id de categories  au code php  pour afficher la liste de sous catégories qui appartient a cette catégories ( choisit dans l'interface ^recedant )
		     nameValuePairs.add(new BasicNameValuePair("num_categ",Categories.id_categ_choisit));
		     ArrayList<String> donnees = new ArrayList<String>();
		     
		     
		     ArrayList<HashMap<String, Object>> listItem = new ArrayList<HashMap<String, Object>>();
		     HashMap<String, Object> mapPPPP=null;
		     try{
		     //execution de la requete sql en utilisant le protocole http et la methode POST
		     HttpClient httpclient = new DefaultHttpClient();
		        HttpPost httppost = new HttpPost("http://10.0.2.2/Vente_imen/sous_categories.php");
		        httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
		        HttpResponse response = httpclient.execute(httppost);
		        HttpEntity entity = response.getEntity();
		        is = entity.getContent();
		     }
		     catch(Exception e){
		      Log.i("taghttppost",""+e.toString());
		            Toast.makeText(getBaseContext(),e.toString() ,Toast.LENGTH_LONG).show();
		       }
		   
		      
		     //conversion de la réponse en chaine de caractère
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
		        //recuperation des donnees json
		        try{
		          JSONArray jArray = new JSONArray(result);
		           
		             for(int i=0;i<jArray.length();i++)
		             {
		            	 
		              json_data = jArray.getJSONObject(i);
		                   
						 mapPPPP = new HashMap<String, Object>();
						 
		                   mapPPPP.put("nom",json_data.getString("Nom_sous_categ"));
		                   mapPPPP.put("idd",json_data.getString("Id_sous_categ"));  
		                   
		                   listItem.add(mapPPPP);
		                    
						
		               }
		             
		            }
		            catch(JSONException e){
		             Log.i("tagjsonexp",""+e.toString());
		            } catch (ParseException e) {
		             Log.i("tagjsonpars",""+e.toString());
		       }
		            ArrayList<HashMap<String, Object>> listItema = listItem;
		            SimpleAdapter m = new SimpleAdapter(this.getBaseContext(), listItema, R.layout.item_categ,
		                   new String[] {"nom"}, new int[] {R.id.tv_nom});
		            listV.setAdapter(m);
		            
		            // action a faire lor de clique sur un item de la liste 
		            listV.setOnItemClickListener(new OnItemClickListener() {
		    			@SuppressWarnings("unchecked")
		             	public void onItemClick(AdapterView<?> a, View v, int position, long id) {
		    				
		    				final HashMap<String, Object> map = (HashMap<String, Object>) listV.getItemAtPosition(position);
		    				 String item = listV.getItemAtPosition(position).toString();
		                       
		    				 Intent i = new Intent(Sous_Categories.this, Produits.class);
		    				 startActivity(i);
		    				  id_sous_categ_choisit = (String) map.get("idd") ; // recuperer l'id de categories choisit
		                    // Log.d("the  value is", DT.nomouvrier);
		                     
		    		 	}
		            	
		            });
		            
		           
		
	    }
	    
	}
