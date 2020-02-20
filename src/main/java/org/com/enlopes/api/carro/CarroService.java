package org.com.enlopes.api.carro;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

@Service
public class CarroService {

	@Autowired
	private CarroRepository rep;

	public List<CarroDto> getCarros() {
		// List<Carro> carros = rep.findAll();

		// List<CarroDto> list = carros.stream().map(c -> new
		// CarroDto(c)).collect(Collectors.toList());
		return rep.findAll().stream().map(CarroDto::create).collect(Collectors.toList());

		// List<CarroDto> list = new ArrayList<>();
		// for (Carro c : carros) {
		// list.add(new CarroDto(c));
		// }

		// return list;
	}

	/*
	 * public List<Carro> getCarrosFake() { List<Carro> carros = new
	 * ArrayList<Carro>();
	 * 
	 * carros.add(new Carro(1L, "Fusca")); carros.add(new Carro(2L, "Brasilia"));
	 * carros.add(new Carro(3L, "Chevette"));
	 * 
	 * return carros; }
	 */
	public Optional<CarroDto> getCarrosById(Long id) {
		return rep.findById(id).map(CarroDto::create);

	}

	public List<CarroDto> getCarrosByTipo(String tipo) {

		// List<Carro> carros = rep.findByTipo(tipo);
		// List<CarroDto> list = new ArrayList<>();
		// for (Carro c : carros) {
		// list.add(new CarroDto(c));
		// }

		// return list;

		return rep.findByTipo(tipo).stream().map(CarroDto::create).collect(Collectors.toList());
	}

	public CarroDto insert(Carro carro) {
		Assert.isNull(carro.getId(), "Não foi possível inserir o registro");

		return CarroDto.create(rep.save(carro));
	}

	public CarroDto update(Carro carro, Long id) {
		Assert.notNull(id, "Não foi possivel atualizar o registro");

		// Busca o carro no banco de dados
		Optional<Carro> optional = rep.findById(id);
		if (optional.isPresent()) {
			Carro db = optional.get();
			// Copiar as propriedades
			db.setNome(carro.getNome());
			db.setTipo(carro.getTipo());
			System.out.println("Carro id : " + db.getId());

			// Atualiza o Carro
			rep.save(db);

			return CarroDto.create(db);
		} else {
			return null;
			// throw new RuntimeException("Não foi possivel atualizar o registro");
		}
	}

	public boolean delete(Long id) {
		Assert.notNull(id, "Não foi possivel excluir o registro");

		if (getCarrosById(id).isPresent()) {
			rep.deleteById(id);
			return true;
		}
		return false;

	}

}
