package com.philips.olingo.fhir.service;

import static com.philips.olingo.fhir.service.DataStore.TYPE_OBSERVATION;
import static com.philips.olingo.fhir.service.DataStore.TYPE_PATIENT;
import static com.philips.olingo.fhir.service.DataStore.TYPE_SCALAR;
import static com.philips.olingo.fhir.service.MyEdmProvider.ENTITY_SET_NAME_OBSERVATIONS;
import static com.philips.olingo.fhir.service.MyEdmProvider.ENTITY_SET_NAME_PATIENTS;
import static com.philips.olingo.fhir.service.MyEdmProvider.ENTITY_SET_NAME_SCALARS;

import java.net.URI;
import java.util.Map;

import org.apache.olingo.odata2.api.edm.EdmEntitySet;
import org.apache.olingo.odata2.api.edm.EdmLiteralKind;
import org.apache.olingo.odata2.api.edm.EdmProperty;
import org.apache.olingo.odata2.api.edm.EdmSimpleType;
import org.apache.olingo.odata2.api.ep.EntityProvider;
import org.apache.olingo.odata2.api.ep.EntityProviderWriteProperties;
import org.apache.olingo.odata2.api.ep.EntityProviderWriteProperties.ODataEntityProviderPropertiesBuilder;
import org.apache.olingo.odata2.api.exception.ODataException;
import org.apache.olingo.odata2.api.exception.ODataNotFoundException;
import org.apache.olingo.odata2.api.exception.ODataNotImplementedException;
import org.apache.olingo.odata2.api.processor.ODataResponse;
import org.apache.olingo.odata2.api.processor.ODataSingleProcessor;
import org.apache.olingo.odata2.api.uri.KeyPredicate;
import org.apache.olingo.odata2.api.uri.info.GetEntitySetUriInfo;
import org.apache.olingo.odata2.api.uri.info.GetEntityUriInfo;

public class MyODataSingleProcessor extends ODataSingleProcessor {

	private final DataStore dataStore;

	public MyODataSingleProcessor() {
		dataStore = new DataStore();
	}

	@Override
	public ODataResponse readEntitySet(GetEntitySetUriInfo uriInfo, String contentType) throws ODataException {

		EdmEntitySet entitySet;

		if (uriInfo.getNavigationSegments().size() == 0) {
			entitySet = uriInfo.getStartEntitySet();

			if (ENTITY_SET_NAME_PATIENTS.equals(entitySet.getName())) {
				return EntityProvider.writeFeed(
						contentType, entitySet, dataStore.getEntities(TYPE_PATIENT), 
						EntityProviderWriteProperties.serviceRoot(getContext().getPathInfo().getServiceRoot()).build());
			} else if (ENTITY_SET_NAME_OBSERVATIONS.equals(entitySet.getName())) {
				return EntityProvider.writeFeed(
						contentType, entitySet, dataStore.getEntities(TYPE_OBSERVATION), 
						EntityProviderWriteProperties.serviceRoot(getContext().getPathInfo().getServiceRoot()).build());
			} else if (ENTITY_SET_NAME_SCALARS.equals(entitySet.getName())) {
				return EntityProvider.writeFeed(
						contentType, entitySet, dataStore.getEntities(TYPE_SCALAR), 
						EntityProviderWriteProperties.serviceRoot(getContext().getPathInfo().getServiceRoot()).build());
			}

			throw new ODataNotFoundException(ODataNotFoundException.ENTITY);

		} else if (uriInfo.getNavigationSegments().size() == 1) {
			//navigation first level, simplified example for illustration purposes only
			/*entitySet = uriInfo.getTargetEntitySet();

			if (ENTITY_SET_NAME_OBSERVATIONS.equals(entitySet.getName())) {
				String patientKey = getKeyValue(uriInfo.getKeyPredicates().get(0));

				List<Map<String, Object>> observations = new ArrayList<Map<String, Object>>();
				observations.addAll(dataStore.getObservationsFor(patientKey));

				return EntityProvider.writeFeed(contentType, entitySet, observations, EntityProviderWriteProperties.serviceRoot(getContext().getPathInfo().getServiceRoot()).build());
			} else if (ENTITY_SET_NAME_SCALARS.equals(entitySet.getName())) {
				String observationKey = getKeyValue(uriInfo.getKeyPredicates().get(0));

				List<Map<String, Object>> scalars = new ArrayList<Map<String, Object>>();
				scalars.addAll(dataStore.getScalarsFor(observationKey));

				return EntityProvider.writeFeed(contentType, entitySet, scalars, EntityProviderWriteProperties.serviceRoot(getContext().getPathInfo().getServiceRoot()).build());
			}

			throw new ODataNotFoundException(ODataNotFoundException.ENTITY);*/
		}

		throw new ODataNotImplementedException();
	}

