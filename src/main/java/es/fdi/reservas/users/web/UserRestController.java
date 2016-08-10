package es.fdi.reservas.users.web;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import es.fdi.reservas.fileupload.business.entity.Attachment;
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

	@RequestMapping(value = "/administrar/usuarios/{numPag}/restaurar/{idUsuario}", method = RequestMethod.GET)
	public String restaurarUsuario(@PathVariable("idUsuario") Long idUser, @PathVariable("numPag") Long numPag) {
		user_service.restaurarUser(idUser);
		return "redirect:/reservas/administrar/usuarios/{numPag}";
	}

	@RequestMapping(value = "/admin/administrar/usuarios/{numPag}/restaurar")
	public ModelAndView restaurarUsers(@PathVariable("numPag") Long numPag) {
		ModelAndView model = new ModelAndView("index");
		User u = user_service.getCurrentUser();
		model.addObject("usuarios", user_service.getEliminados());
		model.addObject("User", u);
		model.addObject("pagina", numPag);
		model.addObject("view", "/admin/papelera_usuarios");
		return model;
	}

	@RequestMapping(value = "/admin/administrar/usuarios/editar/{idUser}/{user}/{admin}/{gestor}/{user.imagen}", method = RequestMethod.PUT)
	public void editarUsuario(@PathVariable("idUser") long idUser, @PathVariable("user") String user,
			@PathVariable("admin") String admin, @PathVariable("gestor") String gestor,
			@RequestBody UserDTO userActualizado, @PathVariable("user.imagen") String img) {
		
		String imagen = "A:/FDI-Reservas/src/main/webapp/img/" + img;
		File fich = new File(imagen);
		
		if (fich.exists()){
			Attachment attachment = new Attachment("");
			if (user_service.getAttachmentByName(img).isEmpty()){
				//si no esta, lo añado
				
				attachment.setAttachmentUrl("/img/" + img);
				attachment.setStorageKey(user_service.getUser(idUser).getUsername() + "/" + img);
				//reserva_service.addAttachment(attachment);
			}else{
				attachment = user_service.getAttachmentByName(img).get(0);
			}
			user_service.editaUsuario(userActualizado, user, admin, gestor, attachment);
			System.out.println(imagen + " Existe");
		}else{
			System.out.println(imagen + " No existe");
		}
		
	}
	
	@RequestMapping(value="/admin/nuevoUsuario", method=RequestMethod.POST)
	public String crearUsuario(User us){
		user_service.addNewUser(us);
	   return "redirect:/admin/administrar";
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

}
