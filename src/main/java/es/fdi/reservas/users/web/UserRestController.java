package es.fdi.reservas.users.web;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
import es.fdi.reservas.fileupload.business.entity.Attachment;
import es.fdi.reservas.reserva.web.EdificioDTO;
import es.fdi.reservas.users.business.boundary.UserService;
import es.fdi.reservas.users.business.entity.User;

@RestController
public class UserRestController {

	private UserService user_service;

	@Autowired
	public UserRestController(UserService us) {
		user_service = us;
	}
	
	@RequestMapping(value = "/user/{idUsuario}", method = RequestMethod.DELETE)
	public String eliminarUsuario(@PathVariable("idUsuario") long idUser) {
		user_service.editarUserDeleted(idUser);
		return "redirect:/admin/administrar/usuarios/1";
	}
	
	@RequestMapping(value="/gestor/nuevoUsuario", method=RequestMethod.POST)
	public String crearEdificioGestor(@RequestBody UserDTO f){
		user_service.addNewUser(f);
	    return "redirect:/gestor/administrar/edificios/page/1";
	}
	
	@RequestMapping(value = "/admin/administrar/usuarios/{numPag}/restaurar/{idUsuario}", method = RequestMethod.DELETE)
	public String restaurarUsuario(@PathVariable("idUsuario") Long idUser, @PathVariable("numPag") Long numPag){
		user_service.restaurarUser(idUser);
		return "redirect:/reservas/administrar/usuarios/{numPag}";
	}
	
	@RequestMapping(value = "/gestor/administrar/user/restaurar/{idUsuario}", method = RequestMethod.DELETE)
	public String restaurarUsuarioGestor(@PathVariable("idUsuario") Long idUsuario){
		System.out.println(idUsuario);
		user_service.restaurarUser(idUsuario);
		return "redirect:gestor/administrar/usuarios/eliminados/page/1";
	}
	
	@RequestMapping(value="/admin/administrar/usuarios/editar/{idUser}/{user}/{admin}/{gestor}", method=RequestMethod.PUT)
	public void editarUsuario(@PathVariable("idUser") long idUser, @PathVariable("user") String user,
			@PathVariable("admin") String admin, @PathVariable("gestor") String gestor,
			@RequestBody UserDTO userActualizado) {
					
			Attachment attachment = new Attachment("");
			
			if (userActualizado.getFacultad() == null){
				userActualizado.setFacultad(user_service.getUser(userActualizado.getId()).getFacultad().getId());
			}
			if (userActualizado.getImagen().equals("")){
				attachment = user_service.getUser(userActualizado.getId()).getImagen();
			}
			else {

				if (user_service.getAttachmentByName(userActualizado.getImagen()).isEmpty()){
			
					//si no esta, lo añado
					String img = userActualizado.getImagen();
					int pos = img.lastIndexOf(".");
					String punto = img.substring(0, pos);
					String fin = img.substring(pos+1, img.length());
					String nom = punto + "-" + idUser + "." + fin;
					nom = nom.replace(nom, "/img/" + nom);
					
					
					attachment.setAttachmentUrl("/img/" + userActualizado.getImagen());
					attachment.setStorageKey(nom);
				}else{
					attachment = user_service.getAttachmentByName(userActualizado.getImagen()).get(0);
				}
			}
			user_service.editaUsuario(userActualizado, user, admin, gestor, attachment);
			//System.out.println(imagen + " Existe");
//		}else{
//			System.out.println(imagen + " No existe");
//		}
		
	}
	
