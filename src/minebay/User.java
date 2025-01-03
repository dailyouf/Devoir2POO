/**
 * @author Youcef GHEDAMSI 12302243
 * Je déclare qu'il s'agit de mon propre travail
 */

package minebay;

import java.time.Instant;
import java.util.Collections;
import java.util.EnumSet;
import java.util.ListIterator;
import java.util.Set;

/**
 * Un utilisateur de l'application MinEbay.
 *
 * <p>
 * Un User est caractérisé par:
 * <ul>
 * <li>son nom et son mot de passe</li>
 * <li>le montant disponible sur son compte bancaire</li>
 * <li>sa date d'inscription sur MinEbay (= date de création de l'instance de
 * l'User)</li>
 * <li>la liste des annonces qu'il a posté et pour lesquelles il n'a pas trouvé
 * d'acheteur (annonces dans l'état OPEN)</li>
 * <li>la liste des annonces qu'il a posté et pour lesquelles il a vendu l'objet
 * concerné (annonces dans l'état CLOSED)</li>
 * <li>la liste des annonces (d'autres utilisateurs) pour lesquelles il a acheté
 * l'objet concerné (annonces dans l'état PURCHASE)</li>
 * </ul>
 * </p>
 *
 * <p>
 * Chacune des trois listes d'annonces d'un User est représentée en utilisant
 * une instance de MultiEnumList. Cela permet d'effectuer des itérations portant
 * uniquement sur des annonces appartenant à un sous-ensemble de catégories,
 * sans qu'il soit besoin de créer des listes intermédiaires contenant ces
 * annonces. Comme pour les MultiEnumList, il est fortement déconseillé
 * d'utiliser les méthodes get pour effectuer un parcours de liste, l'usage d'un
 * itérateur est hautement préférable.
 * </p>
 *
 * <p>
 * Les méthodes ne possédant pas de paramètre de type AdState ou Set<AdCategory>
 * agissent uniquement sur les annonces dont l'état et la catégorie a été
 * sélectionnée.
 * </p>
 *
 * @author Marc Champesme
 * @version 26/11/2024
 * @invariant getName() != null && !getName().isBlank();
 * @invariant getPassword() != null && !getPassword().isBlank();
 * @invariant getRegistrationDate() != null;
 * @invariant getAvailableCash() >= 0;
 * @invariant (\ forall AdState state ; true ; ! contains ( state, null));
 * @invariant (\ forall int i, j ; i > = 0 & & i < j & & j < size ();
 * get(i).isAfter(get(j)));
 * @invariant (\ forall AdState state, AdCategory cat ; true ; < br / >
 *( \ forall int i, j ; < br / >
 *i > = 0 & & i < j & & j < size ( state, EnumSet.of ( cat)); get(state,
 * EnumSet.of(cat), i).isAfter(get(state, EnumSet.of(cat), j))));
 * @since 27/09/2024
 */
@SuppressWarnings("FieldMayBeFinal")
public class User implements Iterable<ClassifiedAd> {

        public static final int DEFAULT_CASH_AMMOUNT = 100;

        private final String name, pswd;
        private int budget;
        private final Instant date;

        private MultiEnumList<AdCategory, ClassifiedAd> open;
        private MultiEnumList<AdCategory, ClassifiedAd> closed;
        private MultiEnumList<AdCategory, ClassifiedAd> purchaces;

        private AdState state;
        private Set<AdCategory> cats;

        /**
         * Initialise une nouvelle instance ayant les nom et mot de passe spécifiés. La
         * date d'inscription du nouvel utilisateur est la date au moment de l'exécution
         * de ce constructeur. L'état sélectionné est OPEN et toutes les catégories sont
         * sélectionnées.
         *
         * @param userName nom de la nouvelle instance de User
         * @param password mot de passe de la nouvelle instance de User
         * @throws NullPointerException     si l'un des arguments spécifiés est null
         * @throws IllegalArgumentException si l'un des arguments spécifiés ne contient
         *                                  que des caractères blancs ou ne contient
         *                                  aucun caractères
         * @requires userName != null && !userName.isBlank();
         * @requires password != null && !password.isBlank();
         * @ensures getName().equals(userName);
         * @ensures getPassword().equals(password);
         * @ensures getRegistrationDate() != null;
         * @ensures \old(Instant.now()).isBefore(getRegistrationDate());
         * @ensures getRegistrationDate().isBefore(Instant.now ());
         * @ensures getAvailableCash() = DEFAULT_CASH_AMMOUNT;
         * @ensures getSelectedAdState().equals(OPEN);
         * @ensures getSelectedCategories().equals(EnumSet.allOf ( AdCategory.class));
         */
        public User(String userName, String password) {

                if ((userName == null) || (password == null)) {
                        throw new NullPointerException();
                }

                if ((userName.isBlank()) || (password.isBlank())) {
                        throw new IllegalArgumentException();
                }

                name = userName;
                pswd = password;

                date = Instant.now();

                budget = DEFAULT_CASH_AMMOUNT;

                open = new MultiEnumList<>(AdCategory.class);
                closed = new MultiEnumList<>(AdCategory.class);
                purchaces = new MultiEnumList<>(AdCategory.class);

                state = AdState.OPEN;
                cats = EnumSet.allOf(AdCategory.class);
        }

