package com.clougence.sql.common.registry;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;
import org.xml.sax.SAXException;

class DatabaseResourceXmlLoaderTest {

    @Test
    void validatesAndLoadsCatalog() {
        var resources = DatabaseResourceXmlLoader.load(getClass(), "valid-database-resources.xml");

        assertEquals(1, resources.size());
        assertEquals("AVG", resources.get(0).name());
        assertEquals(java.util.Set.of("1.0", "2"), resources.get(0).versions());
    }

    @Test
    void rejectsCatalogThatViolatesSchemaOrder() {
        IllegalStateException error = assertThrows(IllegalStateException.class,
                () -> DatabaseResourceXmlLoader.load(getClass(), "invalid-database-resources.xml"));

        assertInstanceOf(SAXException.class, error.getCause());
    }
}
