package com.example.weatherSearch.controller;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.net.URI;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.example.weatherSearch.entity.model.CityModel;
import com.example.weatherSearch.entity.model.CityPoolModel;
import com.example.weatherSearch.entity.model.WeatherModel;
import com.example.weatherSearch.service.WeatherService;

/**
 * The Class WeatherControllerTest.
 */

@RunWith(MockitoJUnitRunner.class)
public class WeatherControllerTest {
	
	/** The mock mvc. */
	private MockMvc mockMvc;

    /** The weather service. */
    @Mock
    private WeatherService weatherService;

    /** The weather controller. */
    @InjectMocks
    private WeatherController weatherController;
    
    /**
     * Inits the.
     */
    @Before
    public void init(){
        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders
                .standaloneSetup(weatherController)
                .build();
    }
    
    /**
     * Test get city success.
     *
     * @throws Exception the exception
     */
    @Test
    public void test_get_city_success() throws Exception {
    	CityPoolModel cityPoolModel = new CityPoolModel();
    	
    	CityModel cityModel = new CityModel();
    	cityModel.setId("Melbourne");
    	cityModel.setName("墨尔本");
    	cityPoolModel.getCityModel().add(cityModel);

        when(this.weatherService.getCityList()).thenReturn(cityPoolModel);

        final URI uri = URI.create("/api/cities");
        mockMvc.perform(MockMvcRequestBuilders.get(uri))
                .andExpect(status().isOk())
        		.andExpect(content().json("{\"code\":\"888\",\"msg\":\"success\",\"data\":{\"cityModel\":[{\"name\":\"墨尔本\",\"id\":\"Melbourne\"}]}}"));
        verify(weatherService, times(1)).getCityList();
        verifyNoMoreInteractions(weatherService);
    }
    
    @Test
    public void test_get_weather_success() throws Exception {
    	WeatherModel weatherModel = new WeatherModel();
    	weatherModel.setCity("Sydney");
    	weatherModel.setTemperature("29");
    	weatherModel.setUpdateTime("2019-07-13 20:00:00");
    	weatherModel.setWeather("cloudy");
    	weatherModel.setWind("east wind");

        when(this.weatherService.getWeatherByCity("Sydney")).thenReturn(weatherModel);

        final URI uri = URI.create("/api/weather/Sydney");
        mockMvc.perform(MockMvcRequestBuilders.get(uri))
                .andExpect(status().isOk())
        		.andExpect(content().json("{\"code\":\"888\",\"msg\":\"success\",\"data\":{\"city\":\"Sydney\",\"updateTime\":\"2019-07-13 20:00:00\",\"weather\":\"cloudy\",\"temperature\":\"29\",\"wind\":\"east wind\"}}"));
        verify(weatherService, times(1)).getWeatherByCity("Sydney");
        verifyNoMoreInteractions(weatherService);
    }
}
