package mx.com.gm.web;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;

import javax.mail.MessagingException;
import javax.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import mx.com.gm.dao.UsuarioDao;
import mx.com.gm.domain.Aliado;
import mx.com.gm.domain.Descuento;
import mx.com.gm.domain.Examen;
import mx.com.gm.domain.Formulario;
import mx.com.gm.domain.FormularioDoctor;
import mx.com.gm.domain.FormularioTecnico;
import mx.com.gm.domain.Persona;
import mx.com.gm.domain.Rol;
import mx.com.gm.domain.Usuario;
import mx.com.gm.servicio.AliadoService;
import mx.com.gm.servicio.DescuentoService;
import mx.com.gm.servicio.DolarService;
import mx.com.gm.servicio.ExamenService;
import mx.com.gm.servicio.FormularioService;
import mx.com.gm.servicio.PersonaService;
import mx.com.gm.util.Constantes;
import mx.com.gm.util.EmailService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;

@Controller
@Slf4j
public class ControladorInicio {

	@Autowired
	private PersonaService personaService;

	@Autowired
	private AliadoService aliadoService;

	@Autowired
	private ExamenService examenService;

	@Autowired
	private DescuentoService descuentoService;

	@Autowired
	private FormularioService formularioService;

	@Autowired
	private UsuarioDao usuarioDao;

	@Autowired
	private DolarService dolarService;

	private final String UPLOAD_DIR = "C://imagenesTemporales/";

	@GetMapping("/")
	public String inicio(Model model, @AuthenticationPrincipal User user) {
		Usuario usuario = usuarioDao.findByUsername(user.getUsername());
		List<Rol> roles = usuario.getRoles();
		if (roles.get(0).getNombre().equals("ROLE_TEC")) {
			return extraerIndexTecnico(model, usuario, Constantes.ASIGNAR_TECNICO);
		}
		if (roles.get(0).getNombre().equals("ROLE_DOC")) {
			return extraerIndexDoctor(model, usuario, Constantes.ASIGNAR_DOCTOR);
		}
		List<Persona> personas = personaService.listarPersonas(Sort.by(Order.desc("idPersona")));

		log.info("ejecutando el controlador Spring MVC");
		log.info("usuario que hizo login:" + user);
		model.addAttribute("personas", personas);
		double saldoTotal = 0D;
		for (Persona p : personas) {
			saldoTotal += p.getSaldo();
		}
		model.addAttribute("saldoTotal", saldoTotal);
		model.addAttribute("totalClientes", personas.size());
		model.addAttribute("mensaje", "sin mensaje");
		return "index";
	}

	private String extraerIndexTecnico(Model model, Usuario usuario, String tipoFiltro) {
		List<FormularioTecnico> formTec = new ArrayList<FormularioTecnico>();
		List<Formulario> form = formularioService.listarFormularioTecnico(String.valueOf(usuario.getIdUsuario()));
		for (Formulario formulario : form) {
			if (formulario.getEstatus().equals(tipoFiltro)) {
				Examen exa = examenService.encontrarExamen(formulario.getIdExamen());
				if (exa != null) {
					formTec.add(new FormularioTecnico(formulario.getIdFormulario(), formulario.getCedula(),
							formulario.getIdTecnico(), exa.getNombre(), formulario.getIdExamen(),
							formulario.getEstatus()));
				}
			}
		}
		model.addAttribute("personas", formTec);
		model.addAttribute("totalClientes", formTec.size() + 1);
		return "indexTecnico";
	}

	private String extraerIndexDoctor(Model model, Usuario usuario, String tipoFiltro) {
		List<FormularioDoctor> formDoc = new ArrayList<FormularioDoctor>();
		List<Formulario> form = formularioService.listarFormularioDoctor(String.valueOf(usuario.getIdUsuario()));
		for (Formulario formulario : form) {
			if (formulario.getEstatus().equals(tipoFiltro)) {
				Examen exa = examenService.encontrarExamen(formulario.getIdExamen());
				if (null != exa) {
					formDoc.add(new FormularioDoctor(formulario.getIdFormulario(), formulario.getCedula(),
							formulario.getIdDoctor(), exa.getNombre(), formulario.getIdExamen(),
							formulario.getEstatus(), formulario.getRutaExamen()));
				}
			}
		}
		model.addAttribute("personas", formDoc);
		model.addAttribute("totalClientes", formDoc.size());
		return "indexDoctor";
	}

