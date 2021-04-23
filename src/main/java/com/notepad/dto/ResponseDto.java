package com.notepad.dto;

import lombok.Builder;
import lombok.Data;

/**
* The ResponseDto to keep response status and data
*
* @author  Zohaib Ali
* @version 1.0
* @since   2021-04-22 
*/
@Data
@Builder
public class ResponseDto {

	private String status;
	private String data;
	
}
