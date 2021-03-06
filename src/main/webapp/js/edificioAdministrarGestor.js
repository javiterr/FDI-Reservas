$(document).ready(function(){
	 	var edificio = {};
	 	var token = $("meta[name='_csrf']").attr("content");
	 	var header = $("meta[name='_csrf_header']").attr("content");
	 	var reqHeaders = [];
	 	reqHeaders[header] = token;
	 	
	 	$("#texto-busqueda").keyup(function(){
	 		var searchTerm = $('#texto-busqueda').val();
	 		tipoBusqueda = $('#selec-busqueda').val();
	 		
	 		var link = '/reservas/gestor/administrar/edificios/' + tipoBusqueda + '/' + searchTerm + '/page/1';
	 		$("#busquedaEdificio").attr("href",link);
	 		
	 	});
	 	
	 	$('td a').click(function(){
	 		
	 		
	 		edificio.id =  $(this).attr("data-id");
	 		var nombreEdificio = $(this).attr("name");
	 		var direccion = $(this).attr("dir");
	 		var deleted = $(this).attr("act");
	 		var facultad = $(this).attr("fac");
	 		var imagen = "../../.." + $(this).attr("img");
	 		var accion = $(this).attr("data-accion");
	 		
	 		$('#modalEditarEdificio #idNombre').text(nombreEdificio);
	 		$('#modalEditarEdificio #idDir').text(direccion);
	 		$('#modalEditarEdificio #idFacul').text(facultad);
	 		$('#modalEditarEdificio #idActivado').text(deleted);
	 		$('#modalEditarEdificio #idAttachment').attr("src",imagen);
	 		$('#modalEditarEdificio #btn-editar').prop("href", baseURL + "gestor/administrar/edificios/editar/" + edificio.id);
	 		
	 		
	 		if (accion == 'Eliminar'){
	 			
 				modalEliminarEdificio(edificio, reqHeaders);	
 			
	 		}else if(accion == 'Ver'){
	 		
	 			$('#modalEditarEdificio').modal('show');
	 		}
	 	});
	 	
	 	$('#selec-busqueda').change(function(){
	 		$('#texto-busqueda').val("");
	 		if ($(this).val()=="nombre")
	 			$('#texto-busqueda').prop("placeholder", "Introduce nombre de edificio");
	 		else 
	 			$('#texto-busqueda').prop("placeholder", "Introduce la dirección");
	 	});
});

function modalEliminarEdificio(edificio, reqHeaders){
	 	
	 		$.ajax({
	 			url: baseURL + "edificio/" + edificio.id,
	 			type: 'DELETE',
	 			headers : reqHeaders,
	 			success : function(datos) {
	 				$('#modalEditarEdificio').modal('hide');
	 				$("#" + edificio.id).remove();
	 			
	 			},    
	 			error : function(xhr, status) {
	 				alert('Disculpe, existió un problema');
	 			}
		 	});
		 
	 }
	 
