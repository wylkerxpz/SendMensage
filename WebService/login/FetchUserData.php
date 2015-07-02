<?php
    $con = mysql_connect("mysql.consertar.srv.br","wylker","Pbge6nsp@");
    $db= mysql_select_db("dsdm");
    
    $username = $_POST["username"];
    $password = $_POST["password"];

    if (!empty($_POST)) {
        if (empty($_POST['username']) || empty($_POST['password'])) {
            // Create some data that will be the JSON response 
            $response["success"] = 0;
            $response["message"] = "One or both of the fields are empty .";

            //die is used to kill the page, will not let the code below to be executed. It will also
            //display the parameter, that is the json data which our android application will parse to be //shown to the users
            die(json_encode($response));
        }

        $query = " SELECT * FROM Docente WHERE docente_usuario = '$username' and docente_senha = '$password'";

        // executa a query
        $dados = mysql_query($query, $con) or die(mysql_error());
        // transforma os dados em um array
        $linha = mysql_fetch_assoc($dados);
        // calcula quantos dados retornaram
        $total = mysql_num_rows($dados);

        // Verifica se existe um usuario
        if ($total == 1) {
            $user["docente_nome"] = $linha['docente_nome'];
            die(json_encode($user));    
        } else {
            $response["success"] = 0;
            $response["message"] = "invalid username or password ";
            die(json_encode($response));
        }
    } else{
        $response["success"] = 0;
        $response["message"] = " One or both of the fields are empty ";
        die(json_encode($response));
    }
  
    mysql_close();
?>