package minebay.test;

import static org.junit.jupiter.api.Assertions.*;

import minebay.AdCategory;
import minebay.AdState;
import minebay.ClassifiedAd;
import minebay.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.EnumSet;
import java.util.Set;

class UserTestV2 {
        private User user;

        @BeforeEach
        void setUp() {
                user = new User("Youcef", "password123");
        }

        @Test
        void testConstructor() {
                assertEquals("Youcef", user.getName());
                assertEquals("password123", user.getPassword());
                assertNotNull(user.getRegistrationDate());
                assertEquals(User.DEFAULT_CASH_AMMOUNT, user.getAvailableCash());
                assertEquals(AdState.OPEN, user.getSelectedAdState());
                assertEquals(EnumSet.allOf(AdCategory.class), user.getSelectedCategories());
        }

        @Test
        void testSelectAdState() {
                user.selectAdState(AdState.CLOSED);
                assertEquals(AdState.CLOSED, user.getSelectedAdState());
        }

        @Test
        void testAddSelectedCategory() {
                AdCategory newCategory = AdCategory.GAMES;
                assertTrue(user.clearSelectedCategories());
                assertTrue(user.addSelectedCategory(newCategory));
                assertTrue(user.getSelectedCategories().contains(newCategory));
        }

        @Test
        void testRemoveSelectedCategory() {
                AdCategory category = AdCategory.GAMES;
                user.addSelectedCategory(category);
                assertTrue(user.removeSelectedCategory(category));
                assertFalse(user.getSelectedCategories().contains(category));
        }

        @Test
        void testClearSelectedCategories() {
                user.addSelectedCategory(AdCategory.GAMES);
                user.clearSelectedCategories();
                assertTrue(user.getSelectedCategories().isEmpty());
        }

        @Test
        void testSelectAllCategories() {
                user.clearSelectedCategories();
                assertTrue(user.selectAllCategories());
                assertEquals(EnumSet.allOf(AdCategory.class), user.getSelectedCategories());
        }

        @Test
        void testAdd() {
                AdCategory category = AdCategory.GAMES;
                String description = "Laptop for sale";
                int price = 500;

                ClassifiedAd ad = user.add(category, description, price);

                assertNotNull(ad);
                assertEquals(category, ad.getCategory());
                assertEquals(description, ad.getDescription());
                assertEquals(price, ad.getPrice());
                // assertTrue(ad.getDate().isAfter(user.getRegistrationDate()));
                // (ad.getDate().isBefore(Instant.now()));
        }

        @Test
        void testBuy() {
                User vendor = new User("Vendor", "vendorpass");
                ClassifiedAd ad = vendor.add(AdCategory.COMPUTERS, "Laptop", 50);

                user.buy(vendor, ad);
                assertTrue(user.containsInState(AdState.PURCHASE, ad));
                assertTrue(vendor.containsInState(AdState.CLOSED, ad));
                assertEquals(User.DEFAULT_CASH_AMMOUNT  - ad.getPrice(), user.getAvailableCash());
                assertEquals(User.DEFAULT_CASH_AMMOUNT  + ad.getPrice(), vendor.getAvailableCash());
        }

        @Test
        void testSize() {
                assertEquals(0, user.size());
                user.add(AdCategory.GAMES, "Laptop", 500);
                assertEquals(1, user.size());
        }

        @Test
        void testGet() {
                AdCategory category = AdCategory.GAMES;
                ClassifiedAd ad = user.add(category, "Laptop", 500);
                assertEquals(ad, user.get(0));
        }

        @Test
        void testContainsInState() {
                AdCategory category = AdCategory.GAMES;
                ClassifiedAd ad = user.add(category, "Laptop", 500);

                assertTrue(user.containsInState(AdState.OPEN, ad));
        }

        @Test
        void testIterator() {
                user.add(AdCategory.GAMES, "Laptop", 500);
                user.add(AdCategory.SHOES, "T-shirt", 20);

                assertTrue(user.iterator().hasNext());
        }

        @Test
        void testToString() {
                String userInfo = user.toString();
                assertTrue(userInfo.contains("Youcef"));
                assertTrue(userInfo.contains("OPEN"));
                assertTrue(userInfo.contains("CLOSED"));
                assertTrue(userInfo.contains("PURCHASE"));
        }

        @Test
        void testInvariants() {
                assertDoesNotThrow(() -> {
                        assertNotNull(user.getName());
                        assertFalse(user.getName().isBlank());
                        assertNotNull(user.getPassword());
                        assertFalse(user.getPassword().isBlank());
                        assertNotNull(user.getRegistrationDate());
                        assertTrue(user.getAvailableCash() >= 0);
                });
        }
}
