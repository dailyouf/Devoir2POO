/**
 * @author Youcef GHEDAMSI 12302243
 * Je déclare qu'il s'agit de mon propre travail
 */

package minebay;


import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.ListIterator;
import java.util.NoSuchElementException;

/**
 * Un ListIterator fusionnant plusieurs ListIterator ordonnés en interdisant les
 * opérations de modification add et set.
 *
 * Un FusionSortedIterator garantie que, si les ListIterator fusionnés sont
 * ordonnés, alors ce FusionSortedIterator sera également ordonné.
 *
 * Par défaut, l'ordre considéré est l'ordre naturel entre les éléments,
 * cependant un ordre alternatif peut-être spécifié à la création de l'instance.
 * 
 * @param <E> le type des éléments énumérés par cet itérateur
 * @param <I> le type des itérateurs fusionnés
 *
 * @model ListIterObserver<E> iterModel = new ListIterObserverAdapter<E>(this);
 * @invariant nextIndex() == previousIndex() + 1;
 * @invariant previousIndex() >= -1 && previousIndex() < iterModel.size());
 * @invariant nextIndex() >= 0 && nextIndex() <= iterModel.size());
 * @invariant !hasPrevious() <==> previousIndex() == -1;
 * @invariant !hasNext()() <==> nextIndex() == iterModel.size();
 * @invariant lastIndex() == nextIndex() || lastIndex() == previousIndex() ||
 *            lastIndex() == -1;
 * @invariant lastIndex() >= -1 && lastIndex() < iterModel.size());
 * @invariant !iterModel.contains(null);
 * @invariant comparator() != null;
 * @invariant iterModel.isSorted(comparator());
 * 
 * @author Marc Champesme
 * @since 2/08/2023
 * @version 8/12/2024
 *
 */
public class FusionSortedIterator<E extends Comparable<? super E>> implements ListIterator<E> {

	private ArrayList<ListIterator<E>> fusion;

	private Comparator<? super E> cmp;

	/**
	 * Initialise une instance permettant d'itérer selon l'ordre "naturel" sur tous
	 * les éléments des ListIterator de la collection spécifiée. Il s'agit donc
	 * d'une fusion de tous les ListIterator contenus dans la collection spécifiée.
	 * Les ListIterator spécifiés sont supposés ordonnés selon l'ordre "naturel" de
	 * leurs éléments.
	 *
	 * @param iters ensemble des ListIterator à fusionner
	 *
	 * @requires iters != null && !iters.contains(null);
	 * @ensures (\forall ListIterator<E> iter; iters.contains(iter);
	 *          iterModel.containsAll(toList(iter)));
	 * @ensures iterModel.size() == (\sum ListIterator<E> iter;
	 *          iters.contains(iter); size(iter));
	 * @ensures (\forall E p; iterModel.contains(p); (\exists ListIterator<E> iter;
	 *          iters.contains(iter); contains(iter, p)));
	 * @ensures !hasPrevious();
	 * @ensures lastIndex() == -1;
	 * @ensures comparator() != null;
	 *
	 * @throws NullPointerException si l'ensemble spécifié est null ou contient null
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
	 *
	 * @param iters      collection des ListIterator à fusionner
	 * @param comparator le comparateur à utiliser
	 *
	 * @requires iters != null && !iters.contains(null);
	 * @requires comparator != null;
	 * @ensures comparator() != null;
	 * @ensures comparator().equals(comparator);
	 * @ensures !hasPrevious();
	 * @ensures lastIndex() == -1;
	 *
	 * @throws NullPointerException si l'ensemble spécifié est null ou contient
	 *                              null, ou si le Comparator spécifié est null
	 */
	public FusionSortedIterator(Collection<? extends ListIterator<E>> iters, Comparator<? super E> comparator) {

		if ((iters == null) || (comparator == null)) {
			throw new NullPointerException();
		}

		cmp = comparator;

		fusion = new ArrayList<>();

		for (ListIterator<E> l : iters) {

			if (l == null) {
				throw new NullPointerException();
			}

			fusion.add(l);
		}
	}

	/**
	 * Renvoie le comparateur selon lequel les éléments de cet itérateur sont
	 * ordonnés.
	 * 
	 * @return le comparateur selon lequel les éléments de cet itérateur sont
	 *         ordonnés
	 * 
	 * @ensures \result != null;
	 * 
	 * @pure
	 */
	public Comparator<? super E> comparator() {
		return cmp;
	}

