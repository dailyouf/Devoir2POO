package minebay.test;

import minebay.FusionSortedIterator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.ListIterator;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Classe de test pour FusionSortedIterator
 * @author Youcef
 */
class FusionSortedIteratorTestV2 {

        private ListIterator<Integer> it1, it2, it3;
        private FusionSortedIterator<Integer> iterator;

        @BeforeEach
        void setUp() {
                it1 = new ArrayList<>(Arrays.asList(1, 3, 5)).listIterator();
                it2 = new ArrayList<>(Arrays.asList(2, 4, 6)).listIterator();
                it3 = new ArrayList<>(Arrays.asList(0, 7, 8)).listIterator();
        }

        @Test
        void testConstructorWithNaturalOrder() {
                iterator = new FusionSortedIterator<>(Arrays.asList(it1, it2, it3));
                assertNotNull(iterator.comparator());
                assertTrue(iterator.hasNext());
                assertFalse(iterator.hasPrevious());
        }

        @Test
        void testConstructorWithCustomComparator() {
                Comparator<Integer> reverseOrder = Comparator.reverseOrder();
                iterator = new FusionSortedIterator<>(Arrays.asList(it1, it2), reverseOrder);
                assertEquals(reverseOrder, iterator.comparator());
        }

        @Test
        void testHasNext() {
                iterator = new FusionSortedIterator<>(Arrays.asList(it1, it2));
                assertTrue(iterator.hasNext());
                while (iterator.hasNext()) {
                        iterator.next();
                }
                assertFalse(iterator.hasNext());
        }

        @Test
        void testNext() {
                iterator = new FusionSortedIterator<>(Arrays.asList(it1, it2));
                assertEquals(1, iterator.next());
                assertEquals(2, iterator.next());
                assertEquals(3, iterator.next());
        }

        @Test
        void testHasPrevious() {
                iterator = new FusionSortedIterator<>(Arrays.asList(it1, it2));
                assertFalse(iterator.hasPrevious());
                iterator.next();
                assertTrue(iterator.hasPrevious());
        }

        @Test
        void testPrevious() {
                iterator = new FusionSortedIterator<>(Arrays.asList(it1, it2, it3));
                iterator.next();
                iterator.next();
                iterator.next();
                assertEquals(2, iterator.previous());
                assertEquals(1, iterator.previous());
                assertEquals(0, iterator.previous());
        }

        @Test
        void testPreviousOnBoundary() {
                iterator = new FusionSortedIterator<>(Arrays.asList(it1, it2));
                iterator.next();
                assertEquals(1, iterator.previous());
                assertThrows(NoSuchElementException.class, iterator::previous);
        }

        @Test
        void testPreviousWithEmptyIterators() {
                iterator = new FusionSortedIterator<>(Arrays.asList(
                        new ArrayList<Integer>().listIterator(),
                        new ArrayList<Integer>().listIterator()
                ));
                assertFalse(iterator.hasPrevious());
                assertThrows(NoSuchElementException.class, iterator::previous);
        }

        @Test
        void testRemove() {
                iterator = new FusionSortedIterator<>(Arrays.asList(it1, it2));
                iterator.next();
                iterator.remove();
                assertFalse(it1.hasPrevious());
                assertEquals(3, it1.next());
        }

        @Test
        void testRemoveAfterPrevious() {
                iterator = new FusionSortedIterator<>(Arrays.asList(it1, it2));
                iterator.next();
                iterator.next();
                iterator.previous();
                iterator.remove();
                assertEquals(4, it2.next());
        }

        @Test
        void testRemoveWithoutCallingNextOrPrevious() {
                iterator = new FusionSortedIterator<>(Arrays.asList(it1, it2));
                assertThrows(IllegalStateException.class, iterator::remove);
        }

        @Test
        void testRemoveMultipleTimes() {
                iterator = new FusionSortedIterator<>(Arrays.asList(it1, it2));
                iterator.next();
                iterator.remove();
                assertThrows(IllegalStateException.class, iterator::remove);
        }

        @Test
        void testRemoveOnEmptyIterator() {
                iterator = new FusionSortedIterator<>(Arrays.asList(
                        new ArrayList<Integer>().listIterator()
                ));
                assertFalse(iterator.hasNext());
                assertThrows(NoSuchElementException.class, iterator::next);
                assertThrows(IllegalStateException.class, iterator::remove);
        }

        @Test
        void testNextIndex() {
                iterator = new FusionSortedIterator<>(Arrays.asList(it1, it2));
                assertEquals(0, iterator.nextIndex());
                iterator.next();
                assertEquals(1, iterator.nextIndex());
        }

