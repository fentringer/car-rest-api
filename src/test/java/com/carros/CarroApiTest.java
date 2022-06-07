package com.carros;

import com.carros.domain.Carro;
import com.carros.domain.dto.CarroDTO;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;


@SpringBootTest(classes = CarrosApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CarroApiTest {

    @Autowired
    protected TestRestTemplate rest;

    private ResponseEntity<CarroDTO> getCarro(String url){
        return rest.getForEntity(url, CarroDTO.class);
    }

    private ResponseEntity<List<CarroDTO>> getCarros(String url){
        return rest.exchange(
                url,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<CarroDTO>>(){}
                );
    }

    @Test
    public void testLista(){
        List<CarroDTO> carros = getCarros("/api/v1/carros").getBody();
        Assertions.assertNotNull(carros);
        Assertions.assertEquals(30, carros.size());
    }

    @Test
    public void testListaPorTipo(){

        Assertions.assertEquals(10, getCarros("/api/v1/carros/tipo/esportivos").getBody().size());
        Assertions.assertEquals(10, getCarros("/api/v1/carros/tipo/luxo").getBody().size());
        Assertions.assertEquals(10, getCarros("/api/v1/carros/tipo/classicos").getBody().size());

        Assertions.assertEquals(HttpStatus.NO_CONTENT, getCarros("/api/v1/carros/tipo/asdasddqw").getStatusCode());
    }

    @Test
    public void testGetCarroNotFound(){
        ResponseEntity rp = getCarros("/api/v1/carros/1029");
        Assertions.assertEquals(HttpStatus.NOT_FOUND, rp.getStatusCode());
    }

    @Test
    public void testSave(){
        Carro c = new Carro();
        c.setNome("uaua");
        c.setTipo("nando");

        //insert
        ResponseEntity rp = rest.postForEntity("/api/v1/carros", c, null);
        System.out.println(rp);

        //verifica se criou
        Assertions.assertEquals(HttpStatus.CREATED, rp.getStatusCode());

        //Busca o objeto
        String location = rp.getHeaders().get("location").get(0);
        Assertions.assertEquals("uaua", getCarro(location).getBody().getNome());
        Assertions.assertEquals("nando", getCarro(location).getBody().getTipo());

        //deleta o objeto
        rest.delete(location);

        //verifica se deletou
        Assertions.assertEquals(HttpStatus.NOT_FOUND, getCarro(location).getStatusCode());
    }





}