	@RequestMapping(value="/gestor/administrar/usuarios/editar/{idUser}/{user}/{admin}/{gestor}", method=RequestMethod.PUT)
	public void editarUsuarioGestor(@PathVariable("idUser") long idUser, @PathVariable("user") String user,
			@PathVariable("admin") String admin, @PathVariable("gestor") String gestor,
			@RequestBody UserDTO userActualizado) {
					
			Attachment attachment = new Attachment("");
			
			if (userActualizado.getFacultad() == null){
				userActualizado.setFacultad(user_service.getUser(userActualizado.getId()).getFacultad().getId());
			}
			if (userActualizado.getImagen().equals("")){
				attachment = user_service.getUser(userActualizado.getId()).getImagen();
			}
			else {
//				String img = "/img/users/" + user_service.getUser(idUser).getUsername();
//				String nombreViejo = user_service.getUser(idUser).getUsername();
//				String nombreNuevo = userActualizado.getUsername();
//				
//				if (!nombreViejo.equalsIgnoreCase(nombreNuevo)){
//					//si el nombre de usuario ha cambiado, hay que renombrar el directorio y las referencias
//					//File dirViejo = new File("../src/main/webapp/img/"  + nombreViejo);
//					File dirNuevo = new File("../../img/"  + nombreNuevo);
//					boolean correcto = dirNuevo.mkdir();
//					
//				}
//				File file = new File("/img/" + userActualizado.getImagen());
//				boolean ex = file.exists();
				if (user_service.getAttachmentByName(userActualizado.getImagen()).isEmpty()){
			
					//si no esta, lo añado
									
					attachment.setAttachmentUrl("/img/" + userActualizado.getImagen());
					attachment.setStorageKey(user_service.getUser(idUser).getUsername() + "/" + userActualizado.getImagen());
					//reserva_service.addAttachment(attachment);
				}else{
					attachment = user_service.getAttachmentByName(userActualizado.getImagen()).get(0);
				}
			}
			user_service.editaUsuario(userActualizado, user, admin, gestor, attachment);
			//System.out.println(imagen + " Existe");
//		}else{
//			System.out.println(imagen + " No existe");
//		}
		
	}
	
	@RequestMapping(value="/admin/nuevoUsuario", method=RequestMethod.POST)
	public String crearUsuario(@RequestBody UserDTO us){
		user_service.addNewUser(us);
	   return "redirect:/admin/administrar";
		//return "nuevoUsuario";
	}
	
	@RequestMapping(value="/nuevoUser", method=RequestMethod.POST)
	public String crearUsuarioLogin(@RequestBody UserDTO us){
		user_service.addNewUserLogin(us);
	   return "redirect:/login";
		//return "nuevoUsuario";
	}

	@RequestMapping(value = "/usuarios/tag/{tagName}", method = RequestMethod.GET)
	public List<UserDTO> usuariosFiltro(@PathVariable("tagName") String tagName) {

		List<UserDTO> result = new ArrayList<>();
		List<User> usuarios = new ArrayList<>();

		usuarios = user_service.getUsuariosPorTagName(tagName);

		for (User u : usuarios) {
			result.add(UserDTO.fromUserDTOAutocompletar(u));
		}

		return result;
	}

	@RequestMapping(value = "/gestor/usuarios/tag/{tagName}", method = RequestMethod.GET)
	public List<UserDTO> usuariosFiltroAutocompletar(@PathVariable("tagName") String tagName) {

		List<UserDTO> result = new ArrayList<>();
		List<User> usuarios = new ArrayList<>();

		usuarios = user_service.getUsuariosPorTagName(tagName);

		for (User u : usuarios) {
			result.add(UserDTO.fromUserDTOAutocompletar(u));
		}

		return result;
	}
	
	@RequestMapping(value = "/perfil/editar", method = RequestMethod.PUT)
	public void editarPerfil(@RequestBody UserDTO userDTO){
		user_service.editarPerfil(userDTO);
	}
	
	@RequestMapping(value = "/admin/usuarios/tag/{tagName}", method = RequestMethod.GET)
	public List<UserDTO> usuariosFiltroAutocompletarAdmin(@PathVariable("tagName") String tagName) {

		List<UserDTO> result = new ArrayList<>();
		List<User> usuarios = new ArrayList<>();

		usuarios = user_service.getUsuariosPorTagName(tagName);

		for (User u : usuarios) {
			result.add(UserDTO.fromUserDTOAutocompletar(u));
		}

		return result;
	}
	
//	@RequestMapping(value = "/admin/email/tag/{tagName}", method = RequestMethod.GET)
//	public List<UserDTO> emailFiltroAutocompletar(@PathVariable("tagName") String tagName) {
//
//		List<UserDTO> result = new ArrayList<>();
//		List<User> usuarios = new ArrayList<>();
//
//		usuarios = user_service.getUsuariosPorEmail(tagName);
//
//		for (User u : usuarios) {
//			result.add(UserDTO.fromUserDTOAutocompletar(u));
//		}
//
//		return result;
//	}
//	
//	@RequestMapping(value = "/admin/facultad/tag/{tagName}", method = RequestMethod.GET)
//	public List<UserDTO> facultadFiltroAutocompletar(@PathVariable("tagName") String tagName) {
//
//		List<UserDTO> result = new ArrayList<>();
//		List<User> usuarios = new ArrayList<>();
//
//		usuarios = user_service.getUsuariosPorFacultad(tagName);
//
//		for (User u : usuarios) {
//			result.add(UserDTO.fromUserDTOAutocompletar(u));
//		}
//
//		return result;
//	}

}