        @Test
        void testPreviousIndex() {
                iterator = new FusionSortedIterator<>(Arrays.asList(it1, it2));
                assertEquals(-1, iterator.previousIndex());
                iterator.next();
                assertEquals(0, iterator.previousIndex());
        }

        @Test
        void testLastIndex() {
                iterator = new FusionSortedIterator<>(Arrays.asList(it1, it2));
                assertEquals(-1, iterator.lastIndex());
                iterator.next();
                assertEquals(0, iterator.lastIndex());
                iterator.previous();
                assertEquals(0, iterator.lastIndex());
        }

        @Test
        void testUnsupportedOperations() {
                iterator = new FusionSortedIterator<>(Arrays.asList(it1, it2));
                assertThrows(UnsupportedOperationException.class, () -> iterator.add(10));
                assertThrows(UnsupportedOperationException.class, () -> iterator.set(10));
        }

        @Test
        void testInvalidConstructorArguments() {
                assertThrows(NullPointerException.class, () -> new FusionSortedIterator<>(null));
                assertThrows(NullPointerException.class, () -> new FusionSortedIterator<>(Arrays.asList(it1, null)));
                assertThrows(NullPointerException.class, () -> new FusionSortedIterator<>(Arrays.asList(it1, it2), null));
        }

        @Test
        void testGetNextAndPrevious() {
                iterator = new FusionSortedIterator<>(Arrays.asList(it1, it2));
                assertEquals(1, iterator.getNext());
                iterator.next();
                assertEquals(1, iterator.getPrevious());
        }

        @Test
        void testPreviousAfterExhaustingNext() {
                iterator = new FusionSortedIterator<>(Arrays.asList(it1, it2));
                while (iterator.hasNext()) {
                        iterator.next();
                }
                assertTrue(iterator.hasPrevious());
                assertEquals(6, iterator.previous());
        }

        @Test
        void testIteratorWithMultipleEmptyLists() {
                iterator = new FusionSortedIterator<>(Arrays.asList(
                        new ArrayList<Integer>().listIterator(),
                        new ArrayList<Integer>().listIterator(),
                        new ArrayList<Integer>().listIterator()
                ));
                assertFalse(iterator.hasNext());
                assertFalse(iterator.hasPrevious());
        }

        @Test
        void testIteratorWithSingleEmptyList() {
                iterator = new FusionSortedIterator<>(Arrays.asList(
                        new ArrayList<Integer>().listIterator()
                ));
                assertFalse(iterator.hasNext());
                assertFalse(iterator.hasPrevious());
        }

        @Test
        void testNextWithAllListsExhausted() {
                iterator = new FusionSortedIterator<>(Arrays.asList(it1, it2));
                while (iterator.hasNext()) {
                        iterator.next();
                }
                assertThrows(NoSuchElementException.class, iterator::next);
        }

        @Test
        void testPreviousWithAllListsExhausted() {
                iterator = new FusionSortedIterator<>(Arrays.asList(it1, it2));
                iterator.next();
                iterator.next();
                iterator.next();
                iterator.previous();
                iterator.previous();
                iterator.previous();
                assertThrows(NoSuchElementException.class, iterator::previous);
        }

        @Test
        void testNextWithMultipleIterators() {
                iterator = new FusionSortedIterator<>(Arrays.asList(it1, it2, it3));
                assertEquals(0, iterator.next());
                assertEquals(1, iterator.next());
                assertEquals(2, iterator.next());
                assertEquals(3, iterator.next());
                assertEquals(4, iterator.next());
                assertEquals(5, iterator.next());
                assertEquals(6, iterator.next());
                assertEquals(7, iterator.next());
                assertEquals(8, iterator.next());
        }

        @Test
        void testIteratorWithEmptyIteratorList() {
                iterator = new FusionSortedIterator<>(new ArrayList<>());
                assertFalse(iterator.hasNext());
                assertFalse(iterator.hasPrevious());
        }

        @Test
        void testIteratorWithSingleEmptyIterator() {
                iterator = new FusionSortedIterator<>(Arrays.asList(new ArrayList<Integer>().listIterator()));
                assertFalse(iterator.hasNext());
                assertFalse(iterator.hasPrevious());
        }

        @Test
        void testMultipleEmptyIterators() {
                iterator = new FusionSortedIterator<>(Arrays.asList(
                        new ArrayList<Integer>().listIterator(),
                        new ArrayList<Integer>().listIterator(),
                        new ArrayList<Integer>().listIterator()
                ));
                assertFalse(iterator.hasNext());
                assertFalse(iterator.hasPrevious());
        }

