package minebay.test;

import minebay.AdCategory;
import minebay.ClassifiedAd;
import minebay.MultiEnumList;
import org.junit.jupiter.api.Test;

import java.util.EnumSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class MultiEnumListTestV2 {

        @Test
        void testInitializationWithEmptyConstructor() {
                MultiEnumList<AdCategory, ClassifiedAd> list = new MultiEnumList<>(AdCategory.class);

                assertNotNull(list.getCatType());
                assertEquals(AdCategory.class, list.getCatType());
                assertTrue(list.isEmpty());
                assertEquals(0, list.size());
        }

        @Test
        void testInitializationWithCollection() {
                ClassifiedAd ad1 = new ClassifiedAd(AdCategory.CLOTHES, "Shirt", 20);
                ClassifiedAd ad2 = new ClassifiedAd(AdCategory.SHOES, "Sneakers", 50);
                ClassifiedAd ad3 = new ClassifiedAd(AdCategory.BOOKS, "Novel", 10);

                Set<ClassifiedAd> ads = Set.of(ad1, ad2, ad3);
                MultiEnumList<AdCategory, ClassifiedAd> list = new MultiEnumList<>(AdCategory.class, ads);

                assertEquals(3, list.size());
                assertTrue(list.contains(ad1));
                assertTrue(list.contains(ad2));
                assertTrue(list.contains(ad3));
        }

        @Test
        void testAddElement() {
                MultiEnumList<AdCategory, ClassifiedAd> list = new MultiEnumList<>(AdCategory.class);
                ClassifiedAd ad = new ClassifiedAd(AdCategory.CLOTHES, "Jeans", 40);

                assertTrue(list.add(ad));
                assertEquals(1, list.size());
                assertTrue(list.contains(ad));
        }

        @Test
        void testAddNullThrowsException() {
                MultiEnumList<AdCategory, ClassifiedAd> list = new MultiEnumList<>(AdCategory.class);

                assertThrows(NullPointerException.class, () -> list.add(null));
        }

        @Test
        void testRemoveElement() {
                ClassifiedAd ad1 = new ClassifiedAd(AdCategory.CLOTHES, "Shirt", 20);
                MultiEnumList<AdCategory, ClassifiedAd> list = new MultiEnumList<>(AdCategory.class, Set.of(ad1));

                assertTrue(list.remove(ad1));
                assertFalse(list.contains(ad1));
                assertEquals(0, list.size());
        }

        @Test
        void testRemoveNonExistentElement() {
                ClassifiedAd ad1 = new ClassifiedAd(AdCategory.CLOTHES, "Shirt", 20);
                MultiEnumList<AdCategory, ClassifiedAd> list = new MultiEnumList<>(AdCategory.class);

                assertFalse(list.remove(ad1));
        }

        @Test
        void testClear() {
                ClassifiedAd ad1 = new ClassifiedAd(AdCategory.CLOTHES, "Shirt", 20);
                ClassifiedAd ad2 = new ClassifiedAd(AdCategory.SHOES, "Sneakers", 50);
                MultiEnumList<AdCategory, ClassifiedAd> list = new MultiEnumList<>(AdCategory.class, Set.of(ad1, ad2));

                list.clear();
                assertTrue(list.isEmpty());
        }

        @Test
        void testClearWithCategorySet() {
                ClassifiedAd ad1 = new ClassifiedAd(AdCategory.CLOTHES, "Shirt", 20);
                ClassifiedAd ad2 = new ClassifiedAd(AdCategory.SHOES, "Sneakers", 50);
                MultiEnumList<AdCategory, ClassifiedAd> list = new MultiEnumList<>(AdCategory.class, Set.of(ad1, ad2));

                list.clear(EnumSet.of(AdCategory.CLOTHES));
                assertFalse(list.contains(ad1));
                assertTrue(list.contains(ad2));
                assertEquals(1, list.size());
        }

        @Test
        void testGetElementByIndex() {
                ClassifiedAd ad1 = new ClassifiedAd(AdCategory.CLOTHES, "Shirt", 20);
                ClassifiedAd ad2 = new ClassifiedAd(AdCategory.SHOES, "Sneakers", 50);
                MultiEnumList<AdCategory, ClassifiedAd> list = new MultiEnumList<>(AdCategory.class, Set.of(ad1, ad2));

                assertEquals(ad1, list.get(0));
                assertEquals(ad2, list.get(1));
        }

        @Test
        void testGetElementByIndexOutOfBounds() {
                MultiEnumList<AdCategory, ClassifiedAd> list = new MultiEnumList<>(AdCategory.class);

                assertThrows(IndexOutOfBoundsException.class, () -> list.get(0));
        }

        @Test
        void testSizeByCategorySet() {
                ClassifiedAd ad1 = new ClassifiedAd(AdCategory.CLOTHES, "Shirt", 20);
                ClassifiedAd ad2 = new ClassifiedAd(AdCategory.SHOES, "Sneakers", 50);
                MultiEnumList<AdCategory, ClassifiedAd> list = new MultiEnumList<>(AdCategory.class, Set.of(ad1, ad2));

                assertEquals(1, list.size(EnumSet.of(AdCategory.CLOTHES)));
                assertEquals(1, list.size(EnumSet.of(AdCategory.SHOES)));
                assertEquals(0, list.size(EnumSet.of(AdCategory.BOOKS)));
        }

        @Test
        void testContains() {
                ClassifiedAd ad1 = new ClassifiedAd(AdCategory.CLOTHES, "Shirt", 20);
                MultiEnumList<AdCategory, ClassifiedAd> list = new MultiEnumList<>(AdCategory.class, Set.of(ad1));

                assertTrue(list.contains(ad1));
        }

        @Test
        void testContainsNonExistentElement() {
                ClassifiedAd ad1 = new ClassifiedAd(AdCategory.CLOTHES, "Shirt", 20);
                MultiEnumList<AdCategory, ClassifiedAd> list = new MultiEnumList<>(AdCategory.class);

                assertFalse(list.contains(ad1));
        }
}
