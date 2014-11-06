package com.philips.olingo.fhir.common;

import org.apache.olingo.odata2.api.edm.FullQualifiedName;

public interface Constants {
	// Entity Set Name
	public static final String ENTITY_SET_NAME_PATIENTS = "Patients";
	public static final String ENTITY_SET_NAME_OBSERVATIONS = "Observations";
	public static final String ENTITY_SET_NAME_SCALARS = "Scalars";
	
	// Entity Name
	public static final String ENTITY_NAME_PATIENT = "Patient";
	public static final String ENTITY_NAME_OBSERVATION = "Observation";
	public static final String ENTITY_NAME_SCALAR = "Scalar";

	// Data Namespace
	public static final String NAMESPACE = "com.philips.odata2.ODataDhp";

	// Entity Type
	public static final FullQualifiedName ENTITY_TYPE_OB = new FullQualifiedName(NAMESPACE, ENTITY_NAME_OBSERVATION);
	public static final FullQualifiedName ENTITY_TYPE_P = new FullQualifiedName(NAMESPACE, ENTITY_NAME_PATIENT);
	public static final FullQualifiedName ENTITY_TYPE_S = new FullQualifiedName(NAMESPACE, ENTITY_NAME_SCALAR);

	// Association
	public static final FullQualifiedName ASSOCIATION_OB_PATIENT = new FullQualifiedName(NAMESPACE, "Ob_Patient_Patient_Obs");
	public static final FullQualifiedName ASSOCIATION_SCALAR_OB = new FullQualifiedName(NAMESPACE, "Scalar_Ob_Ob_Scalars");

	// Role
	public static final String ROLE_OB_P = "Observation_Patient";
	public static final String ROLE_P_OBS = "Patient_Observations";
	public static final String ROLE_SCALAR_OB = "Scalar_Observation";
	public static final String ROLE_OB_SCALARS = "Observation_Scalars";

	// Container
	public static final String ENTITY_CONTAINER = "ODataDhpEntityContainer";

	// Association Set
	public static final String ASSOCIATION_SET_OB_P = "Observations_Patients";
	public static final String ASSOCIATION_SET_SCALAR_OB = "Scalars_Observations";

	// Function Import
	public static final String FUNCTION_IMPORT_OBSERVATION = "NumberOfObservations";
	public static final String FUNCTION_IMPORT_SCALAR = "NumberOfScalars";

	// Type
	static final String TYPE_PATIENT = "patient";
	static final String TYPE_OBSERVATION = "observation";
	static final String TYPE_SCALAR = "scalar";
	
	// URL
	static final String URL_PATIENT = "http://www.hl7.org/implement/standards/FHIR-Develop/patient-examples.json";
	static final String URL_OBSERVATION = "http://www.hl7.org/implement/standards/FHIR-Develop/deviceobservationreport-example.json";
	static final String URL_SCALAR = "";

}
