package org.com.enlopes.api.user;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.com.enlopes.api.user.role.Role;
import org.com.enlopes.api.user.role.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AuthorizationServiceException;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

@Service
public class UserService {

	@Autowired
	private UserRepository userrepository;
	@Autowired
	private RoleRepository rolerepository;

	// Retorna o usuário autenticado no sistema
	public static User authenticated() {
		try {
			return (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

		} catch (Exception e) {
			return null;
		}

	}

	public List<UserDto> getUsers() {
		return userrepository.findAll().stream().map(UserDto::create).collect(Collectors.toList());
	}

	public Optional<UserDto> getUsersById(Long id) {

		return userrepository.findById(id).map(UserDto::create);

	}

	@Transactional
	public User insert(UserDto dto) {
		Assert.isNull(dto.getId(), "Não foi possível inserir o registro");
		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
		dto.setSenha(encoder.encode(dto.getSenha()));

		User userauthenticated = UserService.authenticated();
		boolean hasrole = dto.hasRole("ROLE_ADMIN", userauthenticated.getRoles());

		Role role;
		if (!hasrole) {
			Long id = (long) 1;
			role = rolerepository.getOne(id);
		} else {
			role = rolerepository.getOne(dto.getRoleid());
		}

		List<Role> roles = new ArrayList<Role>();
		roles.add(role);
		dto.setRoles(roles);

		User newuser = UserDto.create(dto);

		//User postuser = UserDto.create(userrepository.save(newuser));
		User postuser = userrepository.save(newuser);
		return postuser;
	}

	@Transactional
	public UserDto update(User user, Long role_id, Long id) {
		Assert.notNull(id, "Não foi possivel atualizar o registro");

		User userauthenticated = UserService.authenticated();

		UserDto dto = UserDto.create(userauthenticated);

		boolean hasrole = dto.hasRole("ROLE_ADMIN", userauthenticated.getRoles());

		Optional<User> optional = userrepository.findById(id);

		if (optional.isPresent() && userauthenticated != null) {
			User db = optional.get();
			BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

			if (!hasrole && !userauthenticated.getId().equals(db.getId())) {

				throw new AuthorizationServiceException("Acesso Negado");
			} else {

				if (user.getNome() != null)
					db.setNome(user.getNome());

				if (user.getEmail() != null)
					db.setEmail(user.getEmail());

				if (user.getPassword() != null)
					db.setSenha(encoder.encode(user.getPassword()));
			}

			if (hasrole) {
				Optional<Role> opt = rolerepository.findById(role_id);
				Role role = opt.get();
				List<Role> roles = new ArrayList<Role>();
				roles.add(role);
				db.setRoles(roles);
			}

			userrepository.save(db);

			return UserDto.create(db);
		}
		return dto;

	}

	@Transactional
	@Secured({ "ROLE_ADMIN" })
	public boolean delete(Long id) {
		Assert.notNull(id, "Não foi possivel excluir o registro");

		if (getUsersById(id).isPresent()) {
			userrepository.deleteById(id);
			return true;
		}
		return false;

	}

}
