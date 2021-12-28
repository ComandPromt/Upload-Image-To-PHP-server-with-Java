<?php  
    
mkdir("imagenes",0777);

if (isset($_FILES['archivo'])) {
	
    $file = $_FILES["archivo"];
    
	$tipo = $file['type'];
    
	$path = $file['tmp_name'];
    
	$size = $file['size'];
    
	$nombre = $file['name'];
    
	$dimensiones = getimagesize($path);
    
	$width = $dimensiones[0];
    
	$height = $dimensiones[1];
    
	$carpeta = "imagenes/";
    
	$imagen = NULL;
    
	$urlImagen = NULL;
	
    if ($tipo == "image/jpg" || $tipo == "image/jpeg" || $tipo == "image/JPG" || $tipo == "image/JPEG") {
		
        $imagen = imagecreatefromjpeg($path);
		
    }

	else if ($tipo == "image/png" || $tipo == "image/PNG") {
		
        $imagen = imagecreatefrompng($path);
		
    }

	else {
		
        echo 'FORMATO DE ARCHIVO INVÁLIDO';
		
    }
	
    if (is_uploaded_file($path) && $imagen != NULL) {
						
        $urlImagen = $carpeta . "$nombre";
		
        $image_p = imagecreatetruecolor($width, $height);
		
        imagecopyresampled($image_p, $imagen, 0, 0, 0, 0, $width, $height, $width, $height);
		
        $uploaded = imagejpeg($image_p, $urlImagen);
				
        if ($uploaded == TRUE) {
			
            echo 'IMAGEN SUBIDA CORRECTAMENTE';
			
        } 
		
		else {
			
            echo 'ERROR AL SUBIR IMAGEN';
			
        }
		
    }
	
} 

else {
	
    echo 'NO HAY IMAGEN';
	
}

?>
