package org.com.enlopes.api.user.role;

import org.modelmapper.ModelMapper;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.Data;

@Data
public class RoleDto {

	private Long id;
	private String nome;

	public static RoleDto create(Role role) {
		ModelMapper modelMapper = new ModelMapper();
		RoleDto dto = modelMapper.map(role, RoleDto.class);

		return dto;
	}

	public String toJson() throws JsonProcessingException {
		ObjectMapper m = new ObjectMapper();
		return m.writeValueAsString(this);
	}
}
