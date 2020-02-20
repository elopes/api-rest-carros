package org.com.enlopes.api.user;

import java.net.URI;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {
	@Autowired
	private UserService service;

	@GetMapping()
	@Secured({ "ROLE_ADMIN" })
	public ResponseEntity<List<UserDto>> get() {
		List<UserDto> list = service.getUsers();
		return ResponseEntity.ok(list);
	}

	@GetMapping("/info")
	public static UserDto userInfo(@AuthenticationPrincipal User user) {

		// UserDetails userdetails=JwtUtil.getUserDetails();
		
		return UserDto.create(user);
	}

	@GetMapping("/{id}")
	@Secured({ "ROLE_ADMIN" })
	public ResponseEntity<?> get(@PathVariable("id") Long id) {

		Optional<UserDto> user = service.getUsersById(id);

		return user.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());

	}

	@PostMapping
	public ResponseEntity<Void> post(@RequestBody UserDto dto) {

		try {
			User user = service.insert(dto);
			URI uri = getUri(user.getId());
			return ResponseEntity.created(uri).build();
		} catch (Exception e) {
			return ResponseEntity.badRequest().build();
		}
	}

	// Funciona
	/*
	 * @PostMapping public ResponseEntity<Void> post(@Valid @RequestBody User user)
	 * {
	 * 
	 * try { PostUserDto u = service.insert(user); URI uri = getUri(u.getId());
	 * return ResponseEntity.created(uri).build(); } catch (Exception e) { return
	 * ResponseEntity.badRequest().build(); } }
	 */

	private URI getUri(Long id) {
		return ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(id).toUri();

	}

	@PutMapping("/{id}")
	public ResponseEntity<?> put(@PathVariable("id") Long id, @RequestBody UserDto userdto) {
		
		//sem id
		User user = UserDto.create(userdto);
		
		UserDto userupdated = service.update(user, userdto.getRoleid(), id);
		return userupdated != null ? ResponseEntity.ok(userupdated) : ResponseEntity.notFound().build();
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<?> delete(@PathVariable("id") Long id) {
		boolean ok = service.delete(id);

		return ok ? ResponseEntity.ok().build() : ResponseEntity.notFound().build();

	}

}