	// **********************PERSONAS*************************//
	@GetMapping("/agregar")
	public String agregar(Persona persona) {
		return "modificar";
	}

	@PostMapping("/guardar")
	public String guardar(@Valid Persona persona, Errors errores) {
		if (errores.hasErrors()) {
			return "modificar";
		}
		if (null == persona.getSaldo()) {
			persona.setSaldo(0.0);
		}
		persona.setNombre(
				persona.getNombre().substring(0, 1).toUpperCase() + persona.getNombre().substring(1).toLowerCase());
		persona.setApellido(
				persona.getApellido().substring(0, 1).toUpperCase() + persona.getApellido().substring(1).toLowerCase());
		personaService.guardar(persona);
		return "redirect:/";
	}

	@GetMapping("/editar/{idPersona}")
	public String editar(Persona persona, Model model) {
		persona = personaService.encontrarPersona(persona);
		model.addAttribute("persona", persona);
		return "modificar";
	}

	@GetMapping("/agregarFormulario/{idPersona}")
	public String agregarFormulario(Persona persona, Model model) {
		persona = personaService.encontrarPersona(persona);
		List<Aliado> aliado = aliadoService.listarAliados();
		List<Examen> estudios = examenService.listarExamen();
		List<Usuario> tecnicos = usuarioDao.findAllByRoles("ROLE_TEC");
		List<Usuario> doctores = usuarioDao.findAllByRoles("ROLE_DOC");
		model.addAttribute("tecnicos", tecnicos);
		model.addAttribute("doctores", doctores);
		model.addAttribute("aliadosComerciales", aliado);
		model.addAttribute("estudiosCargados", estudios);
		model.addAttribute("persona", persona);
		return "modificarFormulario";
	}

	@GetMapping("/generarFactura/{idPersona}")
	public String generarFactura(Persona persona, Model model) {

		Persona pers = personaService.encontrarPersona(persona);

		List<Formulario> form = formularioService.listarFormularioUsuario(pers.getCedula());
		int precioDolar = dolarService.precioDolar();
		List<Examen> examen = new ArrayList<Examen>();
		int total = 0;
		for (Formulario formulario : form) {
			if (formulario.getFacturado() == 0) {
				Examen enconExamen = examenService.encontrarExamen(formulario.getIdExamen());
				if (null != enconExamen) {
					Descuento descuento = descuentoService.sacarDescuentoIndividual(
							String.valueOf(enconExamen.getIdExamen()), formulario.getIdAliado());
					float precioTotal = 0;
					float descuentoTotal = (float) (enconExamen.getPrecio()
							* (Float.valueOf(descuento.getDescuento()) / 100));
					precioTotal = enconExamen.getPrecio() - descuentoTotal;
					total += (precioTotal * precioDolar);
					enconExamen.setPrecio(precioTotal);
					examen.add(enconExamen);
					formulario.setFacturado(1);
					formularioService.guardar(formulario);

				}
			}
		}
		float iva = (float) (0.15 * total);
		float subtotal = total - iva;
//		
		model.addAttribute("total", total);
		model.addAttribute("subtotal", subtotal);
		model.addAttribute("iva", iva);
		model.addAttribute("precioDolar", precioDolar);
		model.addAttribute("persona", pers);
		model.addAttribute("examen", examen);
		if (examen.size() < 1) {
			model.addAttribute("mensaje", examen);
			return "redirect:/";
		}
		return "factura";
	}

	@GetMapping("/eliminar")
	public String eliminar(Persona persona) {
		personaService.eliminar(persona);
		return "redirect:/";
	}