        /**
         * Renvoie l'état actuellement sélectionné pour les annonces.
         *
         * @return l'état actuellement sélectionné pour les annonces
         * @ensures \result != null;
         * @pure
         */
        public AdState getSelectedAdState() {
                return state;
        }

        /**
         * Sélectionne l'état spécifié.
         *
         * @param state état à sélectionner pour les annonces
         * @throws NullPointerException si l'état spécifié est null
         * @requires state != null;
         * @ensures getSelectedAdState().equals(state);
         * @ensures getSelectedCategory().equals(\ old ( getSelectedCategory ()));
         */
        public void selectAdState(AdState state) {

                if (state == null) {
                        throw new NullPointerException();
                }

                this.state = state;
        }

        /**
         * Ajoute la catégorie spécifiée à l'ensemble des catégories sélectionnées.
         *
         * @param cat catégorie à ajouter aux catégories sélectionnées pour les annonces
         * @return true si l'ensemble des catégories sélectionnées a changé; false sinon
         * @throws NullPointerException si la catégorie spécifiée est null
         * @requires cat != null;
         * @ensures !getSelectedCategories().isEmpty();
         * @ensures getSelectedCategories().contains(cat);
         * @ensures \result <==> !\old(getSelectedCategories().contains(cat));
         * @ensures !\result ==>
         * getSelectedCategories().equals(\old(getSelectedCategories()));
         */
        public boolean addSelectedCategory(AdCategory cat) {

                if (cat == null) {
                        throw new NullPointerException();
                }

                return cats.add(cat);
        }

        /**
         * Retire la catégorie spécifiée de l'ensemble des catégories sélectionnées.
         *
         * @param cat catégorie à retirer des catégories sélectionnées pour les annonces
         * @return true si l'ensemble des catégories sélectionnées a changé; false sinon
         * @ensures !getSelectedCategories().contains(cat);
         * @ensures \result <==> \old(getSelectedCategories().contains(cat));
         * @ensures !\result ==>
         * getSelectedCategories().equals(\old(getSelectedCategories()));
         */
        public boolean removeSelectedCategory(Object obj) {
                //noinspection SuspiciousMethodCalls
                return cats.remove(obj);
        }

        /**
         * Renvoie une vue non modifiable de l'ensemble des catégories actuellement
         * sélectionnées. Si aucune catégorie n'est sélectionnée, renvoie un ensemble
         * vide.
         *
         * @return un ensemble contenant les catégories actuellement sélectionnées
         * @ensures \result != null;
         * @pure
         */
        public Set<AdCategory> getSelectedCategories() {
                return Collections.unmodifiableSet(cats);
        }

        /**
         * Déselectionne toutes les catégories précédemment sélectionnées.
         *
         * @return true si l'ensemble des catégories sélectionnées a changé; false sinon
         * @ensures getSelectedCategories().isEmpty();
         */
        public boolean clearSelectedCategories() {

                if (cats.isEmpty()) {
                        return false;
                }

                cats.clear();

                return true;
        }

        /**
         * Sélectionne toutes les catégories.
         *
         * @return true si l'ensemble des catégories sélectionnées a changé; false sinon
         * @ensures getSelectedCategories().equals(EnumSet.allOf ( AdCategory.class));
         */
        public boolean selectAllCategories() {

                if (cats.containsAll(EnumSet.allOf(AdCategory.class))) {
                        return false;
                }

                cats = EnumSet.allOf(AdCategory.class);

                return true;
        }

