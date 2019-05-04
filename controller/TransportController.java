package jet.travels.transportManager.controller;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import jet.travels.transportManager.CommonReportModelSmall;
import jet.travels.transportManager.model.TransportNightstops;
import jet.travels.transportManager.model.TransportVehiclerequesitions;
import jet.travels.transportManager.repositoryServices.masterDataServiceDao;

@RestController
@CrossOrigin
@RequestMapping("/transport")
public class TransportController {
	@Autowired masterDataServiceDao masterdataservicedao;
	String returnlocation="index.html#!/login/signin";
	
	
	/*Transport Vehicle Requesitions*/
	
	//return requesisition list - server processed for handle transport
	@GetMapping(value = "/getVehicleRequestsServerProcessedByHandleTr")
	@ResponseBody
	public String getVehicleRequestsServerProcessedByHandleTr(@RequestParam("draw") String draw,@RequestParam("start") String start,
			@RequestParam("length") String length,@RequestParam("search[value]") String search,
			@RequestParam("vehicType") String vehicType,@RequestParam("market") String market,@RequestParam("frmDate") String frmDate,@RequestParam("toDate") String toDate,
			@RequestParam("reqNo") String reqNo,@RequestParam("tourNo") String tourNo){
		System.out.println("<<getVehicleRequestsServerProcessedByHandleTr>>");
		String list=masterdataservicedao.getVehicleRequestsServerProcessedByHandleTr(draw, start, length, search, vehicType, market,frmDate,toDate,reqNo,tourNo);
		return list;
	}
}
