/*
 * Copyright 2026 杭州开云集致科技有限公司
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.clougence.clouddm.ds.metadata;

import static org.junit.Assert.*;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import com.clougence.clouddm.dsfamily.execute.AbstractMetadataProvider;
import com.clougence.clouddm.dsfamily.execute.AbstractRdbUmiService;
import com.clougence.clouddm.sdk.execute.session.Session;
import com.clougence.clouddm.sdk.execute.session.rdb.DefaultRdbMetaService;
import com.clougence.clouddm.sdk.execute.session.rdb.DmRdbUmiService;
import com.clougence.schema.umi.special.rdb.RdbTable;
import com.clougence.schema.umi.struts.UmiTypes;
import com.clougence.utils.function.ESupplier;

public abstract class SelectObjectContract {
    private final Class<? extends DefaultRdbMetaService> metaServiceType;
    private final String                                 version;
    private final Map<UmiTypes, Object>                  levels = Map.of(UmiTypes.Catalog, "TESTDB", UmiTypes.Schema, "TEST_SCHEMA");

    protected SelectObjectContract(Class<? extends DefaultRdbMetaService> metaServiceType, String version){
        this.metaServiceType = metaServiceType;
        this.version = version;
    }

    private DmRdbUmiService service(Connection connection) throws Exception {
        Constructor<? extends DefaultRdbMetaService> constructor = this.metaServiceType.getDeclaredConstructor(Session.class);
        constructor.setAccessible(true);
        DefaultRdbMetaService meta = constructor.newInstance(new EmptyMetadataJdbc(null, this.version).session());
        Method factory = method(this.metaServiceType, "rdbUmiService", Connection.class);
        factory.setAccessible(true);
        return (DmRdbUmiService) factory.invoke(meta, connection);
    }

    @Test
    public void implementsSelectObjectEntry() throws Exception {
        DmRdbUmiService service = service(new EmptyMetadataJdbc(null, this.version).connection());
        assertNotEquals(AbstractRdbUmiService.class, service.getClass().getMethod("fetchSelectObject", Map.class, String.class).getDeclaringClass());
    }

    @Test
    public void implementsProviderSelectObjectLookup() throws Throwable {
        DmRdbUmiService service = service(new EmptyMetadataJdbc(null, this.version).connection());
        Class<?> type = service.getClass();
        Field supplier = null;
        while (supplier == null) {
            try {
                supplier = type.getDeclaredField("metadataSupplier");
            } catch (NoSuchFieldException e) {
                type = type.getSuperclass();
            }
        }

        supplier.setAccessible(true);
        Object provider = ((ESupplier<?, ?>) supplier.get(service)).eGet();
        Method load = provider.getClass().getMethod("loadSelectObject", String.class, String.class, String.class);
        if (load.getDeclaringClass() == AbstractMetadataProvider.class) {
            Method lookup = method(provider.getClass(), "fetchSelectObjectByPart", Connection.class, String.class, String.class, List.class);
            assertNotEquals(AbstractMetadataProvider.class, lookup.getDeclaringClass());
        }
    }

    @Test
    public void emptyMetadataReachesRealLookup() throws Exception {
        EmptyMetadataJdbc jdbc = new EmptyMetadataJdbc(null, this.version);
        Object result = service(jdbc.connection()).fetchSelectObject(this.levels, "MISSING_TABLE");
        if (result != null) {
            assertTrue(((RdbTable) result).getColumns() == null || ((RdbTable) result).getColumns().isEmpty());
        }

        assertTrue("Metadata SQL must actually be reached", jdbc.queryCount() > 0);
    }

    @Test
    public void sqlFailureIsNotReplacedWithUnsupportedOperation() throws Exception {
        SQLException failure = new SQLException("metadata unavailable", "08006");
        EmptyMetadataJdbc jdbc = new EmptyMetadataJdbc(failure, this.version);
        try {
            service(jdbc.connection()).fetchSelectObject(this.levels, "MISSING_TABLE");
            fail("Expected SQLException");
        } catch (SQLException e) {
            assertSame(failure, e);
        }
    }

    private static Method method(Class<?> type, String name, Class<?>... parameters) throws NoSuchMethodException {
        while (type != null) {
            try {
                return type.getDeclaredMethod(name, parameters);
            } catch (NoSuchMethodException e) {
                type = type.getSuperclass();
            }
        }
        throw new NoSuchMethodException(name);
    }
}
