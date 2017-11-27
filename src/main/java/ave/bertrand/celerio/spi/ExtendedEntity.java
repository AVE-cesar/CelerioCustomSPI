package ave.bertrand.celerio.spi;

import com.jaxio.celerio.model.Attribute;
import com.jaxio.celerio.model.Entity;
import com.jaxio.celerio.model.Relation;
import com.jaxio.celerio.spi.EntitySpi;

/**
 * Adds some extra function to Celerio.
 * 
 * do a: maven clean install
 * 
 * @author avebertrand
 *
 */
public class ExtendedEntity implements EntitySpi {

	private static final String COMMA_SEPARATOR = ", ";
	private static final String CURLY_BRACKET_BEGIN = "{";
	private static final String CURLY_BRACKET_END = "}";
	private static final String QUOTATION_MARKS = "\\\"";
	private static final String DOLLAR = "$";
	private static final String COLON = ":";
	private static final String COLON_SPACE = ": ";
	private static final String SPACE = " ";
	private static final String REST_PATH_VARIABLE = "@PathVariable";
	
		
	private Entity entity;

	public void init(Entity entity) {
		this.entity = entity;
	}

	public Object getTarget() {
		return this;
	}

	public String velocityVar() {
		return "extended";
	}

	/**
	 * Returns a list (comma separated) of all entity's simple attributes + FK
	 * attributes
	 * 
	 * @return String the list
	 */
	public String getAttributesList() {
		StringBuffer buffer = new StringBuffer();

		for (int i = 0; i < entity.getNonCpkAttributes().getList().size(); i++) {
			Attribute attribute = entity.getNonCpkAttributes().getList().get(i);

			if (!attribute.isInFk()) {
				buffer.append("this.").append(attribute.getName()).append(COMMA_SEPARATOR);
			}
		}
		
		for (int i = 0; i < entity.getManyToOne().getList().size(); i++) {
			Relation relation = entity.getManyToOne().getList().get(i);
			
			// FIXME .getId SHOULD be generated dynamically
			buffer.append("this.").append(relation.getTo().getVar()).append(".getId()").append(COMMA_SEPARATOR);
		}
		
		// remove ending separator if it exits
		String result = buffer.toString();
		if (result.endsWith(COMMA_SEPARATOR)) {
			result = result.substring(0, result.length()-2);
		}
		
		return result;
	}

	/**
	 * Returns a list (comma separated + curly bracket) of all attributes of the entity's key or the default one {id}.
	 * sample (str1): {keyPart1},{keyPart2}
	 * @return the computed string
	 */
	public String getCpkAttributesListWithCommaAndCurlyBracket() {
		StringBuffer buffer = new StringBuffer();
		
		boolean bufferEmpty = true;
		for (int i = 0; i < entity.getAttributes().getList().size(); i++) {
			Attribute attribute = entity.getAttributes().getList().get(i);

			if (attribute.isInCpk()) {
				if (bufferEmpty) {
					buffer.append(CURLY_BRACKET_BEGIN).append(attribute.getName()).append(CURLY_BRACKET_END);
					bufferEmpty = false;
				} else {
					buffer.append(COMMA_SEPARATOR).append(CURLY_BRACKET_BEGIN).append(attribute.getName()).append(CURLY_BRACKET_END);
				}
			}
		}
		
		if (bufferEmpty) {
			// no CPK for this entity, we add the default one
			buffer.append(CURLY_BRACKET_BEGIN).append("id").append(CURLY_BRACKET_END);
		}
		
		return buffer.toString();
	}
	
	/**
	 * sample (str2): {keyPart1: $stateParams.keyPart1, keyPart2: $stateParams.keyPart2, ...}
	 * @return the computed string
	 */
	public String getCpkAttributesListJsonStyleStateParams() {
		StringBuffer buffer = new StringBuffer();
		
		boolean bufferEmpty = true;
		for (int i = 0; i < entity.getAttributes().getList().size(); i++) {
			Attribute attribute = entity.getAttributes().getList().get(i);

			if (attribute.isInCpk()) {
				if (bufferEmpty) {
					buffer.append(CURLY_BRACKET_BEGIN).append(attribute.getName()).append(COLON_SPACE).append(DOLLAR).append("stateParams.").append(attribute.getName());
					bufferEmpty = false;
				} else {
					buffer.append(COMMA_SEPARATOR).append(attribute.getName());
				}
			}
		}
		
		if (bufferEmpty) {
			// no CPK for this entity, we add the default one
			buffer.append(CURLY_BRACKET_BEGIN).append("id").append(COLON_SPACE).append(DOLLAR).append("stateParams.id").append(CURLY_BRACKET_END);
		} else {
			// don't forget the ending curly bracket
			buffer.append(CURLY_BRACKET_END);
		}
		
		return buffer.toString();
	}
	
