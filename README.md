# Projet Cultura
Auteur: Marie SINAMA et Khadidja MEZIARI 

Notre projet vous fait découvrir les salles de spectales, theatres de Toulouse et les Cinema de france.


Tout d'abord il faut ouvrir le projet sur un environnement de developpement Java ( bien verifier la version de Java 11).

# Remplir le triplestore

On a deux fichiers de données " theatres-et-salles-de-spectacles " et "Liste_Cinema_2019"

Pour remplir le triplestore il faut suivre les étapes suivantes:

-> Lancer le serveur Apache Jena.

-> Faire appel à la fonction sendOntologyToFuseki() dans CreateModel.java pour chaque fichier ttl.


# Lancer l'API

-> Executer le main du fichier "CulturewebApplication.java".

-> taper sur votre navigateur localhost:8080/liste-cultura.

-> Vous pouvez ainsi découvrir ce que notre API propose

ex: Affichage d'une liste de salles de spectacle qui en cliquant dessus affiche plus de details...
