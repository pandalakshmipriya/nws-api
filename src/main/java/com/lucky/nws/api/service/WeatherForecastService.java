/**
 * 
 */
package com.lucky.nws.api.service;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

/**
 * @author Lakshmipriya
 *
 */
@Service
public interface WeatherForecastService {
	Object getPoint(double latitude, double longitude) throws Exception;
	Object getGrid(String office, int gridX,int gridY) throws Exception;
	ResponseEntity<Object> getForeCast(double latitude, double longitude);

}
