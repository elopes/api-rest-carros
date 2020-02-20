/**
 * 
 */
package org.com.enlopes.api.user.role;

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
@RequestMapping("/api/v1/roles")
public class RoleController {
	@Autowired
	private RoleService service;

	@GetMapping
	public ResponseEntity<List<RoleDto>> get() {
		// return ResponseEntity.ok(service.getCarros());
		// return new ResponseEntity<>(service.getCarros(),HttpStatus.OK);
		return ResponseEntity.status(HttpStatus.OK).body(service.getRoles());
	}

	@GetMapping("/{id}")
	public ResponseEntity<?> get(@PathVariable("id") Long id) {
		Optional<RoleDto> role = service.getRoleById(id);

		// return carro.isPresent() ? ResponseEntity.ok(carro.get()) :
		// ResponseEntity.notFound().build();

		return role.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());

		// if (carro.isPresent()) {
		// return ResponseEntity.ok(carro.get());
		// } else {
		// return ResponseEntity.notFound().build();
		// }
	}

	@GetMapping("rolename/{rolename}")
	@Secured({"ROLE_ADMIN"})
	public ResponseEntity<List<RoleDto>> getRolesByNome(@PathVariable("rolename") String nome) {
		List<RoleDto> roles = service.getRolesByNome(nome);
		return roles.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(roles);
	}

	@PostMapping
	@Secured({"ROLE_ADMIN"})
	public ResponseEntity<?> post(@RequestBody Role role) {

		try {
			RoleDto r = service.insert(role);
			URI location = getUri(r.getId());
			return ResponseEntity.created(location).build();
		} catch (Exception e) {
			return ResponseEntity.badRequest().build();
		}
	}

	private URI getUri(Long id) {
		return ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(id).toUri();

	}

	@PutMapping("/{id}")
	@Secured({"ROLE_ADMIN"})
	public ResponseEntity<?> put(@PathVariable("id") Long id, @RequestBody Role role) {
		RoleDto r = service.update(role, id);
		return r != null ? ResponseEntity.ok(r) : ResponseEntity.notFound().build();
	}

	@DeleteMapping("/{id}")
	@Secured({"ROLE_ADMIN"})
	public ResponseEntity<?> delete(@PathVariable("id") Long id) {
		boolean ok = service.delete(id);

		return ok ? ResponseEntity.ok().build() : ResponseEntity.notFound().build();

	}

}
