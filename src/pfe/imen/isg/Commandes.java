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

public class Commandes  extends Activity{
	ListView listPanier ;
	String iddPRD, iddCLT, quantt, prx , id_pan;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.commandes);
		
		listPanier  = ( ListView)findViewById(R.id.listView1);
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
	        HttpPost httppost = new HttpPost("http://10.0.2.2/Vente_imen/commandes.php");
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
	                   mapPPPP.put("prix_tot",json_data.getString("Prix_total")); 
	                   mapPPPP.put("quantite",json_data.getString("quantite"));    
	                   listItem.add(mapPPPP);
	                    
					
	               }
	             
	            }
	            catch(JSONException e){
	             Log.i("tagjsonexp",""+e.toString());
	            } catch (ParseException e) {
	             Log.i("tagjsonpars",""+e.toString());
	       }
	            ArrayList<HashMap<String, Object>> listItema = listItem;
	            SimpleAdapter tab = new SimpleAdapter(this.getBaseContext(), listItema, R.layout.item_commande,
	                   new String[] {"img", "nom", "quantite", "prix_tot"}, new int[] {R.id.img_prod, R.id.tv_nom, R.id.tquant , R.id.prix});
	            tab.setViewBinder(new MyViewBinder());
	           listPanier.setAdapter(tab);
	            
	          
	           

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