	@Override
	public ODataResponse readEntity(GetEntityUriInfo uriInfo, String contentType) throws ODataException {

		if (uriInfo.getNavigationSegments().size() == 0) {
			EdmEntitySet entitySet = uriInfo.getStartEntitySet();

			if (ENTITY_SET_NAME_PATIENTS.equals(entitySet.getName())) {
				String id = getKeyValue(uriInfo.getKeyPredicates().get(0));
				Map<String, Object> data = dataStore.getEntity(id, TYPE_PATIENT, "id");

				if (data != null) {
					URI serviceRoot = getContext().getPathInfo().getServiceRoot();
					ODataEntityProviderPropertiesBuilder propertiesBuilder = EntityProviderWriteProperties.serviceRoot(serviceRoot);

					return EntityProvider.writeEntry(contentType, entitySet, data, propertiesBuilder.build());
				}
			} else if (ENTITY_SET_NAME_OBSERVATIONS.equals(entitySet.getName())) {
				String id = getKeyValue(uriInfo.getKeyPredicates().get(0));
				Map<String, Object> data = dataStore.getEntity(id, TYPE_OBSERVATION, "id");

				if (data != null) {
					URI serviceRoot = getContext().getPathInfo().getServiceRoot();
					ODataEntityProviderPropertiesBuilder propertiesBuilder = EntityProviderWriteProperties.serviceRoot(serviceRoot);

					return EntityProvider.writeEntry(contentType, entitySet, data, propertiesBuilder.build());
				}
			} else if (ENTITY_SET_NAME_SCALARS.equals(entitySet.getName())) {
				String id = getKeyValue(uriInfo.getKeyPredicates().get(0));
				Map<String, Object> data = dataStore.getEntity(id, TYPE_SCALAR, "id");

				if (data != null) {
					URI serviceRoot = getContext().getPathInfo().getServiceRoot();
					ODataEntityProviderPropertiesBuilder propertiesBuilder = EntityProviderWriteProperties.serviceRoot(serviceRoot);

					return EntityProvider.writeEntry(contentType, entitySet, data, propertiesBuilder.build());
				}
			}

			throw new ODataNotFoundException(ODataNotFoundException.ENTITY);

		} else if (uriInfo.getNavigationSegments().size() == 1) {
			//navigation first level, simplified example for illustration purposes only
			/*EdmEntitySet entitySet = uriInfo.getTargetEntitySet();

			Map<String, Object> data = null;

			if (ENTITY_SET_NAME_PATIENTS.equals(entitySet.getName())) {
				String observationKey = getKeyValue(uriInfo.getKeyPredicates().get(0));
				data = dataStore.getPatientFor(observationKey);
			}

			if(data != null) {
				return EntityProvider.writeEntry(contentType, uriInfo.getTargetEntitySet(), 
						data, EntityProviderWriteProperties.serviceRoot(getContext().getPathInfo().getServiceRoot()).build());
			}

			throw new ODataNotFoundException(ODataNotFoundException.ENTITY);*/
		}

		throw new ODataNotImplementedException();
	}

	private String getKeyValue(KeyPredicate key) throws ODataException {
		EdmProperty property = key.getProperty();
		EdmSimpleType type = (EdmSimpleType) property.getType();
		return type.valueOfString(key.getLiteral(), EdmLiteralKind.DEFAULT, property.getFacets(), String.class);
	}

}
