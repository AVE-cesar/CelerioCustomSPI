package ave.bertrand.celerio.spi;

import static org.junit.Assert.*;

import org.junit.Test;

import com.jaxio.celerio.configuration.database.JdbcType;
import com.jaxio.celerio.configuration.entity.ColumnConfig;
import com.jaxio.celerio.configuration.entity.EntityConfig;
import com.jaxio.celerio.factory.RelationCollisionUtil;
import com.jaxio.celerio.model.Attribute;
import com.jaxio.celerio.model.Entity;

public class ExtendedEntityTest {

	@Test
	public void testGetAttributesList() {
		// on instancie la classe à tester
		ExtendedEntity extEntity = new ExtendedEntity();
		
		RelationCollisionUtil relationUtil = new RelationCollisionUtil();
		
		// on crée et configure une classe Entity (=TABLE)
		Entity entity = new Entity();
		EntityConfig entityConfig = new EntityConfig();
		entityConfig.setEntityName("test");
		entity.setEntityConfig(entityConfig);
		System.out.println(entity.getCollisionUtil());
		
		// on crée et configure une classe Attribute (=COLONNE)
		Attribute attribute = new Attribute();
		ColumnConfig columnConfig = new ColumnConfig();
		columnConfig.setType(JdbcType.VARCHAR);
		columnConfig.setLabel("test");
		attribute.setColumnConfig(columnConfig);
		attribute.setEntity(entity);
		
		System.out.println(attribute);
		
		entity.addAttribute(attribute);
		
		extEntity.init(entity);
		
		System.out.println("nb colonnes: " + extEntity.getAttributesList());
		
		assertEquals(extEntity.getAttributesList(), "");
	}

}
