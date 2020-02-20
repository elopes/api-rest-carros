package org.com.enlopes.api.user.role;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.transaction.Transactional;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

@Service
public class RoleService {

	@Autowired
	private RoleRepository rolerepository;
	
	@Secured({"ROLE_ADMIN"})
	public List<RoleDto> getRoles() {
		return rolerepository.findAll().stream().map(RoleDto::create).collect(Collectors.toList());
	}
	
	@Secured({"ROLE_ADMIN"})
	public Optional<RoleDto> getRoleById(Long id) {

		return rolerepository.findById(id).map(RoleDto::create);

	}

	@Transactional
	@Secured({"ROLE_ADMIN"})
	public RoleDto insert(@Valid Role role) {
		Assert.isNull(role.getId(), "Não foi possível inserir o registro");

		return RoleDto.create(rolerepository.save(role));
	}

	@Secured({"ROLE_ADMIN"})
	public List<RoleDto> getRolesByNome(String nome) {

		return rolerepository.findByNome(nome).stream().map(RoleDto::create).collect(Collectors.toList());
	}

	@Transactional
	@Secured({ "ROLE_ADMIN" })
	public RoleDto update(Role role, Long id) {
		Assert.notNull(id, "Não foi possivel atualizar o registro");

		Optional<Role> optional = rolerepository.findById(id);
		if (optional.isPresent()) {
			Role db = optional.get();

			db.setNome(role.getNome());
			System.out.println("Role id : " + db.getId());

			rolerepository.save(db);

			return RoleDto.create(db);
		} else {
			return null;

		}
	}
	
	@Transactional
	@Secured({ "ROLE_ADMIN" })
	public boolean delete(Long id) {
		Assert.notNull(id, "Não foi possivel excluir o registro");

		if (getRoleById(id).isPresent()) {
			rolerepository.deleteById(id);
			return true;
		}
		return false;

	}

}
