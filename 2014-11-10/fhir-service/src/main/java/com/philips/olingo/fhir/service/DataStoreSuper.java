package com.philips.olingo.fhir.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.philips.olingo.fhir.common.Constants;

public abstract class DataStoreSuper implements Constants {

	public List<Map<String, Object>> getEntities(String type) {
		return null;
	}
	
	public HashMap<String, Object> getEntity(String id, String type, String key) {
		return null;
	}
	
}
