package com.bread.app.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/map")
public class MapController {
	
	@GetMapping("/map.do")
	public String map() {
		return "map/map";
	}
}