        /**
         * Renvoie le nom de cet utilisateur.
         *
         * @return le nom de cet utilisateur
         * @pure
         */
        public String getName() {
                return name;
        }

        /**
         * Renvoie le mot de passe de cet utilisateur.
         *
         * @return le mot de passe de cet utilisateur
         * @pure
         */
        public String getPassword() {
                return pswd;
        }

        /**
         * Renvoie l'Instant représentant la date d'inscription de cet utilisateur.
         *
         * @return la date d'inscription de cet utilisateur
         * @pure
         */
        public Instant getRegistrationDate() {
                return date;
        }

        /**
         * Renvoie la somme d'argent disponible pour cet utilisateur pour acheter des
         * objets.
         *
         * @return la somme d'argent disponible pour cet utilisateur
         * @pure
         */
        public int getAvailableCash() {
                return budget;
        }

        /**
         * Achète à l'utilisateur spécifié l'objet présenté dans l'annonce spécifiée.
         * Pour le vendeur l'annonce passe de la liste des annonces OPEN à la liste des
         * annonces CLOSED et il reçoit le montant du prix de l'objet présenté dans
         * l'annonce. Pour ce User, l'annonce est ajoutée à la liste des annonces
         * PURCHASE et sa somme d'argent disponible est diminuée du prix de l'objet.
         *
         * @param vendor l'utilisateur auteur de l'annonce spécifiée
         * @param ad     l'annonce de l'objet à acheter
         * @throws NullPointerException     si un des deux arguments spécifié est null
         * @throws IllegalArgumentException si l'annonce spécifiée n'est pas une annonce
         *                                  OPEN du vendeur spécifié ou si le vendeur
         *                                  spécifié est ce User
         * @throws IllegalStateException    si l'argent disponible de ce User est
         *                                  inférieur au prix de l'annonce spécifiée
         * @requires vendor != null;
         * @requires this != vendor;
         * @requires ad != null;
         * @requires vendor.containsInState(OPEN, ad);
         * @requires getAvailableCash() >= ad.getPrice();
         * @ensures containsInState(PURCHASE, ad);
         * @ensures size(PURCHASE, EnumSet.of ( ad.getCategory ()))<br/>
         * == \old(size(PURCHASE, EnumSet.of(ad.getCategory()))) + 1;
         * @ensures !vendor.containsInState(OPEN, ad);
         * @ensures vendor.size(OPEN, EnumSet.of ( ad.getCategory ())) ==
         * \old(vendor.size(OPEN, EnumSet.of(ad.getCategory()))) - 1;
         * @ensures vendor.containsInState(CLOSED, ad);
         * @ensures vendor.size(CLOSED, EnumSet.of ( ad.getCategory ())) ==
         * \old(vendor.size(CLOSED, EnumSet.of(ad.getCategory()))) + 1;
         * @ensures getAvailableCash() == \old(getAvailableCash()) - ad.getPrice();
         * @ensures vendor.getAvailableCash() == \old(vendor.getAvailableCash()) +
         * ad.getPrice();
         */
        public void buy(User vendor, ClassifiedAd ad) {

                if ((vendor == null) || (ad == null)) {
                        throw new NullPointerException();
                }

                if ((!vendor.containsInState(AdState.OPEN, ad)) || (vendor == this)) {
                        throw new IllegalArgumentException();
                }

                if (budget < ad.getPrice()) {
                        throw new IllegalStateException();
                }

                purchaces.add(ad);
                budget -= ad.getPrice();

                vendor.open.remove(ad);
                vendor.closed.add(ad);
                vendor.budget += ad.getPrice();

        }

