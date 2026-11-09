# Calculatrice scientifique V2

Application web pour effectuer des calculs simples et scientifiques, saisir des expressions avec des parenthèses et consulter les résultats de la session en cours.

Cette deuxième version reprend le premier projet avec une interface plus simple, un fond crème et des corrections du moteur de calcul.

![Aperçu de la calculatrice](docs/apercu.png)

## Fonctionnalités

- Effectuer des additions, soustractions, multiplications et divisions.
- Utiliser les parenthèses et respecter les priorités des opérations.
- Calculer un carré ou une racine carrée.
- Utiliser le sinus, le cosinus et la tangente en degrés.
- Calculer le logarithme décimal avec `log` et le logarithme népérien avec `ln`.
- Saisir une expression au clavier ou avec les boutons.
- Supprimer un caractère ou effacer toute la saisie.
- Consulter les 10 derniers calculs de la session et réutiliser un résultat.
- Commencer avec une saisie et un historique vides à chaque chargement de la page.
- Signaler les expressions incorrectes et les calculs impossibles dans la page.

## Technologies

- Java 17 ou ultérieur.
- Spring Boot 3.3 pour le serveur et l’API.
- Maven 3.6 ou ultérieur pour les dépendances, les tests et la compilation.
- HTML, CSS et JavaScript pour l’interface.
- JUnit 5 pour les tests unitaires.

Les dépendances Java sont téléchargées par Maven. Aucune bibliothèque JavaScript externe n’est nécessaire pour utiliser l’application.

## Installation et lancement

Installez un JDK et Maven, puis ouvrez un terminal :

```sh
git clone https://github.com/7amzvv/calculatrice-scientifique-v2.git
cd calculatrice-scientifique-v2
java -version
mvn -version
mvn spring-boot:run
```

Ouvrez ensuite http://localhost:8080 dans un navigateur. Le serveur doit rester lancé pour effectuer les calculs.

Sous Windows, si `java` ou `mvn` n’est pas reconnu, vérifiez leur installation et leur présence dans la variable `PATH`.

## Exemple d’utilisation

1. Saisissez `2+5*3`, puis appuyez sur Entrée : le résultat est `17`.
2. Saisissez `(2+5)*3` : le résultat est `21`.
3. Saisissez `sqrt(81)+3^2` : le résultat est `18`.
4. Essayez `sin(30)` : le résultat est proche de `0.5`.
5. Cliquez sur un résultat dans l’historique pour le réutiliser.
6. Rechargez la page : la saisie et l’historique sont de nouveau vides.

Le bouton `x²` insère `^2`. La touche xʸ n’est pas affichée ; les autres puissances restent possibles au clavier avec `^`. Utilisez `.` pour les décimales et `*` pour multiplier. Les fonctions s’écrivent avec des parenthèses.

Entrée lance le calcul. Échap efface la saisie. Le bouton « Effacer » vide l’historique de la session.

## Organisation des fichiers

```text
calculatrice-scientifique-v2/
├── src/
│   ├── main/
│   │   ├── java/com/calculatrice/
│   │   │   ├── CalculatriceApplication.java
│   │   │   ├── controller/
│   │   │   └── service/ServiceCalculatrice.java
│   │   └── resources/
│   │       ├── application.properties
│   │       └── static/
│   │           ├── index.html
│   │           ├── style.css
│   │           └── script.js
│   └── test/java/com/calculatrice/service/
│       └── ServiceCalculatriceTest.java
├── .github/workflows/verification.yml
├── docs/apercu.png
├── .gitignore
├── pom.xml
└── README.md
```

## Notions informatiques travaillées

- Séparation entre interface, contrôleur et service Java.
- Communication entre le navigateur et le serveur avec HTTP et JSON.
- Analyse récursive d’une expression mathématique.
- Gestion des parenthèses, des signes et des priorités des opérateurs.
- Validation des entrées et traitement des erreurs.
- Gestion des événements du clavier et des boutons en JavaScript.
- Conservation temporaire des résultats dans un tableau en mémoire.
- Tests unitaires avec JUnit et compilation avec Maven.

## Vérifications et limites

```sh
mvn verify
node --check src/main/resources/static/script.js
```

- Les 21 tests JUnit passent : opérations, priorités, puissances, fonctions scientifiques, notation scientifique et expressions invalides.
- La compilation et la création du fichier JAR avec Maven réussissent.
- La vérification syntaxique du JavaScript passe.
- L’application a été lancée et vérifiée dans le navigateur : calcul, présentation et historique vide après rechargement.
- Les calculs utilisent le type `double` et certains résultats sont approximatifs.
- Les fonctions trigonométriques utilisent les degrés, pas les radians.
- Les nombres complexes, les pourcentages et les multiplications implicites ne sont pas pris en charge.
- Une expression est limitée à 512 caractères après suppression des espaces.
- L’historique n’est pas enregistré : recharger ou fermer la page le réinitialise.

## Crédit

Projet réalisé par Fadli Hamza.
