package com.philips.olingo.fhir.service;

import java.util.ArrayList;
import java.util.List;

import org.apache.olingo.odata2.api.edm.EdmMultiplicity;
import org.apache.olingo.odata2.api.edm.EdmSimpleTypeKind;
import org.apache.olingo.odata2.api.edm.EdmTargetPath;
import org.apache.olingo.odata2.api.edm.FullQualifiedName;
import org.apache.olingo.odata2.api.edm.provider.Association;
import org.apache.olingo.odata2.api.edm.provider.AssociationEnd;
import org.apache.olingo.odata2.api.edm.provider.AssociationSet;
import org.apache.olingo.odata2.api.edm.provider.AssociationSetEnd;
import org.apache.olingo.odata2.api.edm.provider.CustomizableFeedMappings;
import org.apache.olingo.odata2.api.edm.provider.EdmProvider;
import org.apache.olingo.odata2.api.edm.provider.EntityContainer;
import org.apache.olingo.odata2.api.edm.provider.EntityContainerInfo;
import org.apache.olingo.odata2.api.edm.provider.EntitySet;
import org.apache.olingo.odata2.api.edm.provider.EntityType;
import org.apache.olingo.odata2.api.edm.provider.Facets;
import org.apache.olingo.odata2.api.edm.provider.FunctionImport;
import org.apache.olingo.odata2.api.edm.provider.Key;
import org.apache.olingo.odata2.api.edm.provider.NavigationProperty;
import org.apache.olingo.odata2.api.edm.provider.Property;
import org.apache.olingo.odata2.api.edm.provider.PropertyRef;
import org.apache.olingo.odata2.api.edm.provider.ReturnType;
import org.apache.olingo.odata2.api.edm.provider.Schema;
import org.apache.olingo.odata2.api.edm.provider.SimpleProperty;
import org.apache.olingo.odata2.api.exception.ODataException;

import com.philips.olingo.fhir.common.Constants;

public class MyEdmProvider extends EdmProvider implements Constants {

	@Override
	public List<Schema> getSchemas() throws ODataException {
		List<Schema> schemas = new ArrayList<Schema>();

		Schema schema = new Schema();
		schema.setNamespace(NAMESPACE);

		List<EntityType> entityTypes = new ArrayList<EntityType>();
		entityTypes.add(getEntityType(ENTITY_TYPE_P));
		entityTypes.add(getEntityType(ENTITY_TYPE_OB));
//		entityTypes.add(getEntityType(ENTITY_TYPE_S));
		schema.setEntityTypes(entityTypes);

		List<Association> associations = new ArrayList<Association>();
//		associations.add(getAssociation(ASSOCIATION_OB_PATIENT));
//		associations.add(getAssociation(ASSOCIATION_SCALAR_OB));
		schema.setAssociations(associations);

		List<EntityContainer> entityContainers = new ArrayList<EntityContainer>();
		EntityContainer entityContainer = new EntityContainer();
		entityContainer.setName(ENTITY_CONTAINER).setDefaultEntityContainer(true);

		List<EntitySet> entitySets = new ArrayList<EntitySet>();
		entitySets.add(getEntitySet(ENTITY_CONTAINER, ENTITY_SET_NAME_PATIENTS));
		entitySets.add(getEntitySet(ENTITY_CONTAINER, ENTITY_SET_NAME_OBSERVATIONS));
//		entitySets.add(getEntitySet(ENTITY_CONTAINER, ENTITY_SET_NAME_SCALARS));
		entityContainer.setEntitySets(entitySets);

		List<AssociationSet> associationSets = new ArrayList<AssociationSet>();
//		associationSets.add(getAssociationSet(ENTITY_CONTAINER, ASSOCIATION_OB_PATIENT, ENTITY_SET_NAME_PATIENTS, ROLE_P_OBS));
//		associationSets.add(getAssociationSet(ENTITY_CONTAINER, ASSOCIATION_SCALAR_OB, ENTITY_SET_NAME_OBSERVATIONS, ROLE_OB_SCALARS));
		entityContainer.setAssociationSets(associationSets);

		List<FunctionImport> functionImports = new ArrayList<FunctionImport>();
//		functionImports.add(getFunctionImport(ENTITY_CONTAINER, FUNCTION_IMPORT_OBSERVATION));
//		functionImports.add(getFunctionImport(ENTITY_CONTAINER, FUNCTION_IMPORT_SCALAR));
		entityContainer.setFunctionImports(functionImports);

		entityContainers.add(entityContainer);
		schema.setEntityContainers(entityContainers);

		schemas.add(schema);

		return schemas;
	}

