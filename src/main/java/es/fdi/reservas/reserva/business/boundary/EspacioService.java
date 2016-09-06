package es.fdi.reservas.reserva.business.boundary;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import es.fdi.reservas.fileupload.business.control.AttachmentRepository;
import es.fdi.reservas.fileupload.business.entity.Attachment;
import es.fdi.reservas.reserva.business.control.EdificioRepository;
import es.fdi.reservas.reserva.business.control.EspacioRepository;
import es.fdi.reservas.reserva.business.entity.Autorizacion;
import es.fdi.reservas.reserva.business.entity.Edificio;
import es.fdi.reservas.reserva.business.entity.Espacio;
import es.fdi.reservas.reserva.business.entity.TipoEspacio;
import es.fdi.reservas.reserva.web.EdificioDTO;
import es.fdi.reservas.reserva.web.EspacioDTO;

@Service
public class EspacioService {

	private EspacioRepository espacio_repository;
	private EdificioService edificio_service;
	private AttachmentRepository attachment_repository;
	
	@Autowired
	public EspacioService(EspacioRepository espacio_repository, AttachmentRepository ar, EdificioService er) {
		super();
		this.espacio_repository = espacio_repository;
		this.attachment_repository = ar;
		this.edificio_service = er;
	}

	public List<Espacio> getEspaciosEdificio(long idEdificio) {
		return espacio_repository.findByEdificioId(idEdificio);
	}

	public Espacio getEspacio(long id_espacio) {
		return espacio_repository.findOne(id_espacio);
	}

	public List<Espacio> getTiposEspacio(long idEdificio, TipoEspacio idTipoEspacio) {
		return espacio_repository.findByEdificioIdAndTipoEspacio(idEdificio, idTipoEspacio);
	}
	
	public Iterable<Espacio> getEspacios() {
		return espacio_repository.findAll();
	}
	
	public void eliminarEspacio(long idEspacio) {
		//espacio_repository.delete(idEspacio);
		espacio_repository.softDelete(Long.toString(idEspacio));
	}
	
	public Page<Espacio> getEspaciosPaginados(Pageable pageRequest) {
		return espacio_repository.findAll(pageRequest);
	}
	
	
	public Espacio editarEspacioDeleted(Long idEspacio){
		Espacio e = espacio_repository.findOne(idEspacio);
		e.setDeleted(true);
		return espacio_repository.save(e);
	}
	
	public Espacio editarEspacio(EspacioDTO espacio, Attachment attachment){
		Espacio e = espacio_repository.findOne(espacio.getId());
		e.setNombreEspacio(espacio.getNombreEspacio());
		e.setCapacidad(espacio.getCapacidad());
		e.setMicrofono(espacio.isMicrofono());
		e.setProyector(espacio.isProyector());
		e.setTipoEspacio(TipoEspacio.fromTipoEspacio(espacio.getTipoEspacio()));
		Long id = Long.decode(espacio.getEdificio());
		e.setEdificio(edificio_service.findEdificio(id));
		e.setImagen(attachment);
		return espacio_repository.save(e);
	}
	
	public Espacio editarEspacioGestor(EspacioDTO espacio, Attachment attachment){
		Espacio e = espacio_repository.findOne(espacio.getId());
		e.setNombreEspacio(espacio.getNombreEspacio());
		e.setCapacidad(espacio.getCapacidad());
		e.setMicrofono(espacio.isMicrofono());
		e.setProyector(espacio.isProyector());
		e.setTipoEspacio(TipoEspacio.fromTipoEspacio(espacio.getTipoEspacio()));
		Long id = Long.decode(espacio.getEdificio());
		e.setEdificio(edificio_service.findEdificio(id));
		e.setImagen(attachment);
		e.setTipoAutorizacion(Autorizacion.fromEstadoReserva(espacio.getTipoAutorizacion()));
		e.setHorasAutorizacion(espacio.getHorasAutorizacion());
		return espacio_repository.save(e);
	}
	
	public Espacio addNewEspacio(Espacio espacio){
		Espacio newEspacio = new Espacio(espacio.getNombreEspacio(),espacio.getCapacidad(), espacio.isMicrofono(), espacio.isProyector(), espacio.getTipoEspacio()); 
				//TipoEspacio.fromTipoEspacio(espacio.getTipoEspacio()), edificio_repository.findOne(espacio.getIdEdificio()));
		newEspacio = espacio_repository.save(newEspacio);
		
		return null;
	}
	