	// **********************ALIADOS*************************//
	@GetMapping("/indexAliado")
	public String inicioAliado(Model model, @AuthenticationPrincipal User user) {
		List<Aliado> aliado = aliadoService.listarAliados();
		model.addAttribute("aliadoComercial", aliado);
		model.addAttribute("totalAliados", aliado.size());
		return "indexAliado";
	}

	@GetMapping("/agregarAliados")
	public String agregar(Aliado aliados) {
		return "modificarAliado";
	}

	@PostMapping("/guardarAliado")
	public String guardarAliado(@Valid Aliado aliado, Errors errores) {
		if (errores.hasErrors()) {
			return "modificarAliado";
		}
		aliadoService.guardar(aliado);
		return "redirect:/indexAliado";
	}

	@GetMapping("/editarAliado/{idAliado}")
	public String editarAliado(Aliado aliado, Model model) {
		aliado = aliadoService.encontrarAliados(aliado);
		model.addAttribute("aliado", aliado);
		return "modificarAliado";
	}

	@GetMapping("/eliminarAliado")
	public String eliminarAliado(Aliado aliado) {
		aliadoService.eliminar(aliado);
		return "redirect:/indexAliado";
	}

	// **********************EXAMENES*************************//
	@GetMapping("/indexExamen")
	public String inicioExamen(Model model, @AuthenticationPrincipal User user) {
		List<Examen> examen = examenService.listarExamen();
		List<Aliado> aliado = aliadoService.listarAliados();

		model.addAttribute("aliadosComerciales", aliado);
		model.addAttribute("examenComercial", examen);
		model.addAttribute("totalExamen", examen.size());
		return "indexExamen";
	}

	@GetMapping("/agregarExamen")
	public String agregar(Examen examen) {
		return "modificarExamen";
	}

	@PostMapping("/guardarExamen")
	public String guardarExamen(Model model, @Valid Examen examen, Errors errores) {

		if (errores.hasErrors()) {
			return "modificarExamen";
		}
		examenService.guardar(examen);
		return "modificarExamen";
	}

	@GetMapping("/editarExamen/{idExamen}")
	public String editarExamen(Examen examen, Model model) {
		examen = examenService.encontrarExamen(examen);
		model.addAttribute("examen", examen);
		return "modificarExamen";
	}

	@GetMapping("/eliminarExamen")
	public String eliminarExamen(Examen examen) {
		examenService.eliminar(examen);
		return "redirect:/indexExamen";
	}

	/**************************** FORMULARIO *************************/

	@PostMapping("/guardarFormulario")
	public String guardarFormulario(@Valid Formulario formulario, Errors errores, Model model) {
		formularioService.guardar(formulario);
		model.addAttribute("formulario", formulario);
		return "redirect:/";
	}

	@PostMapping("/guardarObservacion")
	public String guardarObservacion(@Valid Formulario formulario, Errors errores, Model model) {

		Formulario formObservaciones = formularioService.encontrarFormulario(formulario.getIdFormulario());
		formObservaciones.setObservaciones(formulario.getObservaciones());
		formObservaciones.setEstatus(Constantes.ASIGNAR_DOCTOR);
		formularioService.guardar(formObservaciones);
		model.addAttribute("formulario", formulario);
		return "redirect:/";
	}

