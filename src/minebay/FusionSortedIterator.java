/**
 * @author Youcef GHEDAMSI 12302243
 * Je déclare qu'il s'agit de mon propre travail
 */

package minebay;


import java.util.LinkedList;
import java.util.Collection;
import java.util.Comparator;
import java.util.ListIterator;
import java.util.NoSuchElementException;

/**
 * Un ListIterator fusionnant plusieurs ListIterator ordonnés en interdisant les
 * opérations de modification add et set.
 * <p>
 * Un FusionSortedIterator garantie que, si les ListIterator fusionnés sont
 * ordonnés, alors ce FusionSortedIterator sera également ordonné.
 * <p>
 * Par défaut, l'ordre considéré est l'ordre naturel entre les éléments,
 * cependant un ordre alternatif peut-être spécifié à la création de l'instance.
 *
 * @param <E> le type des éléments énumérés par cet itérateur
 * @param <I> le type des itérateurs fusionnés
 * @author Marc Champesme
 * @version 8/12/2024
 * @model ListIterObserver<E> iterModel = new ListIterObserverAdapter<E>(this);
 * @invariant nextIndex() == previousIndex() + 1;
 * @invariant previousIndex() >= -1 && previousIndex() < iterModel.size());
 * @invariant nextIndex() >= 0 && nextIndex() <= iterModel.size());
 * @invariant !hasPrevious() <==> previousIndex() == -1;
 * @invariant !hasNext()() <==> nextIndex() == iterModel.size();
 * @invariant lastCalled() == nextIndex() || lastCalled() == previousIndex() ||
 * lastCalled() == -1;
 * @invariant lastCalled() >= -1 && lastCalled() < iterModel.size());
 * @invariant !iterModel.contains(null);
 * @invariant comparator() != null;
 * @invariant iterModel.isSorted(comparator ());
 * @since 2/08/2023
 */
public class FusionSortedIterator<E extends Comparable<? super E>> implements ListIterator<E> {

        private final LinkedList<ListIterator<E>> fusion;

        private final Comparator<? super E> cmp;

        // Variable permettant de savoir quelle methode entre next() et previous() a été appelé en dernier
        // Elle prevent 3 valeurs: 1 pour next() ; -1 pour previous et 0 si aucune des deux
        private int lastCalled;

        // Variable permettant de stocker le dernier iterator sur lequel on a appelé next() ou previous()
        private int lastCalledIter;

        /**
         * Initialise une instance permettant d'itérer selon l'ordre "naturel" sur tous
         * les éléments des ListIterator de la collection spécifiée. Il s'agit donc
         * d'une fusion de tous les ListIterator contenus dans la collection spécifiée.
         * Les ListIterator spécifiés sont supposés ordonnés selon l'ordre "naturel" de
         * leurs éléments.
         *
         * @param iters ensemble des ListIterator à fusionner
         * @throws NullPointerException si l'ensemble spécifié est null ou contient null
         * @requires iters != null && !iters.contains(null);
         * @ensures (\ forall ListIterator < E > iter ; iters.contains ( iter);
         * iterModel.containsAll(toList(iter)));
         * @ensures iterModel.size() == (\sum ListIterator<E> iter;
         * iters.contains(iter); size(iter));
         * @ensures (\ forall E p ; iterModel.contains ( p); (\exists ListIterator<E> iter;
         * iters.contains(iter); contains(iter, p)));
         * @ensures !hasPrevious();
         * @ensures lastCalled() == -1;
         * @ensures comparator() != null;
         */
        public FusionSortedIterator(Collection<? extends ListIterator<E>> iters) {
                this(iters, Comparator.naturalOrder());
        }

        /**
         * Initialise une instance permettant d'itérer sur tous les éléments des
         * ListIterator de la collection spécifiée selon l'ordre spécifié. Il s'agit
         * donc d'une fusion de tous les ListIterator contenus dans la collection
         * spécifiée. les ListIterator contenus dans la collection spécifiée sont
         * supposés ordonnés selon l'ordre induit par le Comparator spécifié.
         *
         * @param iters      collection des ListIterator à fusionner
         * @param comparator le comparateur à utiliser
         * @throws NullPointerException si l'ensemble spécifié est null ou contient
         *                              null, ou si le Comparator spécifié est null
         * @requires iters != null && !iters.contains(null);
         * @requires comparator != null;
         * @ensures comparator() != null;
         * @ensures comparator().equals(comparator);
         * @ensures !hasPrevious();
         * @ensures lastCalled() == -1;
         */
        public FusionSortedIterator(Collection<? extends ListIterator<E>> iters, Comparator<? super E> comparator) {

                if ((iters == null) || (comparator == null)) {
                        throw new NullPointerException();
                }

                cmp = comparator;

                fusion = new LinkedList<>();

                for (ListIterator<E> l : iters) {

                        if (l == null) {
                                throw new NullPointerException();
                        }

                        fusion.add(l);
                }

                lastCalled = 0;
                lastCalledIter = -1;
        }

