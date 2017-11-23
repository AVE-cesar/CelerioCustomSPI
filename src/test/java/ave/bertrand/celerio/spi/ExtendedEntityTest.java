package ave.bertrand.celerio.spi;

import static org.junit.Assert.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.jaxio.celerio.configuration.database.JdbcType;
import com.jaxio.celerio.configuration.entity.ColumnConfig;
import com.jaxio.celerio.configuration.entity.EntityConfig;
import com.jaxio.celerio.factory.EntityFactory;
import com.jaxio.celerio.factory.RelationCollisionUtil;
import com.jaxio.celerio.model.Attribute;
import com.jaxio.celerio.model.Entity;

import junit.framework.Assert;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes= {BeanA.class})
public class ExtendedEntityTest {

	@Autowired
    private BeanA beanA;
    
    @Autowired
    private ApplicationContext applicationContext;
    
    @Test
    public void testContext() {
        Assert.assertNotNull(applicationContext.getBean(Entity.class));
    }
    
	@Test
	public void testGetAttributesList() {
		
		System.out.println(beanA);
		// on instancie la classe à tester
		ExtendedEntity extEntity = new ExtendedEntity();
		
		EntityConfig entityConfig = new EntityConfig();
		entityConfig.setTableName("LaTable");
		entityConfig.setEntityName("test");
		
		EntityFactory entityFactory = new EntityFactory();
		//entityFactory.
		entityFactory.buildEntity(entityConfig);
		
		
		
		// on crée et configure une classe Entity (=TABLE)
		Entity entity = new Entity();
		
		
		entity.setEntityConfig(entityConfig);
		System.out.println("CollisionUtil: " + entity.getCollisionUtil());
		
		// on crée et configure une classe Attribute (=COLONNE)
		/*
		Attribute attribute = new Attribute();
		ColumnConfig columnConfig = new ColumnConfig();
		columnConfig.setType(JdbcType.VARCHAR);
		columnConfig.setLabel("test");
		attribute.setColumnConfig(columnConfig);
		attribute.setEntity(entity);
		
		System.out.println(attribute);
		
		entity.addAttribute(attribute);
		*/
		extEntity.init(entity);
		
		System.out.println("nb colonnes: " + extEntity.getAttributesList());
		
		assertEquals(extEntity.getAttributesList(), "");
	}

}
