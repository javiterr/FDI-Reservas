$(document).ready(function(){
	var espacio = {};
 	var tipoBusqueda;
 	var token = $("meta[name='_csrf']").attr("content");
 	var header = $("meta[name='_csrf_header']").attr("content");
 	var reqHeaders = [];
 	reqHeaders[header] = token;
 	
 	$("#texto-busqueda").keyup(function(){
 		var searchTerm = $('#texto-busqueda').val();
 		tipoBusqueda = $('#selec-busqueda').val();
 		
 		var link = '/reservas/gestor/administrar/espacios/eliminados/' + tipoBusqueda + '/' + searchTerm + '/page/1';
 		$("#busquedaEspacio").attr("href",link);
 		
 	});
 	
 	$('td a').click(function(){
 		
 		espacio.id =  $(this).attr("data-id");
 		var nombreEspacio = $(this).attr("name");
 		var capacidad = $(this).attr("capacidad");
 		var microfono = $(this).attr("micro");
 		var proyector = $(this).attr("proye");
 		var tipoEspacio = $(this).attr("tipo");
 		var edificio = $(this).attr("edif");
 		var eliminado = $(this).attr("act");
 		var accion = $(this).attr("data-accion");
 		var imagen = "../../.." + $(this).attr("img");

 		//$('#modalEditarEspacio #idEspacio').text(espacio.id);
 		$('#modalEditarEspacio #idNombre').text(nombreEspacio);
 		$('#modalEditarEspacio #idCapa').text(capacidad);
 		$('#modalEditarEspacio #idMicro').text(microfono);
 		$('#modalEditarEspacio #idProy').text(proyector);
 		$('#modalEditarEspacio #idTipo').text(tipoEspacio);
 		$('#modalEditarEspacio #idEdificio').text(edificio);
 		$('#modalEditarEspacio #idActivado').text(eliminado);
 		$('#modalEditarEdificio #idAttachment').attr("src",imagen);
 		$('#modalEditarEspacio #btn-editar').prop("href", baseURL + 'gestor/administrar/espacio/editar/' + espacio.id);
 	
 		if (accion == 'Restaurar'){
 			
				modalRestaurarEspacio(espacio, reqHeaders);	
			
 		}else if(accion == 'Ver'){
 		
 			$('#modalEditarEspacio').modal('show');
 		}
 	});
 	
 	$('#selec-busqueda').change(function(){
 		
 		$('#texto-busqueda').val("");
 		if ($(this).val()=="nombre")
 			$('#texto-busqueda').prop("placeholder", "Introduce el nombre del espacio");
 		else if ($(this).val()=="edificio")
 			$('#texto-busqueda').prop("placeholder", "Introduce el nombre del edificio");
 	});
 	
});	

function modalRestaurarEspacio(espacio, reqHeaders){
 	console.log(espacio);
		$.ajax({
			url: baseURL + "gestor/administrar/espacio/restaurar/" + espacio.id,
			type: 'DELETE',
			headers : reqHeaders,
			success : function(datos) {
				alert("Edificio eliminado");
				$('#modalEditarEdificio').modal('hide');
				$("#" + espacio.id).remove();
			
			},    
			error : function(xhr, status) {
				alert('Disculpe, existió un problema');
			}
 	});
 
}