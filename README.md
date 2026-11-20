# Happy Hour

Jeu de gestion de bar en 2D, développé en Java avec JavaFX dans le cadre d'un projet de programmation orientée objet à Epitech Marseille.

Le joueur incarne **Melvin**, barman d'un bar chic. Pendant une soirée de 5 minutes, il doit préparer et servir les boissons de clients de plus en plus nombreux et exigeants, encaisser, et expulser d'un coup de pied les clients indésirables, surtout les enfants déguisés qui se glissent au comptoir.

**But du jeu** : finir la soirée avec le meilleur score possible (chiffre d'affaires + pourboires).

## Lancer le jeu

### Prérequis

- Java 17 ou plus récent
- Maven

### Commandes

```bash
git clone <url du dépôt>
cd <dossier du projet>
mvn javafx:run
```

## Comment jouer

### Contrôles

| Action | Contrôle |
| --- | --- |
| Prendre un verre, un ingrédient, la cuillère ou le shaker | Clic gauche sur le poste |
| Servir un client | Clic gauche sur le client |
| Expulser un client (coup de pied) | Clic droit sur le client |
| Ouvrir ou fermer le livre de recettes | Icône de livre, en haut à gauche |
| Réglages | Icône d'engrenage, en haut à droite |
| Pause | Barre Espace |

### Préparer une boisson

1. Prendre le **verre** en premier (sans verre, les autres postes ne font rien).
2. Ajouter les ingrédients **dans l'ordre de la recette** : alcool, sirop, jus ou soda, glaçons, garnish.
3. Pour un cocktail : utiliser la **cuillère** (stirred) ou le **shaker** (shaken).
4. Cliquer sur le client pour le servir.

Les recettes sont dans le livre de recettes. Chaque boisson est notée sur 10 (ingrédients, ordre, verre, technique) et le client paie **prix × note / 10**, plus un pourboire qui dépend de la note, de la rapidité et du type de client.

### Les clients

| Client | Particularité |
| --- | --- |
| Normal | Aucune |
| Pressé | Patience courte, bonus vitesse doublé |
| Difficile | Commande un cocktail shaken, refuse toute boisson notée moins de 8/10 |
| Généreux | Patience longue, pourboire doublé |
| Enfant | Ne doit **jamais** être servi : il faut l'expulser, sinon c'est la descente de police |

### Fin de partie

- **Fin normale** : le bar ferme au bout de 5 minutes.
- **Défaite** : 10 clients perdus, ou un enfant servi.

Le score est ensuite enregistré dans le tableau des 10 meilleurs scores.

### Touches de démo

Pour montrer rapidement une situation pendant la soutenance :

| Touche | Effet |
| --- | --- |
| E | Fait apparaître un enfant |
| N | Passe à la phase suivante |
| O | Fait apparaître un client |
| A | Saute aux 15 dernières secondes |

## Architecture

Le code suit le modèle **MVC** :

```
src/main/java/com/happyhour/
├── HappyHourApp.java      Point d'entrée, passage d'un écran à l'autre
├── model/                 Logique du jeu, sans JavaFX (testée)
│   ├── GameConfig.java    Toutes les valeurs d'équilibrage
│   ├── customer/          Les 5 types de clients et leur fabrique
│   │   └── state/         États d'un client : en attente, boit, parti
│   ├── drink/             Boissons, recettes, préparation et notation
│   ├── game/              La soirée (GameSession), phases, commandes
│   └── score/             Caisse et tableau des scores
├── view/                  Affichage JavaFX
│   ├── audio/             Musique et bruitages
│   ├── game/              Écran de jeu et dessin des verres
│   └── screen/            Menu, pause, réglages, bilan, scores, tutoriel
└── controller/            Clics et touches du joueur, boucle de jeu
```

### Design patterns

| Pattern | Utilisation |
| --- | --- |
| MVC | Le modèle ne dépend pas de JavaFX, la vue l'affiche, le contrôleur traduit les actions du joueur |
| State | Cycle de vie d'un client : `WaitingState`, `DrinkingState`, `LeftState` |
| Strategy | Notation des boissons : `CocktailGrading` et `BeerWineGrading` |
| Factory | Création des clients au hasard selon les proportions : `CustomerFactory` |
| Observer | Le modèle prévient l'affichage des événements : `GameListener` |

### Héritage

- `Customer` (abstraite) → `NormalCustomer`, `RushedCustomer`, `PickyCustomer`, `GenerousCustomer`, `ChildCustomer`
- `Drink` (abstraite) → `Beer`, `Wine`, `Cocktail` (abstraite) → `StirredCocktail`, `ShakenCocktail`

## Tests

Les tests JUnit 5 portent sur la logique du jeu (package `model`) : notation, pourboires, clients, service, fin de soirée, caisse et scores.

```bash
mvn test
```

La couverture est mesurée avec **JaCoCo** : environ **95 % des lignes du modèle** sont couvertes. Le rapport se trouve dans `target/site/jacoco/index.html` après les tests. Si ce fichier n'existe pas, génère-le avec `mvn test jacoco:report`.

La vue et le contrôleur (JavaFX) ne sont pas testés automatiquement : ils sont vérifiés en jouant.

## Documentation

- **Javadoc** : toutes les classes et méthodes publiques sont documentées.

  ```bash
  mvn javadoc:javadoc
  ```

  Le site est généré dans `target/site/apidocs/index.html` (ou `target/reports/apidocs/index.html` selon la version du plugin).

- **Game Design Document (GDD)** : règles complètes, équilibrage, interface et architecture.

## Équipe

- **Lilou** : logique du jeu (clients, moteur, boissons, score)
- **Ruben** : visuel (direction artistique, assets, affichage JavaFX, effets et son)