        /**
         * Renvoie le comparateur selon lequel les éléments de cet itérateur sont
         * ordonnés.
         *
         * @return le comparateur selon lequel les éléments de cet itérateur sont
         * ordonnés
         * @ensures \result != null;
         * @pure
         */
        public Comparator<? super E> comparator() {
                return cmp;
        }

        /**
         * Renvoie true s'il reste un élément après dans l'itération.
         *
         * @return true s'il reste un élément après dans l'itération; false sinon
         * @ensures !\result <==> nextIndex() == iterModel.size();
         * @pure
         */
        @Override
        public boolean hasNext() {

                for (ListIterator<E> l : fusion) {
                        if (l.hasNext()) {
                                return true;
                        }
                }

                return false;
        }

        /**
         * Renvoie l'élèment qui sera renvoyé par le prochain appel à next().
         *
         * @return l'élèment qui sera renvoyé par le prochain appel à next()
         * @throws NoSuchElementException si l'itérateur n'a pas d'élément suivant
         * @requires hasNext();
         * @ensures \result.equals(iterModel.get(nextIndex()));
         * @pure
         */
        public E getNext() {

                if (!hasNext()) {
                        throw new NoSuchElementException();
                }

                E res = next();
                previous();
                return res;

        }

        /**
         * Renvoie l'élément suivant et avance le curseur.
         *
         * @return l'élément suivant
         * @throws NoSuchElementException si l'itérateur n'a pas d'élément suivant
         * @requires hasNext();
         * @ensures \result != null;
         * @ensures \result.equals(\old(getNext()));
         * @ensures \result.equals(getPrevious());
         * @ensures \result.equals(iterModel.get(previousIndex()))
         * @ensures \old(hasPrevious()) ==> comparator().compare(\old(getPrevious()),
         * \result) <= 0;
         * @ensures hasNext() ==> comparator().compare(\result, getNext()) <= 0;
         * @ensures hasPrevious();
         * @ensures previousIndex() == \old(nextIndex());
         * @ensures nextIndex() == \old(nextIndex() + 1);
         * @ensures lastCalled() == \old(nextIndex());
         * @ensures lastCalled() == previousIndex();
         */
        @Override
        public E next() {

                lastCalled = 1;

                if (!hasNext()) {
                        throw new NoSuchElementException();
                }

                int cpt = 0, minIter = 0;
                E minNext = null, tmpNext;

                for (ListIterator<E> l : fusion) {

                        if (l.hasNext()) {

                                tmpNext = l.next();

                                if (minNext == null) {
                                        minNext = tmpNext;
                                        minIter = cpt;
                                        continue;
                                }

                                if (cmp.compare(tmpNext, minNext) < 0) {
                                        fusion.get(minIter).previous();
                                        minNext = tmpNext;
                                        minIter = cpt;
                                        continue;
                                }

                                l.previous();

                        }

                        cpt++;

                }

                lastCalledIter = minIter;

                return minNext;
        }

        /**
         * Renvoie true s'il y a un élément précédent dans l'itération.
         *
         * @return true s'il y a un élément précédent dans l'itération; false sinon
         * @ensures !\result <==> previousIndex() == -1;
         * @pure
         */
        @Override
        public boolean hasPrevious() {

                for (ListIterator<E> l : fusion) {
                        if (l.hasPrevious()) {
                                return true;
                        }
                }

                return false;
        }

        /**
         * Renvoie l'élèment qui sera renvoyé par le prochain appel à previous().
         *
         * @return l'élèment qui sera renvoyé par le prochain appel à previous()
         * @throws NoSuchElementException si l'itérateur n'a pas d'élément précédent
         * @requires hasPrevious();
         * @ensures \result.equals(iterModel.get(previousIndex()));
         * @pure
         */
        public E getPrevious() {

                if (!hasPrevious()) {
                        throw new NoSuchElementException();
                }

                E res = previous();
                next();
                return res;

        }

