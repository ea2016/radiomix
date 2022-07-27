package mx.com.gm.web;

import java.util.List;

import javax.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import mx.com.gm.dao.UsuarioDao;
import mx.com.gm.domain.Aliado;
import mx.com.gm.domain.Examen;
import mx.com.gm.domain.Formulario;
import mx.com.gm.domain.Persona;
import mx.com.gm.domain.Usuario;
import mx.com.gm.servicio.AliadoService;
import mx.com.gm.servicio.ExamenService;
import mx.com.gm.servicio.FormularioService;
import mx.com.gm.servicio.PersonaService;
import mx.com.gm.servicio.UsuarioService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

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
	private FormularioService formularioService;

	@Autowired
	private UsuarioDao usuarioDao;

	@GetMapping("/")
	public String inicio(Model model, @AuthenticationPrincipal User user) {
		List<Persona> personas = personaService.listarPersonas();
		log.info("ejecutando el controlador Spring MVC");
		log.info("usuario que hizo login:" + user);
		model.addAttribute("personas", personas);
		double saldoTotal = 0D;
		for (Persona p : personas) {
			saldoTotal += p.getSaldo();
		}
		model.addAttribute("saldoTotal", saldoTotal);
		model.addAttribute("totalClientes", personas.size());
		return "index";
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
		model.addAttribute("tecnicos", tecnicos);
		model.addAttribute("aliadosComerciales", aliado);
		model.addAttribute("estudiosCargados", estudios);
		model.addAttribute("persona", persona);
		return "modificarFormulario";
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
		model.addAttribute("examenComercial", examen);
		List<Aliado> aliado = aliadoService.listarAliados();
		model.addAttribute("aliadosComerciales", aliado);
		model.addAttribute("totalExamen", examen.size());
		return "indexExamen";
	}

	@GetMapping("/agregarExamen")
	public String agregar(Examen examen) {
		return "modificarExamen";
	}

	@PostMapping("/guardarExamen")
	public String guardarExamen(Model model, @Valid Examen examen, Errors errores) {
		List<Aliado> aliado = aliadoService.listarAliados();
		model.addAttribute("aliadosComerciales", aliado);
		if (errores.hasErrors()) {
			return "modificarExamen";
		}
		examenService.guardar(examen);
		return "redirect:/indexExamen";
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
	public String guardarFormulario(@Valid Formulario formulario, Errors errores) {
		formularioService.guardar(formulario); 
		return "/layout/respuestaFormulario";
	}
}