        @Test
        void testNextOnEmptyIterator() {
                iterator = new FusionSortedIterator<>(Arrays.asList(new ArrayList<Integer>().listIterator()));
                assertThrows(NoSuchElementException.class, iterator::next);
        }

        @Test
        void testHasNextWithDifferentSizes() {
                ListIterator<Integer> it1 = new ArrayList<>(Arrays.asList(1, 2)).listIterator();
                ListIterator<Integer> it2 = new ArrayList<>(Arrays.asList(3, 4, 5)).listIterator();
                iterator = new FusionSortedIterator<>(Arrays.asList(it1, it2));
                assertTrue(iterator.hasNext());
                iterator.next();
                assertTrue(iterator.hasNext());
        }

        @Test
        void testRemoveAfterNext() {
                iterator = new FusionSortedIterator<>(Arrays.asList(it1, it2));
                iterator.next();
                iterator.remove();
                assertEquals(2, iterator.next());
                assertTrue(iterator.hasPrevious());
        }

        @Test
        void testPreviousOnEmptyIteratorList() {
                iterator = new FusionSortedIterator<>(new ArrayList<>());
                assertThrows(NoSuchElementException.class, iterator::previous);
        }

        @Test
        void testNextIndexWithMultipleIterators() {
                iterator = new FusionSortedIterator<>(Arrays.asList(it1, it2));
                assertEquals(0, iterator.nextIndex());
                iterator.next();
                assertEquals(1, iterator.nextIndex());
                iterator.next();
                assertEquals(2, iterator.nextIndex());
        }

        @Test
        void testPreviousIndexWithMultipleIterators() {
                iterator = new FusionSortedIterator<>(Arrays.asList(it1, it2));
                assertEquals(-1, iterator.previousIndex());
                iterator.next();
                assertEquals(0, iterator.previousIndex());
                iterator.next();
                assertEquals(1, iterator.previousIndex());
        }

        @Test
        void testRemoveAfterExhaustingAllIterators() {
                iterator = new FusionSortedIterator<>(Arrays.asList(it1, it2));
                while (iterator.hasNext()) {
                        iterator.next();
                }
                assertThrows(NoSuchElementException.class, iterator::next);
        }

        @Test
        void testHasPreviousWithMultipleIterators() {
                iterator = new FusionSortedIterator<>(Arrays.asList(it1, it2, it3));
                iterator.next();
                iterator.next();
                iterator.previous();
                assertTrue(iterator.hasPrevious());
                iterator.previous();
                assertFalse(iterator.hasPrevious());
        }

        @Test
        void testNextAndPreviousBehaviorAfterRemoval() {
                iterator = new FusionSortedIterator<>(Arrays.asList(it1, it2));
                iterator.next();
                iterator.remove();
                assertEquals(2, iterator.next());
                assertTrue(iterator.hasPrevious());
                assertEquals(2, iterator.previous());
        }

        @Test
        void testNextOnExhaustedIterator() {
                iterator = new FusionSortedIterator<>(Arrays.asList(it1, it2));
                while (iterator.hasNext()) {
                        iterator.next();
                }
                assertThrows(NoSuchElementException.class, iterator::next);
        }

        @Test
        void testConstructorWithMultipleEmptyIterators() {
                ListIterator<Integer> emptyIterator1 = new ArrayList<Integer>().listIterator();
                ListIterator<Integer> emptyIterator2 = new ArrayList<Integer>().listIterator();
                iterator = new FusionSortedIterator<>(Arrays.asList(emptyIterator1, emptyIterator2));
                assertFalse(iterator.hasNext());
                assertFalse(iterator.hasPrevious());
        }

        @Test
        void testMultipleNextCalls() {
                iterator = new FusionSortedIterator<>(Arrays.asList(it1, it2, it3));
                assertEquals(0, iterator.next());
                assertEquals(1, iterator.next());
                assertEquals(2, iterator.next());
                assertEquals(3, iterator.next());
                assertEquals(4, iterator.next());
                assertEquals(5, iterator.next());
                assertEquals(6, iterator.next());
                assertEquals(7, iterator.next());
                assertEquals(8, iterator.next());
                assertFalse(iterator.hasNext());
        }

        @Test
        void testNoNextAfterExhaustingIterators() {
                iterator = new FusionSortedIterator<>(Arrays.asList(it1, it2, it3));
                while (iterator.hasNext()) {
                        iterator.next();
                }
                assertFalse(iterator.hasNext());
                assertThrows(NoSuchElementException.class, iterator::next);
        }

}
