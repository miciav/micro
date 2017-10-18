package it.tasd.example.micro;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import it.tasd.example.micro.domain.Sensor;
import it.tasd.example.micro.domain.SensorType;
import it.tasd.example.micro.repository.SensorRepository;
import it.tasd.example.micro.rest.SensorController;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import javax.transaction.Transactional;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = MicroApplication.class)
public class MicroApplicationTests {

	@Autowired
	private SensorController controller;

	@Autowired
	private ObjectMapper objectMapper;

	private MockMvc mvc;

	@MockBean
	private SensorRepository repo;

	@Autowired
	private SensorRepository sensorRepository;

	private List<Sensor> testSensorList;

	@Before
	public void before() {
		mvc = MockMvcBuilders.standaloneSetup(controller).build();
		Sensor sensor1 = new Sensor();
		sensor1.setId(1);
		sensor1.setUuid(UUID.randomUUID().toString());
		sensor1.setDescription("Temperature sensor");
		sensor1.setType(SensorType.Temperature);
		Sensor sensor2 = new Sensor();
		sensor2.setId(2);
		sensor2.setUuid(UUID.randomUUID().toString());
		sensor2.setType(SensorType.Pression);
		sensor2.setDescription("Pression sensor");
		testSensorList = Stream.of(sensor1, sensor2).collect(Collectors.toList());
	}


	@Test
	public void getSensors200() throws Exception {
		when(repo.findAll()).thenReturn(testSensorList);

		MvcResult result = mvc.perform(get("/sensors"))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
				.andReturn();
		verify(repo, times(1)).findAll();
		String jsonString = result.getResponse().getContentAsString();
		List<Sensor> responseSensors = (List<Sensor>) objectMapper.readValue(jsonString, new TypeReference<List<Sensor>>(){});
		assertThat(responseSensors).usingRecursiveFieldByFieldElementComparator().isEqualTo(testSensorList);
	}


	@Test
	public void getSensor200() throws Exception {

		when(repo.findById(testSensorList.get(0).getId())).thenReturn(Optional.of(testSensorList.get(0)));
		MvcResult result = mvc.perform(get("/sensors/{sensorId}", testSensorList.get(0).getId()))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)).andReturn();
		verify(repo, times(1)).findById(testSensorList.get(0).getId());
		String jsonString = result.getResponse().getContentAsString();
		Sensor sensor = (Sensor) objectMapper.readValue(jsonString, Sensor.class);
		assertThat(sensor).isEqualToComparingFieldByField(testSensorList.get(0));
	}

	@Test
	public void testGetSensor204() throws Exception {
		Integer sensorId = testSensorList.stream()
				.max((s1,s2)->s1.getId().compareTo(s2.getId()))
				.get().getId()
				+1;
		String plantId = UUID.randomUUID().toString();
		when(repo.findById(sensorId)).thenReturn(Optional.ofNullable(null));
		MvcResult result = mvc.perform(get("/sensors/{sensorId}", sensorId)).andExpect(status().isNoContent()).andReturn();
		verify(repo, times(1)).findById(sensorId);
	}

}


