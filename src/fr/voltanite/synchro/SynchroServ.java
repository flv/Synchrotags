package fr.voltanite.synchro;

import org.json.JSONException;
import org.json.JSONObject;

import fr.voltanite.noeud.Noeud;

public class SynchroServ {

	public JSONObject addBDDtoJson(JSONObject json, Noeud noeud)
	{
			try {
				json.put("Noeud", noeud.toString());
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			
			
			return json;
	}



}
