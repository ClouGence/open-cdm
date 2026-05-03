package com.clougence.utils.reflect.annotations;

import java.io.IOException;
import java.util.List;

import org.junit.jupiter.api.Test;

import com.clougence.utils.reflect.Annotation;
import com.clougence.utils.reflect.Annotations;

public class TypeHasMultipleTest {

    @Test
    public void booleanArray_Test() throws IOException {
        Annotations annotations = Annotations.ofClass(TypeHasMultipleTestTestBean.class);
        Annotation annotation1 = annotations.getAnnotation(ArrayByteDim1.class);
        Annotation annotation2 = annotations.getAnnotation(ArrayBooleanDim1.class);
        Annotation annotation3 = annotations.getAnnotation(IndexDescribeSet.class);
        Annotation annotation4 = annotations.getAnnotation(AnnoCfg.class);

        assert annotation1.getByte("value") == 11;
        assert annotation1.getByteArray("value").size() == 2;
        assert annotation1.getByteArray("value").get(0) == 11;
        assert annotation1.getByteArray("value").get(1) == 22;

        assert annotation2.getBoolean("value");
        assert annotation2.getBooleanArray("value").size() == 2;
        assert annotation2.getBooleanArray("value").get(0);
        assert !annotation2.getBooleanArray("value").get(1);

        List<Annotation> annotationArray = annotation3.getAnnotationArray("value");
        assert annotationArray.size() == 2;
        assert annotationArray.get(0).getString("name").equals("idx_1");
        assert annotationArray.get(0).getStringArray("columns").get(0).equals("a");
        assert annotationArray.get(0).getStringArray("columns").get(1).equals("b");
        assert annotationArray.get(1).getString("name").equals("idx_2");
        assert annotationArray.get(1).getStringArray("columns").get(0).equals("aa");
        assert annotationArray.get(1).getStringArray("columns").get(1).equals("bb");

        assert annotation4.getBoolean("xml");
        assert annotation4.getAnnotation("intConfig").getInt("intConfig") == 123;
        assert annotation4.getAnnotation("intConfig").getString("strConfig").equals("hello");
        assert annotation4.getAnnotationArray("intConfigArrays").get(0).getInt("intConfig") == 456;
        assert annotation4.getAnnotationArray("intConfigArrays").get(0).getString("strConfig").equals("java");
        assert annotation4.getAnnotationArray("intConfigArrays").get(1).getInt("intConfig") == 789;
        assert annotation4.getAnnotationArray("intConfigArrays").get(1).getString("strConfig").equals("python");
    }

    @Test
    public void emptyArray_Test() throws IOException {
        Annotations annotations = Annotations.ofClass(ClassLoader.class);
    }
}

@ArrayByteDim1({ 11, 22 })
@ArrayBooleanDim1({ true, false })
@IndexDescribe(name = "idx_1", columns = { "a", "b" })
@IndexDescribe(name = "idx_2", columns = { "aa", "bb" })
@AnnoCfg(xml = true, //
        intConfig = @AnnoConfig(intConfig = 123, strConfig = "hello"),//
        intConfigArrays = { @AnnoConfig(intConfig = 456, strConfig = "java"), @AnnoConfig(intConfig = 789, strConfig = "python") })
class TypeHasMultipleTestTestBean {

}