        /**
         * Crée et renvoie une nouvelle instance de ClassifiedAd dont les
         * caractéristiques sont les arguments spécifiés et l'ajoute à la liste des
         * annonces ouvertes (open ads) de cet utilisateur. La date de cette annonce est
         * la date d'exécution de cette méthode.
         *
         * @param cat   la catégorie de la nouvelle annonce
         * @param msg   la description de la nouvelle annonce
         * @param price le prix de l'objet décrit dans la nouvelle annonce
         * @return la nouvelle annonce de cet utilisateur
         * @throws NullPointerException     si la catégorie ou la description sont null
         * @throws IllegalArgumentException si le prix spécifié est inférieur ou égal à
         *                                  0 ou la description de l'objet ne contient
         *                                  pas de caractères
         * @old oldDate = Instant.now();
         * @requires cat != null;
         * @requires msg != null;
         * @requires !msg.isBlank()
         * @requires price > 0;
         * @ensures get(OPEN, EnumSet.of ( cat), 0).equals(\result);
         * @ensures \result.getCategory().equals(cat);
         * @ensures \result.getDescription().equals(msg);
         * @ensures \result.getPrice() == price;
         * @ensures size(OPEN, EnumSet.of ( cat)) == \old(size(OPEN, EnumSet.of(cat))) +
         * 1;
         * @ensures oldDate.isBefore(\ result.getDate ());
         * @ensures \result.getDate().isBefore(Instant.now());
         */
        public ClassifiedAd add(AdCategory cat, String msg, int price) {

                if ((cat == null) || (msg == null)) throw new NullPointerException();

                if ((price <= 0) || (msg.isBlank())) {
                        throw new IllegalArgumentException();
                }


                ClassifiedAd ad = new ClassifiedAd(cat, msg, price);

                open.add(ad);

                return ad;
        }

        /**
         * Renvoie le nombre d'annonces de cet utilisateur dans l'état sélectionné
         * (open, closed ou purchase) et appartenant à une des catégories sélectionnées
         * seules les annonces de cette catégorie sont comptées.
         *
         * @return le nombre d'annonces de cet utilisateur dans l'état et la catégorie
         * sélectionnés.
         * @ensures \result == size(getSelectedAdState(), getSelectedCategories());
         * @ensures getSelectedCategories().isEmpty() ==> \result == 0;
         * @pure
         */
        public int size() {
                return size(state, cats);
        }

        /**
         * Renvoie le nombre d'annonces de cet utilisateur dans l'état spécifié et
         * appartenant à une des catégories appartenant à l'ensemble spécifié.
         *
         * @param state  état des annonces
         * @param catSet ensemble de catégories d'annonces
         * @return le nombre d'annonces de cet utilisateur dans l'état spécifié et
         * appartenant à une des catégories appartenant à l'ensemble spécifié
         * @throws NullPointerException si l'un des paramètres est null
         * @requires state != null;
         * @requires catSet != null;
         * @requires !catSet.contains(null)
         * @ensures \result >= 0;
         * @ensures getSelectedAdState().equals(state) &&
         * getSelectedCategories().equals(catSet) <br/>
         * ==> \result == size();
         * @ensures catSet.isEmpty() ==> \result == 0;
         * @pure
         */
        public int size(AdState state, Set<AdCategory> catSet) {

                if ((state == null) || (catSet == null)) {
                        throw new NullPointerException();
                }

                if (state == AdState.OPEN) {
                        return open.size(catSet);
                } else if (state == AdState.CLOSED) {
                        return closed.size(catSet);
                } else if (state == AdState.PURCHASE) {
                        return purchaces.size(catSet);
                }
                return 0;


        }

        /**
         * Renvoie la ième plus récente annonce de ce User dans l'état sélectionné et
         * dont la catégorie appartient à l'ensemble de catégories sélectionnées.
         *
         * @param i index de l'annonce cherchée
         * @return la ième plus récente annonce de ce User
         * @throws IndexOutOfBoundsException si l'index spécifié est strictement
         *                                   inférieur à 0 ou supérieur ou égal à size()
         * @requires i >= 0 && i < size();
         * @ensures \result != null;
         * @ensures \result.equals(get(getSelectedAdState(), getSelectedCategories(),
         * i);
         * @pure
         */
        public ClassifiedAd get(int i) {
                return get(state, cats, i);
        }

        /**
         * Renvoie le ième élément de la liste des annonces étant dans l'état spécifié
         * et dont la catégorie appartient à l'ensemble de catégories spécifié.
         *
         * @param state  l'état des annonces
         * @param catSet ensemble des catégories d'annonces concernées
         * @param i      index de l'élément dans la liste
         * @return le ième élément de la liste des annonces étant dans l'état spécifié
         * et dont la catégorie appartient à l'ensemble de catégories spécifié
         * @throws NullPointerException      si l'état spécifié ou l'ensemble de
         *                                   catégories spécifié est null
         * @throws IndexOutOfBoundsException si l'index spécifié est strictement
         *                                   inférieur à 0 ou supérieur ou égal à
         *                                   size(state, catSet)
         * @requires state != null;
         * @requires catSet != null;
         * @requires !catSet.contains(null)
         * @requires i >= 0 && i < size(state, catSet);
         * @ensures \result != null;
         * @ensures containsInState(state, \ result);
         * @ensures catSet.contains(\ result.getCategory ());
         * @pure
         */
        public ClassifiedAd get(AdState state, Set<AdCategory> catSet, int i) {

                if ((state == null) || (catSet == null)) {
                        throw new NullPointerException();
                }

                if (state == AdState.OPEN) {
                        return open.get(catSet, i);
                } else if (state == AdState.CLOSED) {
                        return closed.get(catSet, i);
                } else if (state == AdState.PURCHASE) {
                        return purchaces.get(catSet, i);
                }
                return null;

        }

