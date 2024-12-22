#!/bin/bash
# Déplacez-vous dans le répertoire du projet
echo "Changement de répertoire vers /home/edouardo/Lecon/S3 Edouardo/HTTP-Server/usr/share/HTTP-Server"
cd /usr/share/HTTP-Server

# Vérifier si des fichiers .class existent déjà
echo "Vérification de l'existence des fichiers .class..."
if [ ! -f fenetre/ControlPanel.class ]; then
  # Si le fichier .class n'existe pas, compiler les fichiers Java
  echo "Le fichier fenetre/ControlPanel.class n'existe pas. Compilation des fichiers Java..."
  javac -d . *.java
else
  echo "Les fichiers .class sont déjà présents. Pas de compilation nécessaire."
fi

# Lancez le serveur
echo "Lancement du serveur avec la classe fenetre.ControlPanel..."
java fenetre.ControlPanel
