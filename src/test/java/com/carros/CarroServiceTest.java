package com.carros;

import com.carros.domain.Carro;
import com.carros.domain.CarroService;
import com.carros.domain.dto.CarroDTO;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Optional;

import static junit.framework.TestCase.*;


@SpringBootTest
class CarroServiceTest {

	@Autowired
	private CarroService service;

	@Test
	void testSave() {
		Carro carro = new Carro();
		carro.setNome("TESTE1");
		carro.setTipo("esportivos");

		CarroDTO c = service.insert(carro);
		Assertions.assertNotNull(c);

		Long id = c.getId();
		Assertions.assertNotNull(id);

		Optional<CarroDTO> op = service.getCarroById(id);
		Assertions.assertTrue(op.isPresent());

		c = op.get();
		Assertions.assertEquals("TESTE1", c.getNome());
		Assertions.assertEquals("esportivos", c.getTipo());

		service.delete(id);

		Assertions.assertFalse(service.getCarroById(id).isPresent());
	}

	@Test
	void testLista(){

		List<CarroDTO> carros = service.getCarros();
		Assertions.assertEquals(30, carros.size());
	}

	@Test
	void testGet(){

		Optional<CarroDTO> op = service.getCarroById(7L);
		Assertions.assertTrue(op.isPresent());
	}

	@Test
	void testGet2(){

		Optional<CarroDTO> op = service.getCarroById(7L);
		CarroDTO c = op.get();
		Assertions.assertEquals("Ferrari 250 GTO", c.getNome());
	}

	@Test
	void testGetTipo(){

		Assertions.assertEquals(10, service.getCarroByTipo("luxo").size());
		Assertions.assertEquals(10, service.getCarroByTipo("esportivos").size());
		Assertions.assertEquals(10, service.getCarroByTipo("classicos").size());
		Assertions.assertEquals(0, service.getCarroByTipo("x").size());
	}


}
