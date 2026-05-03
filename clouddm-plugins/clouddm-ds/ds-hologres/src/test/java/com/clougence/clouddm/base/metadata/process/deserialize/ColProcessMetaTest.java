//package com.clougence.clouddm.base.metadata.process.deserialize;
//
//import java.io.StringWriter;
//import java.io.Writer;
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//import org.junit.Test;
//
//import com.clougence.clouddm.base.metadata.process.ColUnit;
//import com.clougence.clouddm.base.metadata.process.datahandle.DataHandleConfigWrapper;
//import com.clougence.clouddm.base.metadata.process.datahandle.PkgConfigDTO;
//import com.clougence.clouddm.base.metadata.process.serialize.DataHandleConfigSerializer;
//import com.clougence.clouddm.base.utils.JacksonUtil;
//import com.fasterxml.jackson.core.JsonFactory;
//import com.fasterxml.jackson.core.JsonGenerator;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.fasterxml.jackson.databind.SerializerProvider;
//import com.fasterxml.jackson.databind.module.SimpleModule;
//
//import lombok.SneakyThrows;
//
///**
// * Reference: https://stackoverflow.com/questions/21787128/how-to-unit-test-jackson-jsonserializer-and-jsondeserializer
// *
// * @author wanshao create time is 2021/5/20
// **/
//public class ColProcessMetaTest {
//
//    private static DataHandleConfigWrapper dataHandleConfigWrapper = new DataHandleConfigWrapper();
//
//    static {
//        Map<ColUnit, List<PkgConfigDTO>> colProcessMetaDataListMap = new HashMap<>();
//        ColUnit colProcessMetaData = new ColUnit();
//        colProcessMetaData.setCatalog("catalogName");
//        colProcessMetaData.setSchema("mySchema");
//        colProcessMetaData.setTable("myTable");
//        colProcessMetaData.setColumn("myCol");
//        List<PkgConfigDTO> pkgConfigDTOList = new ArrayList<>();
//        PkgConfigDTO pkgConfigDTO = PkgConfigDTO.builder().pkgMd5("md5").pkgInstName("instName").fileName("fileName").build();
//        PkgConfigDTO pkgConfigDTO2 = PkgConfigDTO.builder().pkgMd5("md52").pkgInstName("instName2").fileName("fileName2").build();
//        pkgConfigDTOList.add(pkgConfigDTO);
//        pkgConfigDTOList.add(pkgConfigDTO2);
//
//        colProcessMetaDataListMap.put(colProcessMetaData, pkgConfigDTOList);
//        dataHandleConfigWrapper.setPkgConfigDTOMap(colProcessMetaDataListMap);
//    }
//
//    @SneakyThrows
//    @Test
//    public void serialize() {
//        Writer jsonWriter = new StringWriter();
//        JsonGenerator jsonGenerator = new JsonFactory().createGenerator(jsonWriter);
//        ObjectMapper mapper = new ObjectMapper();
//        jsonGenerator.setCodec(mapper);
//        SerializerProvider serializerProvider = new ObjectMapper().getSerializerProvider();
//
//        new DataHandleConfigSerializer().serialize(dataHandleConfigWrapper, jsonGenerator, serializerProvider);
//        jsonGenerator.flush();
//        String serializedJson = jsonWriter.toString();
//        System.out.println(serializedJson);
//
//    }
//
//    @SneakyThrows
//    @Test
//    public void deserialize() {
//
//        ObjectMapper mapper = new ObjectMapper();
//        SimpleModule module = new SimpleModule();
//        module.addDeserializer(DataHandleConfigWrapper.class, new DataHandleConfigDeserializer());
//        mapper.registerModule(module);
//        String json = mapper.writeValueAsString(dataHandleConfigWrapper);
//
//        DataHandleConfigWrapper readValue = mapper.readValue(json, DataHandleConfigWrapper.class);
//
//        System.out.println(JacksonUtil.toJson(readValue));
//
//    }
//}