        /**
         * Renvoie true si ce User possède parmi les annonces dans l'état spécifié,
         * l'objet spécifié.
         *
         * @param state état des annonces parmi lesquelles l'objet est recherché
         * @param obj   objet cherchée
         * @return true si ce User possède parmi les annonces dans l'état spécifié,
         * l'objet spécifié; false sinon
         * @requires state != null;
         * @ensures !(obj instanceof ClassifiedAd) ==> !\result;
         * @ensures \result <==> (\exists int i; <br/>
         * i >= 0 && i < size(state, EnumSet.allOf(AdCategory.class)); <br/>
         * get(state, EnumSet.allOf(AdCategory.class), i).equals(obj));
         * @pure
         */
        public boolean containsInState(AdState state, Object obj) {

                if (state == null) {
                        throw new NullPointerException();
                }

                if (state == AdState.OPEN) {
                        //noinspection SuspiciousMethodCalls
                        return open.contains(obj);
                } else if (state == AdState.CLOSED) {
                        //noinspection SuspiciousMethodCalls
                        return closed.contains(obj);
                } else if (state == AdState.PURCHASE) {
                        //noinspection SuspiciousMethodCalls
                        return purchaces.contains(obj);
                }
                return false;
        }

        /**
         * Renvoie un itérateur sur les annonces dans l'état sélectionné et appartenant
         * à une des catégories sélectionnées.
         *
         * @return un itérateur sur les annonces dans l'état sélectionné et appartenant
         * à une des catégories sélectionnées
         * @ensures \result != null;
         * @ensures size() > 0 ==> \result.hasNext();
         * @pure
         */
        public ListIterator<ClassifiedAd> iterator() {
                return iterator(state, cats);
        }

        /**
         * Renvoie un itérateur sur les annonces dans l'état spécifié et dont la
         * catégories appartient à l'ensemble spécifié.
         *
         * @return un itérateur sur les annonces dans l'état spécifié et dont la
         * catégories appartient à l'ensemble spécifié
         * @throws NullPointerException si l'état spécifié ou l'ensemble de catégories
         *                              spécifié est null ou si l'ensemble de états spécifié est null ou contient null
         * @requires state != null
         * @requires catSet != null
         * @requires !catSet.contains(null)
         * @ensures \result != null;
         * @ensures size(state, catSet) > 0 ==> \result.hasNext();
         * @pure
         */
        public ListIterator<ClassifiedAd> iterator(AdState state, Set<AdCategory> catSet) {

                if ((state == null) || (catSet == null)) {
                        throw new NullPointerException();
                }

                if (state == AdState.OPEN) {
                        return open.listIterator(catSet);
                } else if (state == AdState.CLOSED) {
                        return closed.listIterator(catSet);
                } else if (state == AdState.PURCHASE) {
                        return purchaces.listIterator(catSet);
                }
                return null;

        }

        /**
         * Renvoie une chaîne de caractères contenant le nom de ce User ainsi que le
         * nombre d'annonces de cet utilisateur dans les trois états possibles (OPEN,
         * CLOSED, PURCHASE).
         *
         * @return une chaîne de caractères contenant le nom de ce User ainsi que le
         * nombre d'annonces de cet utilisateur
         * @ensures \result != null;
         * @ensures \result.contains(getName());
         * @ensures \result.contains("" + size(OPEN, EnumSet.allOf(AdCategory.class)));
         * @ensures \result.contains("" + size(CLOSED, EnumSet.allOf(AdCategory.class)));
         * @ensures \result.contains("" + size(PURCHASE, EnumSet.allOf(AdCategory.class)));
         * @pure
         */
        @Override
        public String toString() {
                return "L'utilisateur " + name + " a : " + open.size() + " OPEN ads, " + closed.size() +
                        " CLOSED ads and " + purchaces.size() + "PURCHASE ads \n";
        }
}
