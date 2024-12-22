<?php
    session_start();
    $_SESSION["testes"]="Edouardo";
?>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Document</title>
</head>
<body>
    <form action="test.php" method="post">
        <input type="text" placeholder="Your name" name="nom">
        <input type="text" placeholder="Your name" name="autre">
        <button type="submit">
            OK
        </button>
    </form>
</body>
</html>