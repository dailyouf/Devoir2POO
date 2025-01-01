package minebay.test;

import minebay.AdCategory;
import minebay.ClassifiedAd;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import java.lang.Thread;

import static org.junit.jupiter.api.Assertions.*;

class ClassifiedAdTestV2 {

        private ClassifiedAd ad1;
        private ClassifiedAd ad2;
        private ClassifiedAd ad3;
        private ClassifiedAd ad4;
        private AdCategory category;
        private AdCategory otherCategory;
        private List<ClassifiedAd> ads;

        @BeforeEach
        void setUp() {
                category = AdCategory.GAMES;
                otherCategory = AdCategory.COMPUTERS;
                ad1 = new ClassifiedAd(category, "Smartphone neuf", 500);
                ad2 = new ClassifiedAd(category, "Tablette occasion", 300);
                ad3 = new ClassifiedAd(otherCategory, "Chaise", 50);
                ad4 = new ClassifiedAd(otherCategory, "Table", 100);

                ads = new ArrayList<>();
                ads.add(ad1);
                ads.add(ad2);

                ads.add(ad3);
		try {
        		Thread.sleep(10);
    		} catch (InterruptedException e) {
        		e.printStackTrace();
    		}
                ads.add(ad4);
        }

        @Test
        void testConstructorValidInputs() {
                ClassifiedAd ad = new ClassifiedAd(category, "Ordinateur portable", 1000);
                assertNotNull(ad.getCategory());
                assertNotNull(ad.getDescription());
                assertNotNull(ad.getDate());
                assertEquals(1000, ad.getPrice());
                assertTrue(ad.getDate().isBefore(Instant.now()) || ad.getDate().equals(Instant.now()));
        }

        @Test
        void testConstructorInvalidInputs() {
                assertThrows(NullPointerException.class, () -> new ClassifiedAd(null, "Description", 500));
                assertThrows(NullPointerException.class, () -> new ClassifiedAd(category, null, 500));
                assertThrows(IllegalArgumentException.class, () -> new ClassifiedAd(category, "", 500));
                assertThrows(IllegalArgumentException.class, () -> new ClassifiedAd(category, "Description", 0));
                assertThrows(IllegalArgumentException.class, () -> new ClassifiedAd(category, "Description", -100));
        }

        @Test
        void testGetters() {
                assertEquals(category, ad1.getCategory());
                assertEquals("Smartphone neuf", ad1.getDescription());
                assertEquals(500, ad1.getPrice());
                assertNotNull(ad1.getDate());
        }

        @Test
        void testIsBefore() {
                assertTrue(ad1.isBefore(ad2) || ad1.getDate().equals(ad2.getDate()));
                assertFalse(ad3.isBefore(ad1));
                assertTrue(ad1.isBefore(ad4) || ad1.getDate().equals(ad4.getDate()));
        }

        @Test
        void testIsAfter() {
                assertTrue(ad2.isAfter(ad1) || ad2.getDate().equals(ad1.getDate()));
                assertFalse(ad1.isAfter(ad3));
                assertTrue(ad4.isAfter(ad3) || ad3.getDate().equals(ad4.getDate()));
        }

        @Test
        void testEqualsAndHashCode() {
                ClassifiedAd adDuplicate = new ClassifiedAd(category, "Smartphone neuf", 500);
                ClassifiedAd adDifferent = new ClassifiedAd(category, "Ordinateur portable", 1500);

                assertFalse(ad1.equals(ad2));
                assertTrue(ad1.equals(ad1));
                assertFalse(ad1.equals(adDifferent));
                assertEquals(ad1.hashCode(), ad1.hashCode());
                assertNotEquals(ad1.hashCode(), ad2.hashCode());
        }

        @Test
        void testToString() {
                String ad1String = ad1.toString();
                assertNotNull(ad1String);
                assertTrue(ad1String.contains(ad1.getCategory().toString()));
                assertTrue(ad1String.contains(ad1.getDescription()));
                assertTrue(ad1String.contains(String.valueOf(ad1.getPrice())));
                assertTrue(ad1String.contains(ad1.getDate().toString()));
        }

        @Test
        void testCompareTo() {
                assertTrue(ad1.compareTo(ad2) > 0 || ad1.getDate().equals(ad2.getDate()));
                assertTrue(ad2.compareTo(ad1) < 0 || ad2.getDate().equals(ad1.getDate()));
                assertEquals(0, ad1.compareTo(ad1));

                assertTrue(ad3.compareTo(ad4) > 0 || ad3.getDate().equals(ad4.getDate()));
                assertTrue(ad4.compareTo(ad3) < 0 || ad4.getDate().equals(ad3.getDate()));
        }

        @Test
        void testBoundaryValues() {
                ClassifiedAd adBoundary = new ClassifiedAd(category, "Boundary Test", 1);
                assertEquals(1, adBoundary.getPrice());
                assertNotNull(adBoundary.getDate());
        }

        @Test
        void testNullInputsInMethods() {
                assertThrows(NullPointerException.class, () -> ad1.isBefore(null));
                assertThrows(NullPointerException.class, () -> ad1.isAfter(null));
                assertThrows(NullPointerException.class, () -> ad1.compareTo(null));
        }
        
        @Test
        void testMultipleInstancesComparison() {
                ads.sort(ClassifiedAd::compareTo);
                for (int i = 0; i < ads.size() - 1; i++) {
                        assertTrue(ads.get(i).getDate().isAfter(ads.get(i + 1).getDate()) ||
                                   ads.get(i).getDate().equals(ads.get(i + 1).getDate()));
                }
        }

        @Test
        void testPriceOrdering() {
                ads.sort((a, b) -> Integer.compare(b.getPrice(), a.getPrice()));
                for (int i = 0; i < ads.size() - 1; i++) {
                        assertTrue(ads.get(i).getPrice() >= ads.get(i + 1).getPrice());
                }
        }

        @Test
        void testCategories() {
                assertEquals(category, ad1.getCategory());
                assertEquals(otherCategory, ad3.getCategory());
                assertNotEquals(ad1.getCategory(), ad3.getCategory());
        }
}