	/**
	 * Renvoie true s'il reste un élément après dans l'itération.
	 * 
	 * @return true s'il reste un élément après dans l'itération; false sinon
	 * 
	 * @ensures !\result <==> nextIndex() == iterModel.size();
	 * 
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
	 * 
	 * @throws NoSuchElementException si l'itérateur n'a pas d'élément suivant
	 * 
	 * @requires hasNext();
	 * @ensures \result.equals(iterModel.get(nextIndex()));
	 * 
	 * @pure
	 */
	public E getNext() {

		if (! hasNext()) {
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
	 *
	 * @throws NoSuchElementException si l'itérateur n'a pas d'élément suivant
	 *
	 * @requires hasNext();
	 * @ensures \result != null;
	 * @ensures \result.equals(\old(getNext()));
	 * @ensures \result.equals(getPrevious());
	 * @ensures \result.equals(iterModel.get(previousIndex()))
	 * @ensures \old(hasPrevious()) ==> comparator().compare(\old(getPrevious()),
	 *          \result) <= 0;
	 * @ensures hasNext() ==> comparator().compare(\result, getNext()) <= 0;
	 * @ensures hasPrevious();
	 * @ensures previousIndex() == \old(nextIndex());
	 * @ensures nextIndex() == \old(nextIndex() + 1);
	 * @ensures lastIndex() == \old(nextIndex());
	 * @ensures lastIndex() == previousIndex();
	 */
	@Override
	public E next() {

		if (! hasNext()) {
			throw new NoSuchElementException();
		}

		return null;
	}

	/**
	 * Renvoie true s'il y a un élément précédent dans l'itération.
	 * 
	 * @return true s'il y a un élément précédent dans l'itération; false sinon
	 * 
	 * @ensures !\result <==> previousIndex() == -1;
	 *
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
	 * 
	 * @throws NoSuchElementException si l'itérateur n'a pas d'élément précédent
	 * 
	 * @requires hasPrevious();
	 * @ensures \result.equals(iterModel.get(previousIndex()));
	 * 
	 * @pure
	 */
	public E getPrevious() {

		if (! hasPrevious()) {
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
	 *
	 * @throws NoSuchElementException si l'itérateur n'a pas d'élément précédent
	 *
	 * @requires hasPrevious();
	 * @ensures hasNext();
	 * @ensures \result != null;
	 * @ensures \result.equals(\old(getPrevious()));
	 * @ensures \result.equals(getNext());
	 * @ensures \result.equals(\old(iterModel.get(previousIndex())));
	 * @ensures \result.equals(iterModel.get(nextIndex()));
	 * @ensures \old(hasNext()) ==> comparator().compare(\result,
	 *          iterModel.get(\old(nextIndex())) <= 0;
	 * @ensures previousIndex() == \old(previousIndex()) - 1;
	 * @ensures nextIndex() == \old(previousIndex());
	 * @ensures lastIndex() == \old(previousIndex());
	 * @ensures lastIndex() == nextIndex();
	 */
	@Override
	public E previous() {
		return null;
	}

	/**
	 * Renvoie l'index de l'élément suivant dans l'itération. Renvoie le nombre
	 * total d'élément dans l'itération s'il n'y a pas d'élément suivant.
	 * 
	 * @return l'index de l'élément suivant dans l'itération
	 * 
	 * @ensures hasNext() <==> \result >= 0 && \result < iterModel.size();
	 * @ensures !hasNext() <==> \result == iterModel.size();
	 * 
	 * @pure
	 */
	@Override
	public int nextIndex() {
		return 0;
	}

	/**
	 * Renvoie l'index de l'élément précédent dans l'itération. Renvoie -1 s'il n'y
	 * a pas d'élément précédent.
	 * 
	 * @return l'index de l'élément précédent dans l'itération
	 * 
	 * @ensures hasPrevious() ==> \result >= 0;
	 * @ensures !hasPrevious() <==> \result == -1;
	 *
	 * @pure
	 */
	@Override
	public int previousIndex() {
		return 0;
	}

	/**
	 * Renvoie l'index de l'élément renvoyé par le dernier appel à next() ou
	 * previous(). Renvoie -1 si next() ou previous() n'ont jamais été appelés
	 * depuis la création de cet itérateur ou bien si remove a été appelée depuis le
	 * dernier appel à next ou previous.
	 * 
	 * @return l'index de l'élément renvoyé par le dernier appel à next() ou
	 *         previous()
	 * 
	 * @ensures \result >= -1 && \result < iterModel.size();
	 * 
	 * @pure
	 */
	public int lastIndex() {
		return 0;
	}

	/**
	 * Retire de l'itération le dernier élèment renvoyé par next() ou previous().
	 * L'élément retiré est l'élèment renvoyé par le dernier appel à next() ou
	 * previous(). Ne peut être appelé qu'une fois par appel à next ou previous.
	 * 
	 * @throws IllegalStateException si next ou previous n'ont jamais été appelés,
	 *                               ou bien si remove a déjà été appelé depuis le
	 *                               dernier appel a next ou remove
	 * 
	 * @requires lastIndex() != -1;
	 * @ensures iterModel.size() == \old(iterModel.size()) - 1;
	 * @ensures (* if last move is previous: *) <br />
	 *          \old(lastIndex() == nextIndex()) ==> (previousIndex() ==
	 *          \old(previousIndex()));
	 * @ensures (* if last move is next: *) <br />
	 *          \old(lastIndex() == previousIndex()) ==> (previousIndex() ==
	 *          \old(previousIndex()) - 1);
	 * @ensures lastIndex() == -1;
	 */
	@Override
	public void remove() {
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
