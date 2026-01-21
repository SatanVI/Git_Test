# Projet Librairie 

## Structure
- `src/main/java/domain` : entités du domaine (`Book`, `User`, `Sale`, `Rental`, `Reservation`).
- `src/main/java/repository` : stockage en mémoire et persistance simple (`FileStorage`).
- `src/main/java/service` : logique métier.
- `src/main/java/ui` : interface console (`Main`).
- `src/main/java/patterns` : exemples de patterns utilisés.

## Patterns implémentés et justification
 # Projet Librairie - Patterns GoF (POO avancée)

Ce projet est une application console de gestion d'une librairie conçue comme support pédagogique pour l'apprentissage et la mise en pratique des Design Patterns (GoF).

## Structure du projet
- `src/main/java/domain` : entités du domaine (`Book`, `User`, `Sale`, `Rental`, `Reservation`).
- `src/main/java/repository` : gestion de la persistance simple via fichiers (`FileStorage`, `BookRepository`, `UserRepository`).
- `src/main/java/service` : logique métier (services pour ventes, locations, réservations, paiements, utilisateurs).
- `src/main/java/ui` : interface console principale (`Main`).
- `src/main/java/patterns` : exemples concrets de patterns utilisés.

## Patterns utilisés — description, justification et rôle dans le projet

- Singleton
  - Exemples : `repository.FileStorage`, `patterns.singleton.AppLogger`.
  - Rôle/Justification : assure un point d'accès unique aux ressources partagées (fichiers de données et logger). Evite les problèmes d'initialisation multiple et facilite la gestion centralisée des logs et fichiers `data/*`.

- Factory
  - Exemple : `patterns.factory.PaymentFactory`.
  - Rôle/Justification : centralise la création des implémentations de paiement (carte, espèces, paypal). Permet d'ajouter de nouveaux moyens de paiement sans modifier le code client.

- Adapter
  - Exemples : `patterns.adapter.PaypalAdapter`, `patterns.adapter.ExternalPaypalClient` (simulé).
  - Rôle/Justification : enveloppe un client externe (SDK PayPal) pour l'adapter à l'interface interne `Payment`. Facilite l'intégration de librairies tierces sans impacter le reste de l'application.

- Decorator
  - Exemples : `patterns.decorator.BaseReceipt`, `TaxReceipt`, `FooterReceipt`.
  - Rôle/Justification : ajoute dynamiquement des fonctionnalités au reçu (`Receipt`) — taxes, pied de page — sans multiplier les sous-classes.

- Strategy
  - Exemples : `patterns.strategy.StandardPricing`, `StudentPricing`, `TeacherPricing`, `CappedPenalty`.
  - Rôle/Justification : encapsule des algorithmes variables (tarification, pénalités) et permet de sélectionner la stratégie adaptée selon le type d'utilisateur.

- Observer
  - Exemple : `patterns.observer.EventBus`.
  - Rôle/Justification : diffuse des événements (vente, location, réservation) à des abonnés (logger, console, autres composants), favorisant le découplage.

- Composite 
  - Exemples : `patterns.composite.Category`, `patterns.composite.ProductLeaf`.
  - Rôle/Justification : représente une hiérarchie catégories/produits. Implémentation partielle — persistance/liaison complète à prévoir.

- Repository 
  - Exemples : `repository.BookRepository`, `repository.UserRepository`.
  - Rôle/Justification : encapsule l'accès aux données (chargement/enregistrement via `FileStorage`) pour séparer la logique métier des détails de persistance.

## Sécurité et Tests

### Sécurité
- Hachage des mots de passe : Les mots de passe utilisateurs ne sont plus stockés en clair. Un algorithme de hachage est appliqué avant la persistance dans `data/users.txt` pour garantir la confidentialité des comptes.

### Tests Unitaires
Une couverture de tests (JUnit) a été mise en place pour valider la robustesse de l'application (voir dossier `src/test/java`) :
- **Patterns** : Validation des logiques `Decorator`, `Factory` (Paiement) et `Strategy` (Prix et Pénalités).
- **Services** : Tests d'intégration des services `RentalService`, `SalesService` et `UserService`.
- **Repository** : Vérification des opérations de lecture/écriture via `FileStorageTest`.