package jet.travels.transportManager.repositoryServices;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;

import jet.travels.transportManager.CommonReportModelSmall;
import jet.travels.transportManager.model.TransportDriverguidemaster;
import jet.travels.transportManager.model.TransportNightstops;
import jet.travels.transportManager.model.TransportStatus;
import jet.travels.transportManager.model.TransportVehiclerequesitions;
import jet.travels.transportManager.repository.TransportNightStopsDao;
import jet.travels.transportManager.repository.TransportStatusDao;
import jet.travels.transportManager.repository.TransportVehicleRequestsDao;

@Service
public class masterDataServiceImpl implements masterDataServiceDao{
	@Autowired
    private EntityManager em;
	@Autowired
	private Environment env;
	@Autowired
	private TransportVehicleRequestsDao vehiclerequestsdao;
	@Autowired
	private TransportStatusDao statusdao;
	@Autowired
	private TransportNightStopsDao nightstopdao;
	
	
	/*Transport Vehicle Requesisitions*/
	@Override
	public List<TransportVehiclerequesitions> getVehicleRequestionList() {
		
		return vehiclerequestsdao.findAll();
	}
	
	@Override
	public String getVehicleRequestionList_ServerSideProcessing(String draw,String start,String length,String search) {
		List<TransportVehiclerequesitions> reqList=vehiclerequestsdao.findAll();
		if(!reqList.isEmpty()) {
			String data="";
			int filtersize=reqList.size();
			int toindex=(Integer.parseInt(start)+Integer.parseInt(length));
			List<TransportVehiclerequesitions> vehireqSubList=reqList.subList(Integer.parseInt(start),(toindex>reqList.size())?reqList.size():toindex);
			if(!search.isEmpty() & search!=null) {
				vehireqSubList=vehiclerequestsdao.findAll(search);
				filtersize=vehireqSubList.size();
				vehireqSubList=vehireqSubList.subList(Integer.parseInt(start),(toindex>filtersize)?filtersize:toindex);
			}
			//Iterator itr=vehireqSubList.iterator();
			int sublistrows=vehireqSubList.size();
			int index=1;
			for (TransportVehiclerequesitions transportVehiclerequesitions : vehireqSubList) {
				data+=transportVehiclerequesitions.toString();
				if(index<sublistrows) {
					data+=",";
				}
				index++;
			}
			
			String json="{" + 
					"\"draw\": "+draw+"," + 
					"\"recordsTotal\": "+reqList.size()+"," + 
					"\"recordsFiltered\": "+filtersize+"," + 
					"\"data\": [" + 
							data +
					" ]" + 
					"}";
			return json;
		}else {
			return "{\"error\":\"Request Failed\"}";
		}
	}
	
	@Override
	public String getVehicleRequestionList_ServerSideProcessingByVehicle(String draw,String start,String length,String search,String searchBy,String searchNo) {
		try {
			List<TransportVehiclerequesitions> reqList=null;
			if(searchBy.equals(null) | searchNo.equals(null)) {
				System.out.println("first card");
				reqList=vehiclerequestsdao.findAll();
			}else {
				System.out.println("advanced search---"+searchBy+"--"+searchNo);
				if(searchBy.isEmpty() | searchNo.isEmpty()) {
					reqList=vehiclerequestsdao.findAll();
				}else if(searchBy.equals("vhno")){
					final String uri = env.getProperty("app.repoService").concat("transport/vehicleUuidByRegNo/"+searchNo);
				    RestTemplate restTemplate = new RestTemplate();
				    List<String> result = restTemplate.getForObject(uri, List.class);
					System.out.println("vhnoo---"+result.size());
					reqList=vehiclerequestsdao.findByVehicleIdIn(result);
				}else if(searchBy.equals("logno")){
					final String uri = env.getProperty("app.repoService").concat("transport/logSheetUuidBylogNo/"+searchNo);
				    RestTemplate restTemplate = new RestTemplate();
				    List<String> result = restTemplate.getForObject(uri, List.class);
				    System.out.println("logno---"+result.size());
					reqList=vehiclerequestsdao.findByTrconfirmationUuidIn(result);
				}else if(searchBy.equals("tourno")){
					System.out.println("tourno");
					reqList=vehiclerequestsdao.findByTourNoContaining(searchNo);
				}else if(searchBy.equals("reqno")){
					System.out.println("reqno");
					reqList=vehiclerequestsdao.findByRequisitionNo(Long.parseLong(searchNo));
				}else {
					System.out.println("wildcard");
					reqList=vehiclerequestsdao.findAll();
				}
			}
			if(!reqList.isEmpty()) {
				String data="";
				int filtersize=reqList.size();
				int toindex=(Integer.parseInt(start)+Integer.parseInt(length));
				List<TransportVehiclerequesitions> vehireqSubList=reqList.subList(Integer.parseInt(start),(toindex>reqList.size())?reqList.size():toindex);
				if(!search.isEmpty() & search!=null) {
					vehireqSubList=vehiclerequestsdao.findAll(search);
					filtersize=vehireqSubList.size();
					vehireqSubList=vehireqSubList.subList(Integer.parseInt(start),(toindex>filtersize)?filtersize:toindex);
				}
				//Iterator itr=vehireqSubList.iterator();
				int sublistrows=vehireqSubList.size();
				int index=1;
				for (TransportVehiclerequesitions transportVehiclerequesitions : vehireqSubList) {
					data+=transportVehiclerequesitions.toString();
					if(index<sublistrows) {
						data+=",";
					}
					index++;
				}
				
				String json="{" + 
						"\"draw\": "+draw+"," + 
						"\"recordsTotal\": "+reqList.size()+"," + 
						"\"recordsFiltered\": "+filtersize+"," + 
						"\"data\": [" + 
								data +
						" ]" + 
						"}";
				return json;
			}else {
				String json="{" + 
						"\"draw\": "+draw+"," + 
						"\"recordsTotal\": "+reqList.size()+"," + 
						"\"recordsFiltered\": "+0+"," + 
						"\"data\": [" +
						" ]" + 
						"}";
				return json;
			}
		}catch (NumberFormatException e) {
			//e.printStackTrace();
			return "{\"error\":\"Invalid No\"}";
		}catch (Exception e) {
			e.printStackTrace();
			return "{\"error\":\"No Records Found\"}";
		}
	}
	
