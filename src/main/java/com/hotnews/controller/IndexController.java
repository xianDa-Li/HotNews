package com.hotnews.controller;

import com.hotnews.service.HotNewsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.*;
import java.util.*;

/**
 * Author: XianDaLi
 * Date: 2020/8/4 21:33
 * Remark:
 */
//@Controller
public class IndexController {
	private static final Logger logger = LoggerFactory.getLogger(IndexController.class);

	@Autowired
	private HotNewsService hotNewsService;

	@RequestMapping(path = {"/", "/index"})
	@ResponseBody
	public String index(HttpSession session){
		logger.info("Visit Index");
		return "Hello GameZone," + session.getAttribute("msg")
				+ "<br> Say:" + hotNewsService.say();

	}

	@RequestMapping(value = {"/profile/{groupId}/{userId}"})
	@ResponseBody
	public String profile(@PathVariable("groupId") String groupId,
	                      @PathVariable("userId") int userId,
	                      @RequestParam(value = "type",defaultValue = "1") int type,
	                      @RequestParam(value = "key",defaultValue = "nowcoder") String key){
		//
		return String.format("GID{%s},UID{%d},TYPE{%d},KEY{%s}",groupId,userId,type,key);
	}

	@RequestMapping(value = {"/vm"})
	public String news(Model model) {
		model.addAttribute("value1","vv1");
		List<String> colors = Arrays.asList(new String[]{"RED","GREEN","BLUE"});

		Map<String,String> hm = new HashMap<String,String>();
		for (int i = 0; i < 4 ; i++) {
			hm.put(String.valueOf(i),String.valueOf(i*i));
		}

		model.addAttribute("colors",colors);
		model.addAttribute("hm",hm);
//		model.addAttribute("user",new User("zoom"));
		return "news";
	}

	@RequestMapping(value = {"/request"})
	@ResponseBody
	public String request(HttpServletRequest request,
	                      HttpServletResponse response,
                          HttpSession session){
		StringBuilder sb = new StringBuilder();
		Enumeration<String> headerNames = request.getHeaderNames();
		while (headerNames.hasMoreElements()){
			String name = headerNames.nextElement();
			sb.append(name + ":" + request.getHeader(name) + "<br>");
		}

		for(Cookie cookie : request.getCookies()){
			sb.append("Cookie:");
			sb.append(cookie.getName());
			sb.append(":");
			sb.append(cookie.getValue());
			sb.append("<br>");
		}

		sb.append("getMethod:" + request.getMethod()+"<br>");
		sb.append("getPathInfo:" + request.getPathInfo()+"<br>");
		sb.append("getQueryString:" + request.getQueryString()+"<br>");
		sb.append("getRequestURI:" + request.getRequestURI()+"<br>");

		return sb.toString();
	}

	@RequestMapping(value = {"/response"})
	@ResponseBody
	public String response(@CookieValue(value = "GameZoneId",defaultValue = "a")String GameZoneId,
	                       @RequestParam(value = "key",defaultValue = "key") String key,
	                       @RequestParam(value = "value",defaultValue = "value") String value,
	                       HttpServletResponse response){
		response.addCookie(new Cookie(key,value));
		response.addHeader(key,value);
		return "GameZoneId From Cookie:" + GameZoneId;
	}

	@RequestMapping("/redirect/{code}")
	public String redirect(@PathVariable("code") int code,
	                       HttpSession session){
		/*
		RedirectView red = new RedirectView("/",true);
		if(code == 301){
			red.setStatusCode(HttpStatus.MOVED_PERMANENTLY);
		}
		return red;
		 */
		session.setAttribute("msg","Jump from redirect.");
		return "redirect:/";
	}

	@RequestMapping("/admin")
	@ResponseBody
	public String admin(@RequestParam(value = "key",required = false) String key){
		if("admin".equals(key)){
			return "hello admin";
		}
		throw new IllegalArgumentException("key 错误");
	}

	@ExceptionHandler()
	@ResponseBody
	public String error(Exception e){
		return "error:" + e.getMessage();
	}

}
