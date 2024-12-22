### To-Do List pour le projet de serveur HTTP en Java (Gestion PHP et Multi-Threading, avec structure organisée)

#### Partie 1 : Configuration initiale et structure du projet
**Responsable 1 :**
1. Créer un dossier principal pour le projet (exemple : `MonServeurHTTP`).
   - Ce dossier contiendra tous les fichiers nécessaires au serveur, y compris les sources Java, les fichiers de configuration, et le dossier des fichiers à servir.

2. Créer la structure suivante dans le dossier principal :
   - `src/` : Contiendra tous les fichiers sources Java.
   - `htdocs/` : Sera utilisé comme dossier racine pour les fichiers statiques et PHP (comme dans Apache).
   - `config/` : Contiendra les fichiers de configuration (exemple : `server.config`).
   - `logs/` : Stockera les journaux du serveur.
   - `examples/` : Inclura des exemples de fichiers HTML, CSS, JS, et PHP pour les tests.

3. Écrire une classe principale `HttpServer` :
   - Configurer un `ServerSocket` pour écouter sur un port (par défaut : 8080).
   - Lancer un thread pour chaque connexion (multi-threading dès le départ).

**Responsable 2 :**
1. Créer un fichier de configuration (`config/server.config`) avec les paramètres suivants :
   - Port du serveur (ex. : 8080).
   - Chemin du dossier racine (`htdocs/`).
   - Chemin vers l’exécutable PHP (ex. : `/usr/bin/php`).
   - Nombre maximum de connexions simultanées.
   - Niveau de journalisation (INFO, DEBUG, ERROR).

2. Mettre en place un système de journalisation dans un fichier (`logs/server.log`) :
   - Journaliser les connexions entrantes (IP, méthode, URL).
   - Journaliser les erreurs (fichier non trouvé, erreurs PHP, etc.).

---

#### Partie 2 : Gestion des connexions HTTP (GET et POST)
**Responsable 1 :**
1. Créer une classe `ClientHandler` pour traiter chaque connexion dans un thread :
   - Lire les requêtes HTTP (méthode, URL, en-têtes).
   - Identifier si la requête cible un fichier statique ou un fichier PHP.
   - Retourner une réponse HTTP adéquate.

2. Créer une classe `HttpRequest` pour analyser et stocker les données des requêtes :
   - Méthode HTTP (GET, POST, etc.).
   - URL demandée.
   - En-têtes et paramètres de requête.

**Responsable 2 :**
1. Implémenter une classe `HttpResponse` pour générer des réponses HTTP :
   - Inclure le code de statut (200 OK, 404 Not Found, etc.).
   - Ajouter les en-têtes nécessaires (Content-Type, Content-Length, etc.).
   - Gérer les erreurs (400 Bad Request, 500 Internal Server Error).

2. Ajouter une logique pour servir des fichiers statiques depuis `htdocs/` :
   - Lire les fichiers HTML, CSS, JS et retourner leur contenu avec le bon type MIME.
   - Gérer les codes d’erreur pour les fichiers manquants ou interdits (403, 404).

---

#### Partie 3 : Gestion des fichiers PHP
**Responsable 1 :**
1. Ajouter une méthode dans `ClientHandler` pour détecter les fichiers `.php` :
   - Utiliser `ProcessBuilder` pour exécuter l’exécutable PHP avec le fichier demandé en argument.
   - Transmettre les données POST via `stdin` à PHP.
   - Lire la sortie de PHP et l’inclure dans la réponse HTTP.

2. Gérer les erreurs spécifiques liées à PHP :
   - Si l'exécutable PHP échoue ou n’est pas trouvé, retourner une réponse HTTP 500 Internal Server Error.

**Responsable 2 :**
1. Ajouter un support pour les données POST dans les requêtes PHP :
   - Lire le corps des requêtes POST.
   - Passer ces données au script PHP via `stdin`.

2. Ajouter une gestion des en-têtes spécifiques pour PHP :
   - Traiter les en-têtes HTTP renvoyés par le script PHP.
   - Les inclure dans la réponse finale.

---

#### Partie 4 : Multi-threading avancé
**Responsable 1 :**
1. Optimiser la gestion des threads avec un pool de threads pour limiter les ressources utilisées.
2. Ajouter une gestion des délais d’attente (timeout) pour les connexions inactives.

**Responsable 2 :**
1. Implémenter une file d’attente pour les connexions lorsque le nombre maximum est atteint.
2. Ajouter une réponse HTTP 503 Service Unavailable si la file d’attente est pleine.

---

#### Partie 5 : Tests et amélioration
**Responsable 1 :**
1. Ajouter un support pour d'autres méthodes HTTP (HEAD, OPTIONS).
2. Créer des tests pour valider les fonctionnalités suivantes :
   - Lecture et affichage de fichiers statiques.
   - Traitement des scripts PHP (GET et POST).
   - Gestion des erreurs HTTP (400, 404, 500, 503).

**Responsable 2 :**
1. Ajouter des exemples dans `examples/` pour valider les tests :
   - Fichiers statiques (HTML, CSS, JS).
   - Scripts PHP (un fichier pour GET, un pour POST).
2. Créer des scénarios pour tester les limites du serveur (ex. : dépassement du nombre maximum de connexions).

---

#### Partie 6 : Finalisation
**Responsable 1 :**
1. Préparer une documentation simple pour expliquer :
   - La structure du projet.
   - Les étapes pour configurer et démarrer le serveur.

**Responsable 2 :**
1. Vérifier le fonctionnement global avec plusieurs clients simultanés.
2. Ajouter des messages clairs pour les logs et les erreurs.

---
