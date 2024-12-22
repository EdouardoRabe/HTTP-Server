<?php
    if (!empty($_POST["nom"]) && !empty($_POST["autre"])) {
        echo $_POST["nom"];
        echo $_POST["autre"];
        
    } else {
        echo "Aucun parametre POST recu.";
    }
?>