	/******************** CARGAR EXAMEN ***************************/
	@PostMapping("/upload")
	public String uploadFile(@RequestParam("idFormulario") Long idFormulario, @RequestParam("imagen") MultipartFile imagen,
			@RequestParam("informe") MultipartFile informe, RedirectAttributes attributes) {

		// comprobar si el archivo está vacío
		
		if (imagen.isEmpty() || informe.isEmpty()) {
			attributes.addFlashAttribute("KO",
					"Se deben cargar el Examen y el informe");
			return "redirect:/";
		}
		
		// normalizar la ruta del archivo
		String imagenName = StringUtils.cleanPath(imagen.getOriginalFilename());

		String informeName = StringUtils.cleanPath(informe.getOriginalFilename());

		// guardar el archivo en el sistema de archivos local
		try {
			Path pathImagen = Paths.get(UPLOAD_DIR +"/"+ idFormulario + imagenName);
			Path pathInforme = Paths.get(UPLOAD_DIR +"/"+ idFormulario + informeName);
			Files.copy(imagen.getInputStream(), pathImagen, StandardCopyOption.REPLACE_EXISTING);
			Files.copy(informe.getInputStream(), pathInforme, StandardCopyOption.REPLACE_EXISTING);
			Formulario form = formularioService.encontrarFormulario(idFormulario);
			form.setEstatus(Constantes.PENDIETE_ENVIO);
			form.setRutaExamen(pathImagen.toString());
			form.setRutaInforme(pathInforme.toString());
			formularioService.guardar(form);

		} catch (IOException e) {
			e.printStackTrace();
		}

		// return success response
		attributes.addFlashAttribute("OK",
				"Examen e informe subido con éxito");

		return "redirect:/";
	}

	/******************** CARGAR INFORME ***************************/
	@PostMapping("/guardarInforme")
	public String guardarInforme(String idFormulario, String informe, String total, RedirectAttributes attributes) {

		Formulario formulario = formularioService.encontrarFormulario(Long.parseLong(idFormulario));
		formulario.setInforme(informe);
		formulario.setEstatus(Constantes.PENDIETE_ENVIO);
		formularioService.guardar(formulario);

		attributes.addFlashAttribute("message", "Informe guardado con exito");
		return "redirect:/";
	}

	/////////////////////////////// DESCUENTO////////////////////////

	@GetMapping("/indexDescuento")
	public String inicioDescuento(Model model, @AuthenticationPrincipal User user) {
		List<Descuento> descuento = descuentoService.listarDescuento();
		List<Examen> examen = examenService.listarExamen();
		List<Aliado> aliado = aliadoService.listarAliados();

		model.addAttribute("aliadosComerciales", aliado);
		model.addAttribute("examenComercial", examen);
		model.addAttribute("descuentoComercial", descuento);
		model.addAttribute("totalDescuento", descuento.size());
		return "indexDescuento";
	}

	@PostMapping("/guardarDescuento")
	public String guardarDescuento(@Valid Descuento descuento, Errors errores, Model model) {
		List<Examen> examen = examenService.listarExamen();
		List<Aliado> aliado = aliadoService.listarAliados();

		for (Aliado aliado2 : aliado) {
			if (String.valueOf(aliado2.getIdAliado()).equals(descuento.getIdAliado())) {
				descuento.setNombreAliado(aliado2.getNombre());
			}
		}

		for (Examen examen2 : examen) {
			if (String.valueOf(examen2.getIdExamen()).equals(descuento.getIdExamen())) {
				descuento.setNombreExamen(examen2.getNombre());
			}
		}

		descuentoService.guardar(descuento);
		model.addAttribute("descuento", descuento);
		return "redirect:/indexDescuento";
		//return "/layout/respuestaFormularioDescuento";
	}

	@GetMapping("/eliminarDescuento{idDescuento}")
	public String borrarDescuento(@Valid Descuento descuento, Errors errores, Model model) {
		descuentoService.eliminar(descuento);
		model.addAttribute("descuento", descuento);
		return "redirect:/indexDescuento";
	}

	@GetMapping("/enviarExamen/{idFormulario}/{idPersona}")
	public String enviarExamen(@PathVariable Long idFormulario, @PathVariable Persona idPersona) {
		Persona personaEncotnrada=personaService.encontrarPersona(idPersona);
		Formulario formulario = formularioService.encontrarFormulario((idFormulario));
		String examen = formulario.getRutaExamen();
		String informe = formulario.getRutaInforme();
		try {
			EmailService.enviarCorreo(personaEncotnrada.getEmail(), "Correo factura",
					"Esto es un a prueba para decirte que eres hermosa", examen, informe);
		} catch (MessagingException e) {
			e.printStackTrace();
		}

		return "redirect:/";
	}

}