	@Override
	public String getVehicleRequestionList_ServerSideProcessingByAllReq(String draw,String start,String length,String search,String vehiType,String status,String from,String to) {
		try {
			List<TransportVehiclerequesitions> reqList=null;
			SimpleDateFormat sft=new SimpleDateFormat("yyyy-MM-dd");
			if(from.equals(null) & to.equals(null)) {
				System.out.println("first card");
				reqList=vehiclerequestsdao.findAll();
			}else {
				if(!status.isEmpty() | !vehiType.isEmpty()) {
					if(!status.isEmpty() & !vehiType.isEmpty()) {
						System.out.println("advanced search---"+status+"--"+vehiType);
						TransportStatus stat=statusdao.findByName(status);
						reqList=vehiclerequestsdao.findByRequestedVehicleAndTransportStatusByConfirmedAndReportDateTimeBetween(vehiType, stat,sft.parse(from), sft.parse(to));
					}else if(!status.isEmpty()) {
						System.out.println("advanced search---"+status);
						TransportStatus stat=statusdao.findByName(status);
						reqList=vehiclerequestsdao.findByTransportStatusByConfirmedAndReportDateTimeBetween(stat, sft.parse(from), sft.parse(to));
					}else {
						System.out.println("advanced search---"+vehiType);
						reqList=vehiclerequestsdao.findByRequestedVehicleAndReportDateTimeBetween(vehiType, sft.parse(from), sft.parse(to));
					}
				}else {
					System.out.println("wildcard");
					reqList=vehiclerequestsdao.findByReportDateTimeBetween(sft.parse(from), sft.parse(to));
				}
			}
			if(!reqList.isEmpty()) {
				String data="";
				int filtersize=reqList.size();
				int toindex=(Integer.parseInt(start)+Integer.parseInt(length));
				List<TransportVehiclerequesitions> vehireqSubList=reqList.subList(Integer.parseInt(start),(toindex>reqList.size())?reqList.size():toindex);
				if(!search.isEmpty() & search!=null) {
					vehireqSubList=vehiclerequestsdao.findAll(search);
					filtersize=vehireqSubList.size();
					vehireqSubList=vehireqSubList.subList(Integer.parseInt(start),(toindex>filtersize)?filtersize:toindex);
				}
				//Iterator itr=vehireqSubList.iterator();
				int sublistrows=vehireqSubList.size();
				int index=1;
				for (TransportVehiclerequesitions transportVehiclerequesitions : vehireqSubList) {
					data+=transportVehiclerequesitions.toString();
					if(index<sublistrows) {
						data+=",";
					}
					index++;
				}
				
				String json="{" + 
						"\"draw\": "+draw+"," + 
						"\"recordsTotal\": "+reqList.size()+"," + 
						"\"recordsFiltered\": "+filtersize+"," + 
						"\"data\": [" + 
								data +
						" ]" + 
						"}";
				return json;
			}else {
				String json="{" + 
						"\"draw\": "+draw+"," + 
						"\"recordsTotal\": "+reqList.size()+"," + 
						"\"recordsFiltered\": "+0+"," + 
						"\"data\": [" +
						" ]" + 
						"}";
				return json;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return "{\"error\":\"No Records Found\"}";
		}
	}
	
	@Override
	public String getVehicleRequestsServerProcessedByHandleTr(String draw,String start,String length,String search,String vehiType,String market,String from,String to,String reqNo,String tourNo) {
		try {
			List<TransportVehiclerequesitions> reqList=null;
			SimpleDateFormat sft=new SimpleDateFormat("yyyy-MM-dd");
			Date fromd=null,tod=null;
			if(vehiType.isEmpty() & market.isEmpty() & from.isEmpty() & to.isEmpty() & tourNo.isEmpty() & reqNo.isEmpty()) {
				System.out.println("first card");
				reqList=vehiclerequestsdao.findAll();
			}else {
				vehiType=(vehiType.isEmpty() | vehiType.equals("null"))?null:vehiType;
				market=(market.isEmpty() | market.equals("null"))?null:market;
				fromd=(from.isEmpty() | from.equals("null"))?null:sft.parse(from);
				tod=(to.isEmpty() | to.equals("null"))?null:sft.parse(to);
				reqNo=(reqNo.isEmpty() | reqNo.equals("null"))?"0":reqNo;
				tourNo=(tourNo.isEmpty() | tourNo.equals("null"))?null:tourNo;
				
				System.out.println("advanced search---"+vehiType+"--"+market+"--"+from+"--"+to+"--"+tourNo+"--"+reqNo);
				
				reqList=vehiclerequestsdao.findHandleTransport(null,vehiType, market, fromd, tod, Long.parseLong(reqNo), tourNo);
			}
			if(!reqList.isEmpty()) {
				String data="";
				int filtersize=reqList.size();
				int toindex=(Integer.parseInt(start)+Integer.parseInt(length));
				List<TransportVehiclerequesitions> vehireqSubList=reqList.subList(Integer.parseInt(start),(toindex>reqList.size())?reqList.size():toindex);
				if(!search.isEmpty() & search!=null) {
					vehireqSubList=vehiclerequestsdao.findHandleTransport(search,vehiType, market, fromd, tod, Long.parseLong(reqNo), tourNo);
					filtersize=vehireqSubList.size();
					vehireqSubList=vehireqSubList.subList(Integer.parseInt(start),(toindex>filtersize)?filtersize:toindex);
				}
				//Iterator itr=vehireqSubList.iterator();
				int sublistrows=vehireqSubList.size();
				int index=1;
				for (TransportVehiclerequesitions transportVehiclerequesitions : vehireqSubList) {
					data+=transportVehiclerequesitions.toString();
					if(index<sublistrows) {
						data+=",";
					}
					index++;
				}
				
				String json="{" + 
						"\"draw\": "+draw+"," + 
						"\"recordsTotal\": "+reqList.size()+"," + 
						"\"recordsFiltered\": "+filtersize+"," + 
						"\"data\": [" + 
								data +
						" ]" + 
						"}";
				return json;
			}else {
				String json="{" + 
						"\"draw\": "+draw+"," + 
						"\"recordsTotal\": "+reqList.size()+"," + 
						"\"recordsFiltered\": "+0+"," + 
						"\"data\": [" +
						" ]" + 
						"}";
				return json;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return "{\"error\":\"No Records Found\"}";
		}
	}
	
	@Override
	public CommonReportModelSmall getVehicleRequestDetails(String idvehreq) {
		CommonReportModelSmall sm=new CommonReportModelSmall();
		TransportVehiclerequesitions vehireq=vehiclerequestsdao.findByUuid(idvehreq);
		if(vehireq!=null) {
			if(!vehireq.getVehicleId().isEmpty()) {
				final String uri = env.getProperty("app.repoService").concat("transport/vehicleRegNoByUuid/"+vehireq.getVehicleId());
			    RestTemplate restTemplate = new RestTemplate();
			    String result = restTemplate.getForObject(uri, String.class);
				sm.setRf1((result.equals("null"))?"Select A Vehicle":result);
			}
			TransportDriverguidemaster driver=vehireq.getTransportDriverguidemasterByIdDriver();
			TransportDriverguidemaster guide=vehireq.getTransportDriverguidemasterByIdGuide();
			sm.setRf2((driver==null)?"Select A Driver":driver.getFirstName()+"-"+driver.getLastName());
			sm.setRf3((guide==null)?"Select A Guide":guide.getFirstName()+"-"+guide.getLastName());
		}
		return sm;
	}
	
	
	/*status*/
	@Override
	public TransportStatus getStatusByName(String name) {

		return statusdao.findByName(name);
	}
	
	/*night stops*/
	@Override
	public List<TransportNightstops> getTransportNightStopsByVehicleReq(String night) {
		
		TransportVehiclerequesitions trveh=vehiclerequestsdao.findByUuid(night);
		return nightstopdao.findByTransportVehiclerequesitions(trveh);
	}

}
