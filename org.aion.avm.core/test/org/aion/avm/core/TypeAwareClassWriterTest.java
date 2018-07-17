package org.aion.avm.core;

import java.util.Collections;

import org.aion.avm.internal.PackageConstants;
import org.junit.Assert;
import org.junit.Test;


/**
 * Tests the internal logic of TypeAwareClassWriter.
 * Note that this class is not directly unit-testable so we created a testing subclass, in order to get access to the relevant protected method.
 */
public class TypeAwareClassWriterTest {
    @Test
    public void testJdkOnly_basic() throws Exception {
        TestClass clazz = new TestClass(new HierarchyTreeBuilder());
        String common = clazz.testing_getCommonSuperClass("java/lang/String", "java/lang/Throwable");
        Assert.assertEquals("java/lang/Object", common);
    }

    @Test
    public void testJdkOnly_exceptions() throws Exception {
        TestClass clazz = new TestClass(new HierarchyTreeBuilder());
        String common = clazz.testing_getCommonSuperClass("java/lang/OutOfMemoryError", "java/lang/Error");
        Assert.assertEquals("java/lang/Error", common);
    }

    @Test
    public void testWrappers_generated() throws Exception {
        TestClass clazz = new TestClass(new HierarchyTreeBuilder());
        String common = clazz.testing_getCommonSuperClass(PackageConstants.kExceptionWrapperSlashPrefix + "java/lang/OutOfMemoryError", PackageConstants.kExceptionWrapperSlashPrefix + "java/lang/Error");
        Assert.assertEquals(PackageConstants.kExceptionWrapperSlashPrefix + "java/lang/Error", common);
    }

    @Test
    public void testWrappers_generatedAndreal() throws Exception {
        TestClass clazz = new TestClass(new HierarchyTreeBuilder());
        String common = clazz.testing_getCommonSuperClass(PackageConstants.kExceptionWrapperSlashPrefix + "java/lang/OutOfMemoryError", "java/lang/OutOfMemoryError");
        Assert.assertEquals("java/lang/Throwable", common);
    }

    @Test
    public void testShadows_both() throws Exception {
        TestClass clazz = new TestClass(new HierarchyTreeBuilder());
        String common = clazz.testing_getCommonSuperClass(PackageConstants.kShadowSlashPrefix + "java/lang/OutOfMemoryError", PackageConstants.kShadowSlashPrefix + "java/lang/TypeNotPresentException");
        Assert.assertEquals(PackageConstants.kShadowSlashPrefix + "java/lang/Throwable", common);
    }

    @Test
    public void testGeneratedOnly() throws Exception {
        HierarchyTreeBuilder builder = new HierarchyTreeBuilder();
        TestClass clazz = new TestClass(builder);
        builder.addClass("A", "java.lang.Object", null);
        builder.addClass("B", "A", null);
        builder.addClass("C", "B", null);
        String common = clazz.testing_getCommonSuperClass("B", "C");
        Assert.assertEquals("B", common);
        builder.addClass("B2", "A", null);
        common = clazz.testing_getCommonSuperClass("B", "B2");
        Assert.assertEquals("A", common);
    }


    private static class TestClass extends TypeAwareClassWriter {
        public TestClass(HierarchyTreeBuilder dynamicHierarchyBuilder) {
            super(0, new ParentPointers(Collections.emptySet(), new Forest<String, byte[]>()), dynamicHierarchyBuilder);
        }
        public String testing_getCommonSuperClass(String type1, String type2) {
            return this.getCommonSuperClass(type1, type2);
        }
    }
}
