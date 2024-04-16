Music Server et Client

Cette application est un serveur et un client de musique qui utilisent la bibliothèque Ice pour la communication et SQLite pour la base de données. Le serveur stocke les fichiers musicaux et les métadonnées dans une base de données SQLite et les diffuse en continu aux clients via Ice.
Installation et Utilisation
Serveur
    Lancer le serveur avec la commande make run_serveur

Client
    Compiler le client avec la commande make install_client
    Lancer le client avec la commande make run_client


Dépendances

    Python 3.x
    Ice 3.7.x
    SQLite 3.x
    Java 8 ou version ultérieure
    vlcj 4.x

Auteurs

    Ange Curé
