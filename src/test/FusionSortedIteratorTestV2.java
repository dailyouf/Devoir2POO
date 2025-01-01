package minebay;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.ListIterator;

public class FusionSortedIteratorTestV2 {

        private ListIterator<Integer> iterator1;
        private ListIterator<Integer> iterator2;
        private ListIterator<Integer> iterator3;

        @BeforeEach
        public void setUp() {
                iterator1 = new ArrayList<>(Arrays.asList(1, 4, 7)).listIterator();
                iterator2 = new ArrayList<>(Arrays.asList(2, 5, 8)).listIterator();
                iterator3 = new ArrayList<>(Arrays.asList(3, 6, 9)).listIterator();
        }

        @Test
        public void testHasNextAndNext() {
                FusionSortedIterator<Integer> fusionIterator = new FusionSortedIterator<>(
                        Arrays.asList(iterator1, iterator2, iterator3)
                );

                ArrayList<Integer> result = new ArrayList<>();
                while (fusionIterator.hasNext()) {
                        result.add(fusionIterator.next());
                }

                assertEquals(Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9), result);
        }

        @Test
        public void testHasPreviousAndPrevious() {
                FusionSortedIterator<Integer> fusionIterator = new FusionSortedIterator<>(
                        Arrays.asList(iterator1, iterator2, iterator3)
                );

                // Traverse forward
                while (fusionIterator.hasNext()) {
                        fusionIterator.next();
                }

                ArrayList<Integer> result = new ArrayList<>();
                while (fusionIterator.hasPrevious()) {
                        result.add(fusionIterator.previous());
                }

                assertEquals(Arrays.asList(9, 8, 7, 6, 5, 4, 3, 2, 1), result);
        }

        @Test
        public void testGetNext() {
                FusionSortedIterator<Integer> fusionIterator = new FusionSortedIterator<>(
                        Arrays.asList(iterator1, iterator2, iterator3)
                );

                assertEquals(1, fusionIterator.getNext());
                assertEquals(1, fusionIterator.getNext()); // Should not move the cursor
        }

        @Test
        public void testGetPrevious() {
                FusionSortedIterator<Integer> fusionIterator = new FusionSortedIterator<>(
                        Arrays.asList(iterator1, iterator2, iterator3)
                );

                // Traverse forward
                while (fusionIterator.hasNext()) {
                        fusionIterator.next();
                }

                assertEquals(9, fusionIterator.getPrevious());
                assertEquals(9, fusionIterator.getPrevious()); // Should not move the cursor
        }

        @Test
        public void testComparator() {
                FusionSortedIterator<Integer> fusionIterator = new FusionSortedIterator<>(
                        Arrays.asList(iterator1, iterator2, iterator3)
                );

                assertNotNull(fusionIterator.comparator());
        }
}
