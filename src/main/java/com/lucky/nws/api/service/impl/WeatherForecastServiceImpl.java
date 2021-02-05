/**
 * 
 */
package com.lucky.nws.api.service.impl;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lucky.nws.api.model.GridRoot;
import com.lucky.nws.api.model.Period;
import com.lucky.nws.api.model.PointRoot;
import com.lucky.nws.api.model.WeatherServiceException;
import com.lucky.nws.api.service.WeatherForecastService;

/**
 * @author Lakshmipriya
 *
 */
@Service
public class WeatherForecastServiceImpl implements WeatherForecastService{
	
	@Autowired
	RestTemplate restTemplate;
	private final String BASE_URL = "https://api.weather.gov";
	private ObjectMapper mapper = new ObjectMapper();
	private final String NEW_LINE = "\n";
	private final String EXP_SERVIE= "Exception in getting data for the requested point!";

	/**
	 * Get the point details 
	 */
	@Override
	public Object getPoint(double latitude, double longitude) throws IOException, InterruptedException {
		ResponseEntity<String> responseEntity = restTemplate.exchange(
				BASE_URL+"/points/"+latitude+","+longitude, 
			    HttpMethod.GET,
			    null,
			    new ParameterizedTypeReference<String>() {
			    });
		String pointObjList = responseEntity.getBody();
		PointRoot pr = null;
		if (pointObjList != null) {
			if(responseEntity.getStatusCode()!=HttpStatus.OK) {
				return mapper.readValue(pointObjList, WeatherServiceException.class);
			}
			pr = mapper.readValue(pointObjList, PointRoot.class);
		}
		return pr;
	}
	/**
	 * Get the Grid details 
	 */
	@Override
	public Object getGrid(String office, int gridX, int gridY) throws IOException, InterruptedException {
		GridRoot gr =null;
		ResponseEntity<String> responseEntity = restTemplate.exchange(
				BASE_URL+"/gridpoints/"+office+"/"+gridX+","+gridY+"/forecast", 
			    HttpMethod.GET,
			    null,
			    new ParameterizedTypeReference<String>() {
			    });
		String gridObjList = responseEntity.getBody();
		if (gridObjList != null) {
			if(responseEntity.getStatusCode()!=HttpStatus.OK) {
				return mapper.readValue(gridObjList, WeatherServiceException.class);
			}
			gr = mapper.readValue(gridObjList, GridRoot.class);
		}
		return gr;
	}
	/**
	 * Get forecast details
	 */
	@Override
	public ResponseEntity<Object> getForeCast(double latitude, double longitude){
		try {
			Object resultObj = getPoint(latitude, longitude);
			if(resultObj instanceof WeatherServiceException) {
				return new ResponseEntity(getExceptionDetail(resultObj),HttpStatus.INTERNAL_SERVER_ERROR);
			}else if(resultObj instanceof PointRoot) {
				PointRoot pr = (PointRoot)resultObj;
				String office = pr.getProperties().getGridId();
				int gridX = pr.getProperties().getGridX();
				int girdY = pr.getProperties().getGridY();
				resultObj = getGrid(office, gridX, girdY);
				if(resultObj instanceof WeatherServiceException) {
					return new ResponseEntity(getExceptionDetail(resultObj),HttpStatus.INTERNAL_SERVER_ERROR);
				}else {
					GridRoot gr = (GridRoot) resultObj;
					List<Period> periods = gr.getProperties().getPeriods();
					return new ResponseEntity(getForeCastDetailsOfDays(periods, 5),HttpStatus.OK);
				}
			}
		}catch(Exception ex) {
			return new ResponseEntity(EXP_SERVIE,HttpStatus.INTERNAL_SERVER_ERROR);
		}

		return null;
	}

	/**
	 * 
	 * @param expObj
	 * @return Exception Object 
	 */
	private String getExceptionDetail(Object expObj) {
		WeatherServiceException wExpObj  = (WeatherServiceException) expObj;
		return wExpObj.getDetail();
	}
	/**
	 * 
	 * @param periods
	 * @param days
	 * @return Result of 5 dyas weather forecast details
	 */
	private String getForeCastDetailsOfDays(List<Period> periods,int days) {
		StringBuffer sb = new StringBuffer();
		sb.append("WeatherForeCast details for the next Five Days")
		.append(NEW_LINE)
		.append("=================================================")
		.append(NEW_LINE);

		for(int i =0;i<days*2;i++) {
			sb.append(periods.get(i).toString())
			.append(NEW_LINE);
		}
		return sb.toString();
	}

}
