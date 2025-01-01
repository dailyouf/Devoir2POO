package minebay.test;

import minebay.AdCategory;
import minebay.ClassifiedAd;

import minebay.MultiEnumList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.NoSuchElementException;

public class MultiEnumListTestV2 {

        /*
        private MultiEnumList<AdCategory, ClassifiedAd> multiEnumList;

        private ClassifiedAd ad1;
        private ClassifiedAd ad2;
        private ClassifiedAd ad3;
        private ClassifiedAd ad4;

        @BeforeEach
        public void setUp() {
                multiEnumList = new MultiEnumList<>(AdCategory.class);

                ad1 = new ClassifiedAd(AdCategory.CLOTHES, "Red Dress", 50);
                ad2 = new ClassifiedAd(AdCategory.SHOES, "Running Shoes", 100);
                ad3 = new ClassifiedAd(AdCategory.BOOKS, "Java Programming", 30);
                ad4 = new ClassifiedAd(AdCategory.CLOTHES, "Blue Shirt", 20);
        }

        @Test
        public void testAdd() {
                multiEnumList.add(ad1);
                multiEnumList.add(ad2);

                //noinspection unchecked
                List<ClassifiedAd> clothesAds = (List<ClassifiedAd>) multiEnumList.get(AdCategory.CLOTHES.ordinal());
                //noinspection unchecked
                List<ClassifiedAd> shoesAds = (List<ClassifiedAd>) multiEnumList.get(AdCategory.SHOES.ordinal());

                assertEquals(1, clothesAds.size());
                assertEquals(1, shoesAds.size());
                assertTrue(clothesAds.contains(ad1));
                assertTrue(shoesAds.contains(ad2));
        }

        @Test
        public void testGet() {
                multiEnumList.add(ad1);
                multiEnumList.add(ad4);

                List<ClassifiedAd> clothesAds = (List<ClassifiedAd>) multiEnumList.get(AdCategory.CLOTHES.ordinal());
                assertEquals(2, clothesAds.size());
                assertTrue(clothesAds.contains(ad1));
                assertTrue(clothesAds.contains(ad4));
        }

        @Test
        public void testRemove() {
                multiEnumList.add(ad1);
                multiEnumList.add(ad4);

                assertTrue(multiEnumList.remove(ad1));

                List<ClassifiedAd> clothesAds = (List<ClassifiedAd>) multiEnumList.get(AdCategory.CLOTHES.ordinal());
                assertEquals(1, clothesAds.size());
                assertFalse(clothesAds.contains(ad1));
        }

        @Test
        public void testRemoveNonExistentElement() {
                multiEnumList.add(ad1);

                assertFalse(multiEnumList.remove(AdCategory.CLOTHES, ad3));
        }

        @Test
        public void testClearCategory() {
                multiEnumList.add(AdCategory.CLOTHES, ad1);
                multiEnumList.add(AdCategory.CLOTHES, ad4);

                multiEnumList.clear(AdCategory.CLOTHES);

                List<ClassifiedAd> clothesAds = multiEnumList.get(AdCategory.CLOTHES);
                assertTrue(clothesAds.isEmpty());
        }

        @Test
        public void testClearAll() {
                multiEnumList.add(AdCategory.CLOTHES, ad1);
                multiEnumList.add(AdCategory.SHOES, ad2);
                multiEnumList.clear();

                assertTrue(multiEnumList.get(AdCategory.CLOTHES).isEmpty());
                assertTrue(multiEnumList.get(AdCategory.SHOES).isEmpty());
        }

        @Test
        public void testContains() {
                multiEnumList.add(AdCategory.CLOTHES, ad1);

                assertTrue(multiEnumList.contains(AdCategory.CLOTHES, ad1));
                assertFalse(multiEnumList.contains(AdCategory.SHOES, ad1));
        }

        @Test
        public void testGetInvalidCategory() {
                List<ClassifiedAd> invalidCategoryAds = multiEnumList.get(AdCategory.GAMES);
                assertTrue(invalidCategoryAds.isEmpty());
        }

        @Test
        public void testExceptionWhenAddingNull() {
                assertThrows(NullPointerException.class, () -> multiEnumList.add(null, ad1));
                assertThrows(NullPointerException.class, () -> multiEnumList.add(AdCategory.CLOTHES, null));
        }

        @Test
        public void testIterator() {
                multiEnumList.add(AdCategory.CLOTHES, ad1);
                multiEnumList.add(AdCategory.CLOTHES, ad4);
                multiEnumList.add(AdCategory.SHOES, ad2);

                var iterator = multiEnumList.iterator();

                int count = 0;
                while (iterator.hasNext()) {
                        ClassifiedAd ad = iterator.next();
                        assertNotNull(ad);
                        count++;
                }
                assertEquals(3, count);
        }

        @Test
        public void testIteratorNoSuchElementException() {
                var iterator = multiEnumList.iterator();
                assertThrows(NoSuchElementException.class, iterator::next);
        }

         */
}
