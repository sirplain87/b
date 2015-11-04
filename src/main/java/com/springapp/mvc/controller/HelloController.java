package com.springapp.mvc.controller;

import com.springapp.mvc.model.Hsrate;
import com.springapp.mvc.model.Price;
import com.springapp.mvc.service.ByslService;
import com.springapp.mvc.service.HsrateStatisticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

@Controller
@RequestMapping("/")
public class HelloController {

	@Autowired
	private HsrateStatisticsService service;

	@Autowired
	private ByslService byslService;

	@ResponseBody
	@RequestMapping(value = "hello/input", method = RequestMethod.POST)
	public void getInput(String ipt){
		System.out.println(ipt);
		service.analiseStore(ipt);
	}

	@ResponseBody
	@RequestMapping(value = "curp", method = RequestMethod.POST)
	public String getCurp(String cd){
		return service.getCurp(cd);
	}

	@ResponseBody
	@RequestMapping(value = "latestp", method = RequestMethod.POST)
	public String getLatestP(String cd){
		return service.getLatestPrice(cd);
	}

	@ResponseBody
	@RequestMapping(value = "dt", method = RequestMethod.GET)
	public String getData(String cd){
		return service.getData(cd);
	}

	@ResponseBody
	@RequestMapping(value = "pr", method = RequestMethod.GET)
	public List<Price> getPrice(String cd){
		return service.getPrice(cd);
	}

	@RequestMapping(value = "loop")
	public void loop(String ipt, HttpServletResponse response){
		service.loop(ipt,response);
	}

	@RequestMapping(value = "test", method = RequestMethod.GET)
	public String test(){
		return "test";
	}

	@RequestMapping(value = "hsrate", method = RequestMethod.GET)
	public ModelAndView hsrate(Long id){
		Hsrate hsrate = service.getHsrateById(id);
		ModelAndView mv = new ModelAndView();
		mv.setViewName("hsrate");
		mv.addObject("hsrate", hsrate);
		return mv;
	}


	@ResponseBody
	@RequestMapping(value = "hsrateOfToday", method = RequestMethod.POST)
	public List<Hsrate> getHsrateofToday(String code)throws Exception
	{
		return service.getHsrateOfToday(code);
	}

	@ResponseBody
	@RequestMapping(value = "hsrttd", method = RequestMethod.POST)
	public String hsrttd(String code)throws Exception{
		return service.hsrttd(code);
	}

	@ResponseBody
	@RequestMapping(value = "hsrtofsmd", method = RequestMethod.POST)
	public String hsrtofsmd(String code, String dt)throws Exception{
		return service.hsrtofsmd(code, dt);
	}

	@ResponseBody
	@RequestMapping(value = "stbyByRate", method = RequestMethod.POST)
	public void stbyByRate(String cd){
		byslService.buyStkByRate(cd);
	}


	@ResponseBody
	@RequestMapping(value = "stbyByDesc", method = RequestMethod.POST)
	public void stbyByDesc(String cd){
		byslService.buyStkByDesc(cd);
	}

	@ResponseBody
	@RequestMapping(value = "stbyBycm", method = RequestMethod.POST)
	public void stbyBycm(String cd, Double m){
		byslService.buyStkByCodeAndMoney(cd, m);
	}

	@ResponseBody
	@RequestMapping(value = "stbyBycp", method = RequestMethod.POST)
	public void stbyBycp(String cd, Double m, Double p){
		byslService.buyStkByCodeAndPrice(cd, m, p);
	}

	@ResponseBody
	@RequestMapping(value = "revert", method = RequestMethod.POST)
	public void revertTd(int id){
		byslService.doRevertTd(id);
	}


	@ResponseBody
	@RequestMapping(value = "stsl", method = RequestMethod.POST)
	public void stsl(String cd){
		byslService.sellStk(cd);
	}

	@ResponseBody
	@RequestMapping(value = "stslByLastPrice", method = RequestMethod.POST)
	public void stslByLastPrice(String cd){
		byslService.stslByLastPrice(cd);
	}

	@ResponseBody
	@RequestMapping(value = "hsrateNotFit", method = RequestMethod.POST)
	public String hsrateNotFit(String start, String end, String comp){
		return service.hsrateNotFit(start, end, comp);
	}

	@ResponseBody
	@RequestMapping(value = "hsrateThatFit", method = RequestMethod.POST)
	public String hsrateThatFit(String start, String end, String comp) {
		return service.hsrateThatFit(start, end, comp);
	}
	@ResponseBody
	@RequestMapping(value = "fisrtHighSecondLow", method = RequestMethod.POST)
	public String fisrtHighSecondLow(){
		return service.fisrtHighSecondLow();
	}

	@ResponseBody
	@RequestMapping(value = "fisrtHighEndSecondLow", method = RequestMethod.POST)
	public String fisrtHighEndSecondLow(){
		return service.fisrtHighEndSecondLow();
	}

	@ResponseBody
	@RequestMapping(value = "fisrtHighEndSecondLowOfOne", method = RequestMethod.POST)
	public String fisrtHighEndSecondLowOfOne(){
		return service.fisrtHighEndSecondLowOfOne();
	}
	@ResponseBody
	@RequestMapping(value = "priceThatFit", method = RequestMethod.POST)
	public String priceThatFit(String time)throws Exception{

		return service.priceThatFit(time);

	}

	@ResponseBody
	@RequestMapping(value = "queryFunds")
	public String queryCurrentMoney(){
		return service.queryCurrentMoney();
	}

	@RequestMapping(value = "display")
	public ModelAndView display()throws Exception{
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("display");
		modelAndView.addObject("displays", service.getMorningp());
		return modelAndView;
	}

	@RequestMapping(value = "pmdisplay")
	public ModelAndView pmdisplay() throws Exception{
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("fdisplay");
		modelAndView.addObject("displays", service.getFivems());
		return modelAndView;
	}


	@RequestMapping(value = "statistics", method = {RequestMethod.POST, RequestMethod.GET} )
	public ModelAndView statistics(String day) throws Exception{

		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("fivemstatistics");
		modelAndView.addObject("displays", day == null ? null : service.statistics(day));
		return modelAndView;
	}

}