	/**
	 * sample (str3): @PathVariable String keyPart1, @PathVariable String keyPart2
	 * @return the computed string
	 */
	public String getCpkAttributesListRestStyle() {
		StringBuffer buffer = new StringBuffer();
		
		boolean bufferEmpty = true;
		for (int i = 0; i < entity.getAttributes().getList().size(); i++) {
			Attribute attribute = entity.getAttributes().getList().get(i);

			if (attribute.isInCpk()) {
				if (bufferEmpty) {
					buffer.append(REST_PATH_VARIABLE).append(SPACE).append(attribute.getType()).append(SPACE).append(attribute.getName());
					bufferEmpty = false;
				} else {
					buffer.append(COMMA_SEPARATOR).append(REST_PATH_VARIABLE).append(SPACE).append(attribute.getType()).append(SPACE).append(attribute.getName());
				}
			}
		}
		
		return buffer.toString();
	}
	
	/**
	 * sample (str4): keyPart1, keyPart2
	 * @return the computed string
	 */
	public String getCpkAttributesListConstructorStyle() {
		StringBuffer buffer = new StringBuffer();
		
		boolean bufferEmpty = true;
		for (int i = 0; i < entity.getAttributes().getList().size(); i++) {
			Attribute attribute = entity.getAttributes().getList().get(i);

			if (attribute.isInCpk()) {
				if (bufferEmpty) {
					buffer.append(attribute.getVar());
					bufferEmpty = false;
				} else {
					buffer.append(COMMA_SEPARATOR).append(attribute.getVar());
				}
			}
		}
		
		return buffer.toString();
	}
	
	/**
	 * sample (str5.1): String keyPart1, String keyPart2
	 * @return the computed string
	 */
	public String getCpkAttributesListJavaStyle() {
		StringBuffer buffer = new StringBuffer();
		
		boolean bufferEmpty = true;
		for (int i = 0; i < entity.getAttributes().getList().size(); i++) {
			Attribute attribute = entity.getAttributes().getList().get(i);

			if (attribute.isInCpk()) {
				if (bufferEmpty) {
					buffer.append(attribute.getType()).append(SPACE).append(attribute.getName());
					bufferEmpty = false;
				} else {
					buffer.append(COMMA_SEPARATOR).append(attribute.getType()).append(SPACE).append(attribute.getName());
				}
			}
		}
		
		return buffer.toString();
	}
	
	/**
	 * sample (str5.2): String attribute1, String attribute2, ...
	 * @return the computed string
	 */
	public String getAttributesListJavaStyle() {
		StringBuffer buffer = new StringBuffer();
		
		// starts with simple attributes
		boolean bufferEmpty = true;
		for (int i = 0; i < entity.getAttributes().getList().size(); i++) {
			Attribute attribute = entity.getAttributes().getList().get(i);

			if (!attribute.isInFk()) {
				if (bufferEmpty) {
					buffer.append(attribute.getType()).append(SPACE).append(attribute.getName());
					bufferEmpty = false;
				} else {
					buffer.append(COMMA_SEPARATOR).append(attribute.getType()).append(SPACE).append(attribute.getName());
				}
			}
		}
		
		// adds Many to one relations
		for (int i = 0; i < entity.getManyToOne().getList().size(); i++) {
			Relation relation = entity.getManyToOne().getList().get(i);
			
			if (bufferEmpty) {
				buffer.append(relation.getTo().getType()).append(SPACE).append(relation.getTo().getVar());
				bufferEmpty = false;
			} else {
				buffer.append(COMMA_SEPARATOR).append(relation.getTo().getType()).append(SPACE).append(relation.getTo().getVar());
			}
		}
		
		return buffer.toString();
	}
	
	/**
	 * sample (str7): {{item.id.keyPart1}}:{{item.id.keyPart2}}
	 * @return the computed string
	 */
	public String getCpkAttributesListAngularJsStyle() {
		StringBuffer buffer = new StringBuffer();
		
		boolean bufferEmpty = true;
		for (int i = 0; i < entity.getAttributes().getList().size(); i++) {
			Attribute attribute = entity.getAttributes().getList().get(i);

			if (attribute.isInCpk()) {
				if (bufferEmpty) {
					buffer.append(CURLY_BRACKET_BEGIN).append(CURLY_BRACKET_BEGIN).append("item.id.").append(attribute.getName()).append(CURLY_BRACKET_END).append(CURLY_BRACKET_END);
					bufferEmpty = false;
				} else {
					buffer.append(COLON).append(CURLY_BRACKET_BEGIN).append(CURLY_BRACKET_BEGIN).append("item.id.").append(attribute.getName()).append(CURLY_BRACKET_END).append(CURLY_BRACKET_END);
				}
			}
		}
		
		return buffer.toString();
	}
	
