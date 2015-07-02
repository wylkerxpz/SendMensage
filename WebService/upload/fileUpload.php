
<?php
//ini_set('upload_max_filesize', '10M');
//ini_set('post_max_size', '10M');
//ini_set('max_input_time', 300);
//ini_set('max_execution_time', 300);

$target_path = "uploads/";

$target_path = $target_path . basename($_FILES['image']['name']);

//Dados da mensagem
$sender = $_POST['sender'];
$recipient = $_POST['recipient'];
$arrayRecipient = explode(',', $recipient);
$subject = $_POST['subject'];
$mensage = $_POST['mensage'];


foreach ($arrayRecipient as $value) {
	$mens[] = array('sender' => $sender,
					'recipient' => $value,
					'subject' => $subject,
					'mensage' => $mensage,
					'arquivo' => basename($_FILES['image']['name'])
				);
}

$mensagens = array('mensagens' => $mens);

//converte o conteúdo do array para uma string JSON
$json_str = json_encode($mensagens); 

try {
    //throw exception if can't move the file
    if (!move_uploaded_file($_FILES['image']['tmp_name'], $target_path)) {
        throw new Exception('Não foi possivel mover o arquivo');
    }

    $fp = fopen('mensagens/'.date("Ymdhis").'.json', 'w');
	fwrite($fp, $json_str);
	fclose($fp);

    echo "O arquivo " . basename($_FILES['image']['name']) .
    " foi carregado no servidor";
} catch (Exception $e) {
    die('Erro arquivo não carregado: ' . $e->getMessage());
}
?>