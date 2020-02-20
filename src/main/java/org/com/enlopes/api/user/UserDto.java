package org.com.enlopes.api.user;

import java.util.List;
import java.util.stream.Collectors;

import org.com.enlopes.api.user.role.Role;
import org.modelmapper.ModelMapper;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserDto {
	@JsonIgnore
	private Long id;
	private String login;
	private String nome;
	private String email;
	@JsonIgnore
	private String senha;
	// token jwt
	private String token;
	private Long roleid;
	// Lista de Roles
	private List<String> role;
	@JsonIgnore
	private List<Role> roles;

	public static UserDto create(User user) {
		ModelMapper modelMapper = new ModelMapper();
		UserDto dto = modelMapper.map(user, UserDto.class);

		dto.role = user.getRoles().stream().map(Role::getNome).collect(Collectors.toList());

		return dto;
	}

	public static UserDto create(User user, String token) {
		UserDto dto = create(user);
		dto.token = token;
		return dto;
	}

	public String toJson() throws JsonProcessingException {
		ObjectMapper m = new ObjectMapper();
		return m.writeValueAsString(this);
	}

	public static User create(UserDto dto) {
		ModelMapper modelMapper = new ModelMapper();

		return modelMapper.map(dto, User.class);
	}

	public boolean hasRole(String role, List<Role> profile) {

		List<Role> roles = profile;

		for (Role r : roles) {
			System.out.println("============Roles================");
			System.out.println(r.getNome());
			if (role.equals(r.getNome())) {
				return true;
			}

		}
		return false;

	}
}
