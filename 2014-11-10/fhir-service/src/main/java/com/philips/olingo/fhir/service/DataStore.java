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

import org.apache.log4j.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.philips.olingo.fhir.common.Constants;

public class DataStore implements Constants {
	protected static final Logger log = Logger.getLogger(DataStore.class);

	public List<Map<String, Object>> getEntities(String type) {
		if (TYPE_PATIENT.equalsIgnoreCase(type)) {
			return createEntities(URL_PATIENT, type);
		} else if (TYPE_OBSERVATION.equalsIgnoreCase(type)) {
			return createEntities(URL_OBSERVATION, type);
		} else if (TYPE_SCALAR.equalsIgnoreCase(type)) {
			return createEntities(URL_SCALAR, type);
		}
		
		return null;
	}
	
	@SuppressWarnings("unchecked")
	public HashMap<String, Object> getEntity(String id, String type, String key) {
		List<Map<String, Object>> list = this.getEntities(type);
		HashMap<String, Object> entity = null;
		for (Map<String, Object> data : list) {
			if (id.equals(data.get(key))) {
				entity = (HashMap<String, Object>) ((HashMap<String, Object>) data).clone();
				break;
			}
		}
		return entity;
	}
	
	private List<Map<String, Object>> createEntities(String link, String type) {
		// Get JSON content from web page
        StringBuilder out = getContentFromWebPage(link);

		// Create the entities
	    List<Map<String, Object>> entities = null;
	    JSONParser parser = new JSONParser();
	    JSONObject jsonObj = null;

		try {
			jsonObj = (JSONObject) parser.parse(out.toString());
			
			if (TYPE_PATIENT.equalsIgnoreCase(type)) {
				entities = createEntitiesPatients(jsonObj);
		    } else if (TYPE_OBSERVATION.equalsIgnoreCase(type)) {
		    	entities = createEntitiesObservations(jsonObj);
		    }
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		return entities;
	}
	
	
	@SuppressWarnings("unchecked")
	private List<Map<String, Object>> createEntitiesPatients(JSONObject jsonObj) {
	    List<Map<String, Object>> entities = new ArrayList<Map<String, Object>>();
	    
		JSONArray patientList = (JSONArray) jsonObj.get("entry");
		Iterator<JSONObject> iter = patientList.iterator();
		while (iter.hasNext()) {
			JSONObject patient = iter.next();
			String id = getLastToken(patient, "id", "/");
			String title = (String) patient.get("title");
			String link = getValue((JSONArray) patient.get("link"), "rel", "self", "href");
			String updated = (String) patient.get("updated");
			String author = getValue((JSONArray) patient.get("author"), "name", null, null);
			
			JSONObject content = (JSONObject) patient.get("content");
			String resourceType = (String) content.get("resourceType");
//			String text = ((JSONObject) content.get("text")).get("div").toString();
			String ssn = getValue((JSONArray) content.get("identifier"), "label", "SSN", "value");
			String name = getFullname((JSONArray) content.get("name"));
			String phone = getValue((JSONArray) content.get("telecom"), "system", "phone", "value");
			String gender = (String) content.get("gender");
			String birthDate = (String) content.get("birthDate");
			String addressHome = getAddress((JSONArray) content.get("address"), "home");
			String managingOrganization = (String) ((JSONObject) content.get("managingOrganization")).get("reference");
			Boolean active = (Boolean) content.get("active");
			
//			String summary = patient.get("summary").toString();
			
			HashMap<String, Object> entity = new HashMap<String, Object>();
			entity.put("id", id);
			entity.put("title", title);
			entity.put("link", link);
			entity.put("updated", updated);
			entity.put("author", author);
			entity.put("resourceType", resourceType);
//			entity.put("text", text);
			entity.put("ssn", ssn);
			entity.put("name", name);
			entity.put("phone", phone);
			entity.put("gender", gender);
			entity.put("birthDate", birthDate);
			entity.put("addressHome", addressHome);
			entity.put("managingOrganization", managingOrganization);
			entity.put("active", active);
//			entity.put("summary", summary);
			entities.add(entity);
		}
		return entities;
	}

	@SuppressWarnings("unchecked")
	private List<Map<String, Object>> createEntitiesObservations(JSONObject jsonObj) {
		List<Map<String, Object>> entities = new ArrayList<Map<String, Object>>();
		
		String patientRef = getLastToken((JSONObject) jsonObj.get("subject"), "reference", "/");
		
		JSONArray observationList = (JSONArray) jsonObj.get("contained");
		Iterator<JSONObject> iter = observationList.iterator();
		while (iter.hasNext()) {
			JSONObject observation = iter.next();
			String resourceType = (String) observation.get("resourceType");
			
			if ("Observation".equals(resourceType)) {
				String id = (String) observation.get("id");
				JSONObject observationName=(JSONObject) observation.get("name");
				JSONArray codingArray = (JSONArray)observationName.get("coding");
				String observationType = getValue(codingArray, "display", null, null);
				JSONObject observationValueQty=(JSONObject) observation.get("valueQuantity");
				Long observationValue =(Long) observationValueQty.get("value");
				String observationUnit = (String) observationValueQty.get("units");
				
				HashMap<String, Object> entity = new HashMap<String, Object>();
				entity.put("id", id);
				entity.put("observationType", observationType);
				entity.put("observationValue", observationValue);
				entity.put("observationUnit", observationUnit);
				entity.put("patientRef", patientRef);
				entities.add(entity);
			}
		}
		
		return entities;
	}

	@SuppressWarnings("unchecked")
	private String getAddress(JSONArray jsonArray, String type) {
		if (type == null || type.trim().length() <= 0) {
			return null;
		}
		
		String address = null;
		Iterator<JSONObject> iter = jsonArray.iterator();
		while (iter.hasNext()) {
			JSONObject jsonObj = iter.next();
			String data = (String) jsonObj.get("use");
			if (type.equals(data)) {
				JSONArray lines = (JSONArray) jsonObj.get("line");
				if (lines.size() > 0) {
					address = (String) lines.get(0);
				}
				break;
			}
		}
		return address;
	}

	@SuppressWarnings("unchecked")
	private String getValue(JSONArray jsonArray, String label1, String key, String label2) {
		String value = null;
		
		if (label2 != null) {
			if (key == null || key.trim().length() <= 0) {
				return null;
			}
			//*********************************************************
			// For example:
			//	"link": [{"rel": "self","href": "http://www.hl7.org/fhir/patient-examples.xml"}]
			// --------------------------------------------------------
			//	jsonArray=link, label1=rel, key=self, lable2=href
			//*********************************************************
			Iterator<JSONObject> iter = jsonArray.iterator();
			while (iter.hasNext()) {
				JSONObject jsonObj = iter.next();
				String data = (String) jsonObj.get(label1);
				if (key.equals(data)) {
					value = (String) jsonObj.get(label2);
					break;
				}
			}
		} else {
			//*********************************************************
			// For example:
			//	"author": [{"name": "Grahame Grieve / HL7 publishing committee"}]
			// --------------------------------------------------------
			//	jsonArray=author, label1=name, key=null, lable2=null
			//*********************************************************
			if (jsonArray.size() > 0) {
				value = (String) ((JSONObject) jsonArray.get(0)).get(label1);
			}
		}
		
		return value;
	}

	private StringBuilder getContentFromWebPage(String link) {
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
	        reader.close();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return out;
	}

	private String getLastToken(JSONObject patient, String label, String delimitor) {
		String idLink = (String) patient.get(label);
		String[] tokens = idLink.split(delimitor);
		return tokens[tokens.length-1];
	}

	@SuppressWarnings("unchecked")
	private String getFullname(JSONArray namesJson) {
		Iterator<JSONObject> iter = namesJson.iterator();
		String lastN = null;
		String firstN = null;
		while (iter.hasNext()) {
			JSONObject nameJson = iter.next();
			String use = (String) nameJson.get("use");
			if ("official".equalsIgnoreCase(use)) {
				JSONArray lastNames = (JSONArray) nameJson.get("family");
				JSONArray firstNames = (JSONArray) nameJson.get("given");
				lastN = (String) lastNames.get(0);
				firstN = (String) firstNames.get(0);
				break;
			}
		}
		if (lastN == null || firstN == null) {
			JSONObject nameJson = (JSONObject) namesJson.get(0);
			JSONArray lastNames = (JSONArray) nameJson.get("family");
			JSONArray firstNames = (JSONArray) nameJson.get("given");
			lastN = (String) lastNames.get(0);
			firstN = (String) firstNames.get(0);
		}
		return lastN + ", " + firstN;
	}
	
	public List<HashMap<String, Object>> getObservationsFor(String patientId) {
	    List<Map<String, Object>> observations = getEntities(TYPE_OBSERVATION);
	    List<HashMap<String, Object>> observationsForPatient = new ArrayList<HashMap<String, Object>>();
	    
	    for (Map<String, Object> observation : observations) {
	    	if (observation.get("patientRef").equals(patientId)) {
	    		observationsForPatient.add((HashMap<String, Object>) observation);
	    	}
	    }
	    
		return observationsForPatient;
	}

	public HashMap<String, Object> getPatientFor(String observationId) {
		Map<String, Object> observation = getEntity(observationId, TYPE_OBSERVATION, "id");
		if (observation != null) {
			Object patientId = observation.get("patientRef");
			if (patientId != null) {
				return getEntity((String)patientId, TYPE_PATIENT, "id");
			}
		}
		return null;
	}
	
	/*public List<HashMap<String, Object>> getScalarsFor(String observationId) {
	    List<Map<String, Object>> scalars = getEntities(TYPE_SCALAR);
	    List<HashMap<String, Object>> scalarsForObservation = new ArrayList<HashMap<String, Object>>();
	    
	    for (Map<String, Object> scalar : scalars) {
	    	if (scalar.get("OBSERVATION__C").equals(observationId)) {
	    		scalarsForObservation.add((HashMap<String, Object>) scalar);
	    	}
	    }
	    
		return scalarsForObservation;
	}*/

}
