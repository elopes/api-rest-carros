package org.com.enlopes.api.carro;

import org.modelmapper.ModelMapper;

public class CarroDto {

	private Long id;

	private String nome;
	private String tipo;

	//public CarroDto(Carro c) {
		//this.id = c.getId();
		//this.nome = c.getNome();
		//this.tipo = c.getTipo();
	//}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	public static CarroDto create(Carro carro) {
		ModelMapper modelMapper = new ModelMapper();
		return modelMapper.map(carro, CarroDto.class);
	}

}
