package com.traveler.print;
import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller("/print")
public class PrintController {
	@ResponseBody
	@RequestMapping("/hello")
	public Object test(){
		Map<String,Object> map=new HashMap<>();
		map.put("id", 3);
		map.put("name", "你好");
		return map;
	}
	
}