	@Override
	public EntityType getEntityType(FullQualifiedName edmFQName) throws ODataException {
		if (NAMESPACE.equals(edmFQName.getNamespace())) {
			 if (ENTITY_TYPE_P.getName().equals(edmFQName.getName())) {
				//Properties
				List<Property> properties = new ArrayList<Property>();
				properties.add(new SimpleProperty().setName("id").setType(EdmSimpleTypeKind.String)
						.setFacets(new Facets().setNullable(false).setMaxLength(80)));
				properties.add(new SimpleProperty().setName("name").setType(EdmSimpleTypeKind.String)
						.setFacets(new Facets().setNullable(false).setMaxLength(80)));
				properties.add(new SimpleProperty().setName("title").setType(EdmSimpleTypeKind.String)
						.setFacets(new Facets().setNullable(true).setMaxLength(80)));
				properties.add(new SimpleProperty().setName("updated").setType(EdmSimpleTypeKind.String)
						.setFacets(new Facets().setNullable(false).setMaxLength(80)));
				properties.add(new SimpleProperty().setName("link").setType(EdmSimpleTypeKind.String)
						.setFacets(new Facets().setNullable(true).setMaxLength(80)));
				properties.add(new SimpleProperty().setName("author").setType(EdmSimpleTypeKind.String)
						.setFacets(new Facets().setNullable(true).setMaxLength(80)));
				properties.add(new SimpleProperty().setName("resourceType").setType(EdmSimpleTypeKind.String)
						.setFacets(new Facets().setNullable(true).setMaxLength(80)));
//				properties.add(new SimpleProperty().setName("text").setType(EdmSimpleTypeKind.String)
//						.setFacets(new Facets().setNullable(true).setMaxLength(80)));
				properties.add(new SimpleProperty().setName("ssn").setType(EdmSimpleTypeKind.String)
						.setFacets(new Facets().setNullable(true).setMaxLength(10)));
				properties.add(new SimpleProperty().setName("phone").setType(EdmSimpleTypeKind.String)
						.setFacets(new Facets().setNullable(true).setMaxLength(80)));
				properties.add(new SimpleProperty().setName("gender").setType(EdmSimpleTypeKind.String)
						.setFacets(new Facets().setNullable(true).setMaxLength(8)));
				properties.add(new SimpleProperty().setName("addressHome").setType(EdmSimpleTypeKind.String)
						.setFacets(new Facets().setNullable(true).setMaxLength(80)));
				properties.add(new SimpleProperty().setName("managingOrganization").setType(EdmSimpleTypeKind.String)
						.setFacets(new Facets().setNullable(true).setMaxLength(80)));
				properties.add(new SimpleProperty().setName("active").setType(EdmSimpleTypeKind.Boolean)
						.setFacets(new Facets().setNullable(true)));
//				properties.add(new SimpleProperty().setName("summary").setType(EdmSimpleTypeKind.String)
//						.setFacets(new Facets().setNullable(true).setMaxLength(80)));

//				//Navigation Properties
//				List<NavigationProperty> navigationProperties = new ArrayList<NavigationProperty>();
//				navigationProperties.add(new NavigationProperty().setName("Observations")
//						.setRelationship(ASSOCIATION_OB_PATIENT).setFromRole(ROLE_P_OBS).setToRole(ROLE_OB_P));

				//Key
				List<PropertyRef> keyProperties = new ArrayList<PropertyRef>();
				keyProperties.add(new PropertyRef().setName("id"));
				Key key = new Key().setKeys(keyProperties);

				return new EntityType().setName(ENTITY_TYPE_P.getName())
						.setProperties(properties)
						.setKey(key)
//						.setNavigationProperties(navigationProperties)
				;
			} else if (ENTITY_TYPE_OB.getName().equals(edmFQName.getName())) {
				List<Property> properties = new ArrayList<Property>();
				properties.add(new SimpleProperty().setName("id").setType(EdmSimpleTypeKind.String)
						.setFacets(new Facets().setNullable(false).setMaxLength(80)));
				properties.add(new SimpleProperty().setName("observationType").setType(EdmSimpleTypeKind.String)
						.setFacets(new Facets().setNullable(true).setMaxLength(80)));
				properties.add(new SimpleProperty().setName("observationValue").setType(EdmSimpleTypeKind.Double)
						.setFacets(new Facets().setNullable(true)));
				properties.add(new SimpleProperty().setName("observationUnit").setType(EdmSimpleTypeKind.String)
						.setFacets(new Facets().setNullable(true).setMaxLength(80)));

//				//Navigation Properties
//				List<NavigationProperty> navigationProperties = new ArrayList<NavigationProperty>();
//				navigationProperties.add(new NavigationProperty().setName("Observations")
//						.setRelationship(ASSOCIATION_OB_PATIENT).setFromRole(ROLE_P_OBS).setToRole(ROLE_OB_P));

				//Key
				List<PropertyRef> keyProperties = new ArrayList<PropertyRef>();
				keyProperties.add(new PropertyRef().setName("id"));
				Key key = new Key().setKeys(keyProperties);

				return new EntityType().setName(ENTITY_TYPE_OB.getName())
						.setProperties(properties)
						.setKey(key)
//						.setNavigationProperties(navigationProperties)
				;
			} else if (ENTITY_TYPE_S.getName().equals(edmFQName.getName())) {
//				//Properties
//				List<Property> properties = new ArrayList<Property>();
//				properties.add(new SimpleProperty().setName("Id".toUpperCase()).setType(EdmSimpleTypeKind.String)
//					.setFacets(new Facets().setNullable(false).setMaxLength(18)));
//
//				//Navigation Properties
//				List<NavigationProperty> navigationProperties = new ArrayList<NavigationProperty>();
//				navigationProperties.add(new NavigationProperty().setName("Observation")
//					.setRelationship(ASSOCIATION_SCALAR_OB).setFromRole(ROLE_SCALAR_OB).setToRole(ROLE_OB_SCALARS));
//
//				//Key
//				List<PropertyRef> keyProperties = new ArrayList<PropertyRef>();
//				keyProperties.add(new PropertyRef().setName("Id".toUpperCase()));
//				Key key = new Key().setKeys(keyProperties);
//
//				return new EntityType().setName(ENTITY_TYPE_S.getName())
//						.setProperties(properties)
//						.setKey(key)
//						.setNavigationProperties(navigationProperties);
			}
		}

		return null;
	}