	/**
	 * sample (str8): {keyPart1: item.id.keyPart1, keyPart2: item.id.keyPart2, ...}
	 * @return the computed string
	 */
	public String getCpkAttributesListJsonStyleItemId() {
		StringBuffer buffer = new StringBuffer();
		
		boolean bufferEmpty = true;
		for (int i = 0; i < entity.getAttributes().getList().size(); i++) {
			Attribute attribute = entity.getAttributes().getList().get(i);

			if (attribute.isInCpk()) {
				if (bufferEmpty) {
					buffer.append(CURLY_BRACKET_BEGIN).append(attribute.getName()).append(COLON_SPACE).append("item.id.").append(DOLLAR).append(attribute.getName());
					bufferEmpty = false;
				} else {
					buffer.append(COMMA_SEPARATOR).append(attribute.getName()).append(COLON_SPACE).append("item.id.").append(DOLLAR).append(attribute.getName());
				}
			}
		}
		
		if (!bufferEmpty) {
			// don't forget the ending curly bracket
			buffer.append(CURLY_BRACKET_END);
		}
		
		return buffer.toString();
	}
	
	/**
	 * Returns a list (JSon style) of all entity's attributes.
	 * Sample (str9): { "attrib1": true, "attrib2": true, ...}
	 * @return the computed string
	 */
	public String getAttributesListJsonStyle() {
		StringBuffer buffer = new StringBuffer();
		
		boolean bufferEmpty = true;
		for (int i = 0; i < entity.getAttributes().getList().size(); i++) {
			Attribute attribute = entity.getAttributes().getList().get(i);

			if (bufferEmpty) {
				buffer.append(CURLY_BRACKET_BEGIN).append(QUOTATION_MARKS).append(attribute.getName()).append(QUOTATION_MARKS)
					.append(": true");
				bufferEmpty = false;
			} else {
				buffer.append(COMMA_SEPARATOR)
					.append(QUOTATION_MARKS).append(attribute.getName()).append(QUOTATION_MARKS)
					.append(": true");
			}
		}
		
		if (!bufferEmpty) {
			// don't forget the ending curly bracket
			buffer.append(CURLY_BRACKET_END);
		}
		
		return buffer.toString();
	}
	
	/**
	 * Returns a list of all entity's attributes as a SQL comma separated list.
	 * Sample (str10): databaseColumn1, databaseColumn2, ...
	 * @return the computed string
	 */
	public String getAttributesListAsSqlColumn() {
		StringBuffer buffer = new StringBuffer();
		
		boolean bufferEmpty = true;
		for (int i = 0; i < entity.getAttributes().getList().size(); i++) {
			Attribute attribute = entity.getAttributes().getList().get(i);

			if (bufferEmpty) {
				buffer.append(attribute.getColumnName());
				bufferEmpty = false;
			} else {
				buffer.append(COMMA_SEPARATOR)
					.append(attribute.getColumnName());
			}
		}
		
		return buffer.toString();
	}
	
	/**
	 * Returns an entity key list for an AngularJs URL.
	 * Sample: :id (for a single key)	or 	:keyPart1,:keyPart2 (for a composite key)
	 * @return the computed string
	 */
	public String getCpkAttributesListForAngularUrl() {
		StringBuffer buffer = new StringBuffer();
		
		boolean bufferEmpty = true;
		for (int i = 0; i < entity.getAttributes().getList().size(); i++) {
			Attribute attribute = entity.getAttributes().getList().get(i);

			if (attribute.isInCpk()) {
				if (bufferEmpty) {
					buffer.append(COLON_SPACE).append(DOLLAR).append(attribute.getName());
					bufferEmpty = false;
				} else {
					buffer.append(COMMA_SEPARATOR).append(COLON_SPACE).append(DOLLAR).append(attribute.getName());
				}
			}
		}
		
		if (bufferEmpty) {
			// no CPK for this entity, we add the default one
			buffer.append(COLON).append("id");
		}
		
		return buffer.toString();
	}	
	
	/**
	 * Returns the first attribute name that is not part of the primary key.
	 *  
	 * @return the attribute name
	 */
	public String getFirstNoneKeyAttribute() {
		StringBuffer buffer = new StringBuffer();
		
		for (int i = 0; i < entity.getAttributes().getList().size(); i++) {
			Attribute attribute = entity.getAttributes().getList().get(i);
			
			if (!attribute.isInCpk() && !attribute.isInPk()) {
				buffer.append(attribute.getName());
				break;
			}
		}
		
		return buffer.toString();
	}
}