	public Espacio addNewEspacio(EspacioDTO f){
		TipoEspacio tipoEspacio = TipoEspacio.fromTipoEspacio(f.getTipoEspacio());
		Espacio newEspacio = new Espacio(f.getNombreEspacio(),f.getCapacidad(), tipoEspacio); 

		newEspacio.setEdificio(edificio_service.getEdificio(1));
		newEspacio.setImagen(attachment_repository.findOne((long) 1));
		newEspacio = espacio_repository.save(newEspacio);
		
		return newEspacio;
	}

	public List<TipoEspacio> tiposDeEspacios(long idEdificio) {
		return espacio_repository.tiposDeEspacios(idEdificio);
	}

	public List<Espacio> getEspaciosEliminados() {
		
		return espacio_repository.recycleBin();
	}
	
	public Espacio restaurarEspacio(Long idEspacio) {
		Espacio e = espacio_repository.findOne(idEspacio);
		e.setDeleted(false);		
		return espacio_repository.save(e);
		
	}

	public List<Attachment> getAttachmentByName(String imagen) {
		return attachment_repository.getAttachmentByName(imagen);
	}

	public List<Edificio> getEdificiosPorTagName(String tagName) {
		
		return edificio_service.getEdificiosPorTagName(tagName);
	}

	public Page<Espacio> getEspaciosPorNombre(String nombre, Pageable pagerequest) {
		// TODO Auto-generated method stub
		return espacio_repository.getEspaciosByTagName(nombre,pagerequest);
	}

	public List<Espacio> getEspaciosPorNombre(String nombre) {
		// TODO Auto-generated method stub
		return espacio_repository.getEspaciosByTagName(nombre);
	}
	
	public Page<Espacio> getEspaciosPorEdificio(String tagName, Pageable pagerequest) {
		// TODO Auto-generated method stub
		return espacio_repository.getEspaciosPorEdificio(tagName, pagerequest);
	}
	
	public List<Espacio> getEspaciosPorEdificio(String tagName) {
		// TODO Auto-generated method stub
		return espacio_repository.getEspaciosPorEdificio(tagName);
	}
	
	public Page<Espacio> getEspaciosEliminadosPorNombre(String nombre, Pageable pagerequest) {
		// TODO Auto-generated method stub
		return espacio_repository.getEspaciosEliminadosByTagName(nombre,pagerequest);
	}

	public Page<Espacio> getEspaciosEliminadosPorEdificio(String tagName, Pageable pagerequest) {
		// TODO Auto-generated method stub
		return espacio_repository.getEspaciosEliminadosPorEdificio(tagName, pagerequest);
	}
	
	public Page<Espacio> getEspaciosPorNombreYFacultad(String nombre, Long id, Pageable pagerequest) {
		// TODO Auto-generated method stub
		return espacio_repository.getEspaciosByTagNameAndFacultad(nombre, id,pagerequest);
	}

	public Page<Espacio> getEspaciosPorEdificioYFacultad(String tagName, Long id, Pageable pagerequest) {
		// TODO Auto-generated method stub
		return espacio_repository.getEspaciosPorEdificioYFacultad(tagName, id, pagerequest);
	}
	
	public Page<Espacio> getEspaciosEliminadosPorNombreYFacultad(String nombre, Long id, Pageable pagerequest) {
		// TODO Auto-generated method stub
		return espacio_repository.getEspaciosEliminadosByTagNameAndFacultad(nombre, id, pagerequest);
	}

	public Page<Espacio> getEspaciosEliminadosPorEdificioYFacultad(String tagName, Long id, Pageable pagerequest) {
		// TODO Auto-generated method stub
		return espacio_repository.getEspaciosEliminadosPorEdificioYFacultad(tagName, id, pagerequest);
	}
	
	
	public Espacio findEspacio(long espacioid)
	{
		return espacio_repository.findEspacio(espacioid);
	}
	
	public Page<Espacio> findEspacioByFacultadId(long facultadid, Pageable pageable)
	{
		return espacio_repository.findEspacioByFacultadId(facultadid, pageable);
	}
	
	public Page<Espacio> findEspacioDeletedByFacultadId(long facultadid, Pageable pageable)
	{
		return espacio_repository.findEspacioDeletedByFacultadId(facultadid, pageable);
	}

}
