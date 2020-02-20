package org.com.enlopes.api.user.role;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role,Long> {

	List<Role> findByNome(String nome);
   
}
