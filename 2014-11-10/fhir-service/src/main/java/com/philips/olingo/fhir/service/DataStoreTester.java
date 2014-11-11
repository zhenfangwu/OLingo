package com.philips.olingo.fhir.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class DataStoreTester {
	
	public static void main(String[] args) {
//		test_001();
//		test_002(DataStore.TYPE_PATIENT);
//		test_002(DataStore.TYPE_BOOK);
		test_003();
	}

	private static void test_003() {
		String jsonString = "{\"content\": {\"resourceType\": \"Patient\",\"text\": {\"status\": \"generated\",\"div\": \"<div>!-- Snipped for Brevity --></div>\"},\"identifier\": [{\"label\": \"SSN\",\"system\": \"http://hl7.org/fhir/sid/us-ssn\",\"value\": \"444222222\"}],\"name\": [{\"use\": \"official\",\"family\": [\"Everywoman\"],\"given\": [\"Eve\"]}]}}";
		JSONParser parser = new JSONParser();
		try {
			JSONObject jsonObj = (JSONObject) parser.parse(jsonString);
			JSONObject content = (JSONObject) jsonObj.get("content");
			JSONArray names = (JSONArray) content.get("name");
			JSONObject name = (JSONObject) names.get(0);
			String use = (String) name.get("use");
			
			JSONArray families = (JSONArray) name.get("family");
			String family = (String) families.get(0);
			
			JSONArray givens = (JSONArray) name.get("given");
			String given = (String) givens.get(0);

			System.out.println(use + " : " + family + ", " + given);
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}

	private static void test_002(String type) {
		DataStore ds = new DataStore();
		List<Map<String, Object>> patients = ds.getEntities(type);
		for (Map<String, Object> patient : patients) {
			System.out.println(patient.toString());
		}
	}

	private static void test_001() {
		String link = "http://www.hl7.org/implement/standards/FHIR-Develop/patient-examples.json";
        StringBuilder out = new StringBuilder();
		try {
			URL url = new URL(link);
			HttpURLConnection connection = (HttpURLConnection)url.openConnection();
			connection.setRequestMethod("GET");
			connection.connect();

			InputStream stream = connection.getInputStream();
			BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
	        String line;
	        while ((line = reader.readLine()) != null) {
	            out.append(line);
	        }
	        System.out.println(out.toString());
	        reader.close();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		// Create the entities
	    List<Map<String, Object>> entities = new ArrayList<Map<String, Object>>();
	    
	    JSONParser parser = new JSONParser();
	    JSONObject jsonObj = null;
		try {
			jsonObj = (JSONObject) parser.parse(out.toString());
		    JSONArray patientList = (JSONArray) jsonObj.get("entry");
		    Iterator<JSONObject> iter = patientList.iterator();
		    while (iter.hasNext()) {
		    	JSONObject patient = iter.next();
		    	String id = (String) patient.get("id");
		    	JSONObject content = (JSONObject) patient.get("content");
		    	JSONArray namesJson = (JSONArray) content.get("name");
		    	JSONObject nameJson = (JSONObject) namesJson.get(0);
		    	
		    	JSONArray lastNames = (JSONArray) nameJson.get("family");
		    	JSONArray firstNames = (JSONArray) nameJson.get("given");
		    	String name = lastNames.get(0) + ", " + firstNames.get(0);
		    	HashMap<String, Object> entity = new HashMap<String, Object>();
		    	entity.put("id", id);
		    	entity.put("name", name);
		    	entities.add(entity);
		    }
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		for (Map<String, Object> entity : entities) {
			System.out.println(entity.toString());
		}
	}
	
	/*public static void main(String[] args) {
		InputStream content = null;
		String url = "";
		try {
			HttpClient httpClient = new DefaultHttpClient();
			HttpResponse response = httpClient.execute(new HttpGet(url));
		} catch (Exception e) {
			System.out.println("[GET REQUEST] "+" Network exception:\n" + e);
		}
		System.out.println(content.toString());
	}*/

}
