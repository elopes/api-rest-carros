/**
 * 
 */
package org.com.enlopes.api.carro;

import java.net.URI;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

/**
 * @author Elias
 *
 */
@RestController
@RequestMapping("/api/v1/carros")
public class CarroController {
	@Autowired
	private CarroService service;

	@GetMapping
	public ResponseEntity<List<CarroDto>> get() {
		// return ResponseEntity.ok(service.getCarros());
		// return new ResponseEntity<>(service.getCarros(),HttpStatus.OK);
		return ResponseEntity.status(HttpStatus.OK).body(service.getCarros());
	}

	@GetMapping("/{id}")
	public ResponseEntity<?> get(@PathVariable("id") Long id) {
		Optional<CarroDto> carro = service.getCarrosById(id);

		// return carro.isPresent() ? ResponseEntity.ok(carro.get()) :
		// ResponseEntity.notFound().build();

		return carro.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());

		// if (carro.isPresent()) {
		// return ResponseEntity.ok(carro.get());
		// } else {
		// return ResponseEntity.notFound().build();
		// }
	}

	@GetMapping("tipo/{tipo}")
	public ResponseEntity<List<CarroDto>> getCarrosByTipo(@PathVariable("tipo") String tipo) {
		List<CarroDto> carros = service.getCarrosByTipo(tipo);
		return carros.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(carros);
	}

	@PostMapping
	@Secured({"ROLE_ADMIN"})
	public ResponseEntity<?> post(@RequestBody Carro carro) {

		try {
			CarroDto c = service.insert(carro);
			URI location = getUri(c.getId());
			return ResponseEntity.created(location).build();
		} catch (Exception e) {
			return ResponseEntity.badRequest().build();
		}
	}

	private URI getUri(Long id) {
		return ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(id).toUri();

	}

	@PutMapping("/{id}")
	public ResponseEntity<?> put(@PathVariable("id") Long id, @RequestBody Carro carro) {
		CarroDto c = service.update(carro, id);
		return c != null ? ResponseEntity.ok(c) : ResponseEntity.notFound().build();
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<?> delete(@PathVariable("id") Long id) {
		boolean ok = service.delete(id);

		return ok ? ResponseEntity.ok().build() : ResponseEntity.notFound().build();

	}

}