	@Override
	public Association getAssociation(FullQualifiedName edmFQName) throws ODataException {
		/*if (NAMESPACE.equals(edmFQName.getNamespace())) {
			if (ASSOCIATION_OB_PATIENT.getName().equals(edmFQName.getName())) {
				return new Association().setName(ASSOCIATION_OB_PATIENT.getName())
						.setEnd1(new AssociationEnd().setType(ENTITY_TYPE_OB).setRole(ROLE_OB_P).setMultiplicity(EdmMultiplicity.MANY))
						.setEnd2(new AssociationEnd().setType(ENTITY_TYPE_P).setRole(ROLE_P_OBS).setMultiplicity(EdmMultiplicity.ONE));
			} else if (ASSOCIATION_SCALAR_OB.getName().equals(edmFQName.getName())) {
				return new Association().setName(ASSOCIATION_SCALAR_OB.getName())
						.setEnd1(new AssociationEnd().setType(ENTITY_TYPE_S).setRole(ROLE_SCALAR_OB).setMultiplicity(EdmMultiplicity.MANY))
						.setEnd2(new AssociationEnd().setType(ENTITY_TYPE_OB).setRole(ROLE_OB_SCALARS).setMultiplicity(EdmMultiplicity.ONE));
			}
		}*/
		return null;
	}

