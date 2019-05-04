package jet.travels.transportManager.repositoryServices;

import java.util.List;

import jet.travels.transportManager.CommonReportModelSmall;
import jet.travels.transportManager.model.TransportNightstops;
import jet.travels.transportManager.model.TransportStatus;
import jet.travels.transportManager.model.TransportVehiclerequesitions;

public interface masterDataServiceDao {
	
	/*Transport Vehicle Requestions*/
	public List<TransportVehiclerequesitions> getVehicleRequestionList();
	public String getVehicleRequestionList_ServerSideProcessing(String draw,String start,String length,String search);
	public String getVehicleRequestionList_ServerSideProcessingByVehicle(String draw,String start,String length,String search,String searchBy,String searchNo);
	public String getVehicleRequestionList_ServerSideProcessingByAllReq(String draw,String start,String length,String search,String vehiType,String status,String from,String to);
	public String getVehicleRequestsServerProcessedByHandleTr(String draw,String start,String length,String search,String vehiType,String market,String from,String to,String reqNo,String tourNo);
	public CommonReportModelSmall getVehicleRequestDetails(String idvehreq);
	
	/*Transport Status*/
	public TransportStatus getStatusByName(String name);
	
	/*Transport Night Stops*/
	public List<TransportNightstops> getTransportNightStopsByVehicleReq(String night);
	
}
