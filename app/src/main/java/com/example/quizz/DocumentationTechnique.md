# Documentation Technique de l'Application Quizz

## Introduction
L'application Quizz utilise l'architecture MVVM (Model-View-ViewModel) pour séparer la logique métier de l'interface utilisateur.

## Architecture MVVM
L'architecture MVVM est choisie pour ses nombreux avantages, notamment la séparation des préoccupations, la facilité de test, et une meilleure gestion du cycle de vie des composants UI. Elle comprend trois composants principaux :

- **Model** : Représente les données et la logique métier de l'application.
- **View** : Affiche les informations à l'utilisateur et détecte les interactions utilisateur.
- **ViewModel** : Sert de pont entre le Model et la View. Il contient la logique de présentation et gère les changements d'état de l'interface utilisateur.

## Structure des Dossiers
L'application est organisée en plusieurs dossiers spécifiques à leurs fonctions, ce qui facilite la navigation et la compréhension du code :

- `db` : Contient les classes liées à la base de données, y compris les DAO (Data Access Objects) et les entités.
- `di` : Dossier pour l'injection de dépendances, utilisant Koin pour simplifier l'instanciation des classes.
- `model` : Définit les structures de données utilisées dans l'application.
- `repositories` : Contient les classes qui agissent comme intermédiaires entre les sources de données (API, base de données locale) et le ViewModel.
- `services` : Regroupe les interfaces pour les appels API, telles que QuizzApiService pour les questions de quizz, facilitant l'accès dynamique à des données externes.
- `utils` : Inclut des classes utilitaires essentielles pour l'application, telles que ResourceProvider pour l'accès simplifié aux ressources de chaîne et PasswordUtils pour le hachage sécurisé des mots de passe avec ajout de sel et encodage.
- `view` : Contient les activités et les fragments, implémentant les éléments d'interface utilisateur.
- `viewmodel` : Comprend les ViewModel qui gèrent la logique de présentation et l'interaction avec les modèles.

## Choix de Conception
- **Utilisation de Koin pour l'Injection de Dépendances** : Koin est choisi pour sa légèreté et sa facilité d'intégration. Il permet une configuration déclarative de l'injection de dépendances, rendant le code plus propre et plus facile à tester.

- **Base de Données Room** : Room est utilisé pour la persistance des données. Il offre une abstraction sur SQLite et permet une manipulation de données plus intuitive et moins sujette aux erreurs.

- **Retrofit pour les Appels Réseau** : Retrofit est utilisé pour la communication avec des services web. Sa facilité d'utilisation, sa flexibilité et son intégration avec Gson pour la sérialisation/désérialisation des objets JSON le rendent idéal pour les opérations réseau.

- **Kotlin Coroutines** : Pour la programmation asynchrone, simplifiant les opérations en arrière-plan et la gestion des appels réseau.

- **LiveData et Data Binding** : LiveData est utilisé en combinaison avec Data Binding pour créer des interfaces réactives qui se mettent à jour automatiquement lorsque les données changent. Cela simplifie la gestion de l'UI en réduisant le code boilerplate et en améliorant la performance.

### Tests
Pour assurer la qualité et la fiabilité de l'application, les technologies suivantes sont utilisées :
- **Espresso** : Pour les tests d'interface utilisateur, simulant des interactions utilisateur pour vérifier l'état de l'interface.
- **JUnit & Mockito** : Pour les tests unitaires, facilitant la validation de la logique métier et des interactions entre les composants.
- **androidx.test** : Bibliothèques supportant les tests instrumentés et unitaires, y compris `androidx.test.ext:junit` pour les assertions JUnit améliorées et `androidx.test.espresso:espresso-core` pour l'automatisation des tests UI.

[Lien vers README](README.md) 