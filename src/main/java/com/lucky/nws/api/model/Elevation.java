package com.lucky.nws.api.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
/**
 * @author Lakshmipriya
 *
 */
@Data
@NoArgsConstructor
@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
public class Elevation {
	@JsonProperty("value")
	private double value;
	
	@JsonProperty("unitCode")
	private String unitCode;
}