	@Override
	public EntitySet getEntitySet(String entityContainer, String name) throws ODataException {
		if (ENTITY_CONTAINER.equals(entityContainer)) {
			if (ENTITY_SET_NAME_OBSERVATIONS.equals(name)) {
				return new EntitySet().setName(name).setEntityType(ENTITY_TYPE_OB);
			} else if (ENTITY_SET_NAME_PATIENTS.equals(name)) {
				return new EntitySet().setName(name).setEntityType(ENTITY_TYPE_P);
			} else if (ENTITY_SET_NAME_SCALARS.equals(name)) {
				return new EntitySet().setName(name).setEntityType(ENTITY_TYPE_S);
			}
		}
		return null;
	}

	@Override
	public AssociationSet getAssociationSet(String entityContainer, FullQualifiedName association
			, String sourceEntitySetName, String sourceEntitySetRole) throws ODataException {
		/*if (ENTITY_CONTAINER.equals(entityContainer)) {
			if (ASSOCIATION_OB_PATIENT.equals(association)) {
				return new AssociationSet().setName(ASSOCIATION_SET_OB_P)
						.setAssociation(ASSOCIATION_OB_PATIENT)
						.setEnd1(new AssociationSetEnd().setRole(ROLE_P_OBS).setEntitySet(ENTITY_SET_NAME_PATIENTS))
						.setEnd2(new AssociationSetEnd().setRole(ROLE_OB_P).setEntitySet(ENTITY_SET_NAME_OBSERVATIONS));
			} else if (ASSOCIATION_SCALAR_OB.equals(association)) {
				return new AssociationSet().setName(ASSOCIATION_SET_SCALAR_OB)
						.setAssociation(ASSOCIATION_SCALAR_OB)
						.setEnd1(new AssociationSetEnd().setRole(ROLE_OB_SCALARS).setEntitySet(ENTITY_SET_NAME_OBSERVATIONS))
						.setEnd2(new AssociationSetEnd().setRole(ROLE_SCALAR_OB).setEntitySet(ENTITY_SET_NAME_SCALARS));
			}
		}*/
		return null;
	}

	@Override
	public FunctionImport getFunctionImport(String entityContainer, String name) throws ODataException {
		/*if (ENTITY_CONTAINER.equals(entityContainer)) {
			if (FUNCTION_IMPORT_OBSERVATION.equals(name)) {
				return new FunctionImport().setName(name)
						.setReturnType(new ReturnType().setTypeName(ENTITY_TYPE_OB).setMultiplicity(EdmMultiplicity.MANY))
						.setHttpMethod("GET");
			} else if (FUNCTION_IMPORT_SCALAR.equals(name)) {
				return new FunctionImport().setName(name)
						.setReturnType(new ReturnType().setTypeName(ENTITY_TYPE_S).setMultiplicity(EdmMultiplicity.MANY))
						.setHttpMethod("GET");
			}
		}*/
		return null;
	}

	@Override
	public EntityContainerInfo getEntityContainerInfo(String name) throws ODataException {
		if (name == null || ENTITY_CONTAINER.equals(name)) {
			return new EntityContainerInfo().setName(ENTITY_CONTAINER).setDefaultEntityContainer(true);
		}

		return null;
	}

}