        /**
         * Renvoie l'élément précédent et recule le curseur.
         *
         * @return l'élément précédent
         * @throws NoSuchElementException si l'itérateur n'a pas d'élément précédent
         * @requires hasPrevious();
         * @ensures hasNext();
         * @ensures \result != null;
         * @ensures \result.equals(\old(getPrevious()));
         * @ensures \result.equals(getNext());
         * @ensures \result.equals(\old(iterModel.get(previousIndex())));
         * @ensures \result.equals(iterModel.get(nextIndex()));
         * @ensures \old(hasNext()) ==> comparator().compare(\result,
         * iterModel.get(\old(nextIndex())) <= 0;
         * @ensures previousIndex() == \old(previousIndex()) - 1;
         * @ensures nextIndex() == \old(previousIndex());
         * @ensures lastCalled() == \old(previousIndex());
         * @ensures lastCalled() == nextIndex();
         */
        @Override
        public E previous() {

                lastCalled = -1;

                if (!hasPrevious()) {
                        throw new NoSuchElementException();
                }

                int cpt = 0, maxIter = 0;
                E maxPrevious = null, tmpPrevious;

                for (ListIterator<E> l : fusion) {

                        if (l.hasPrevious()) {

                                tmpPrevious = l.previous();

                                if (maxPrevious == null) {
                                        maxPrevious = tmpPrevious;
                                        maxIter = cpt;
                                        continue;
                                }

                                if (cmp.compare(tmpPrevious, maxPrevious) > 0) {
                                        fusion.get(maxIter).next();
                                        maxPrevious = tmpPrevious;
                                        maxIter = cpt;
                                        continue;
                                }

                                l.next();

                        }

                        cpt++;

                }

                lastCalledIter = maxIter;

                return maxPrevious;
        }

        /**
         * Renvoie l'index de l'élément suivant dans l'itération. Renvoie le nombre
         * total d'élément dans l'itération s'il n'y a pas d'élément suivant.
         *
         * @return l'index de l'élément suivant dans l'itération
         * @ensures hasNext() <==> \result >= 0 && \result < iterModel.size();
         * @ensures !hasNext() <==> \result == iterModel.size();
         * @pure
         */
        @Override
        public int nextIndex() {

                int res = 0;

                for (ListIterator<E> l : fusion) {
                        res += l.nextIndex();
                }

                return res;

        }

        /**
         * Renvoie l'index de l'élément précédent dans l'itération. Renvoie -1 s'il n'y
         * a pas d'élément précédent.
         *
         * @return l'index de l'élément précédent dans l'itération
         * @ensures hasPrevious() ==> \result >= 0;
         * @ensures !hasPrevious() <==> \result == -1;
         * @pure
         */
        @Override
        public int previousIndex() {
                return nextIndex() - 1;
        }

        /**
         * Renvoie l'index de l'élément renvoyé par le dernier appel à next() ou
         * previous(). Renvoie -1 si next() ou previous() n'ont jamais été appelés
         * depuis la création de cet itérateur ou bien si remove a été appelée depuis le
         * dernier appel à next ou previous.
         *
         * @return l'index de l'élément renvoyé par le dernier appel à next() ou
         * previous()
         * @ensures \result >= -1 && \result < iterModel.size();
         * @pure
         */
        public int lastIndex() {

                return switch (lastCalled) {
                        case 1 -> previousIndex();
                        case -1 -> nextIndex();
                        default -> -1;
                };
        }

        /**
         * Retire de l'itération le dernier élèment renvoyé par next() ou previous().
         * L'élément retiré est l'élèment renvoyé par le dernier appel à next() ou
         * previous(). Ne peut être appelé qu'une fois par appel à next ou previous.
         *
         * @throws IllegalStateException si next ou previous n'ont jamais été appelés,
         *                               ou bien si remove a déjà été appelé depuis le
         *                               dernier appel a next ou remove
         * @requires lastCalled() != -1;
         * @ensures iterModel.size() == \old(iterModel.size()) - 1;
         * @ensures (* if last move is previous : *) <br />
         * \old(lastCalled() == nextIndex()) ==> (previousIndex() ==
         * \old(previousIndex()));
         * @ensures (* if last move is next : *) <br />
         * \old(lastCalled() == previousIndex()) ==> (previousIndex() ==
         * \old(previousIndex()) - 1);
         * @ensures lastCalled() == -1;
         */
        @Override
        public void remove() {

                if (lastCalled == 0 || lastCalledIter == -1) {
                        throw new IllegalStateException();
                }

                fusion.get(lastCalledIter).remove();
                lastCalled = 0;
                lastCalledIter = -1;
        }

        /**
         * Opération non supportée.
         *
         * @throws UnsupportedOperationException toujours
         */
        @Override
        public void set(E e) {
                throw new UnsupportedOperationException();
        }

        /**
         * Opération non supportée.
         *
         * @throws UnsupportedOperationException toujours
         */
        @Override
        public void add(E e) {
                throw new UnsupportedOperationException();
        }

}
