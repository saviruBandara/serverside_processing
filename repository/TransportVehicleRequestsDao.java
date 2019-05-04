package jet.travels.transportManager.repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import jet.travels.transportManager.model.TransportStatus;
import jet.travels.transportManager.model.TransportVehiclerequesitions;

@Repository
public interface TransportVehicleRequestsDao extends JpaRepository<TransportVehiclerequesitions, byte[]>{
	public TransportVehiclerequesitions findByUuid(String uuid);
	public List<TransportVehiclerequesitions> findByVehicleIdIn(List<String> id);
	public List<TransportVehiclerequesitions> findByTrconfirmationUuidIn(List<String> uuid);
	public List<TransportVehiclerequesitions> findByRequisitionNo(long uuid);
	public List<TransportVehiclerequesitions> findByTourNoContaining(String uuid);
	public List<TransportVehiclerequesitions> findByReportDateTimeBetween(Date from,Date to);
	public List<TransportVehiclerequesitions> findByRequestedVehicleAndReportDateTimeBetween(String vehicle,Date from,Date to);
	public List<TransportVehiclerequesitions> findByTransportStatusByConfirmedAndReportDateTimeBetween(TransportStatus status,Date from,Date to);
	public List<TransportVehiclerequesitions> findByRequestedVehicleAndTransportStatusByConfirmedAndReportDateTimeBetween(String vehicle,TransportStatus status,Date from,Date to);
	
	final public static String stockcodelistquerySearch="from TransportVehiclerequesitions where "
			+ "requestedVehicle LIKE CONCAT('%',:search,'%') or "
			+ "requestedMarketLabel LIKE CONCAT('%',:search,'%') or "
			+ "requisitionNo LIKE CONCAT('%',:search,'%')";
	@Query(stockcodelistquerySearch)
	public List<TransportVehiclerequesitions> findAll(@Param("search") String search);
	
	final public static String handletransportlistquerySearch="from TransportVehiclerequesitions where "
			+ "(:vehiType is null or requestedVehicle = :vehiType) and "
			+ "(:market is null or requestedMarket = :market) and "
			+ "((:from is null and :to is null) or reportDateTime between :from and :to) and "
			+ "(:tourNo is null or tourNo LIKE CONCAT(:tourNo,'%')) and "
			+ "(:reqNo=0L or requisitionNo LIKE CONCAT(:reqNo,'%')) and "
			+ "(:search is null or (date(reportDateTime) LIKE CONCAT('%',:search,'%') or requestedVehicle LIKE CONCAT('%',:search,'%') or requestedMarketLabel LIKE CONCAT('%',:search,'%') or requisitionNo LIKE CONCAT('%',:search,'%')))";
	@Query(handletransportlistquerySearch)
	public List<TransportVehiclerequesitions> findHandleTransport(@Param("search") String search,@Param("vehiType") String vehiType,@Param("market") String market,@Param("from") Date from,@Param("to") Date to,@Param("reqNo") long reqNo,@Param("tourNo") String tourNo);
	
//	final public static String findSearchByVehicle="from TransportVehiclerequesitions where "
//			+ "(:searchby='vhno' or VehicleId in(:searchlist)) and "
//			+ "(:searchby='logno' or TrconfirmationUuid in(:searchlist)) and "
//			+ "(:searchby='tourno' or tourNo LIKE CONCAT(:tourNo,'%')) and "
//			+ "(:searchby='reqno' or requisitionNo LIKE CONCAT(:reqNo,'%')) and "
//			+ "(:search is null or (date(reportDateTime) LIKE CONCAT('%',:search,'%') or requestedVehicle LIKE CONCAT('%',:search,'%') or requestedMarketLabel LIKE CONCAT('%',:search,'%') or requisitionNo LIKE CONCAT('%',:search,'%')))";
//	@Query(findSearchByVehicle)
//	public List<TransportVehiclerequesitions> findSearchByVehicle(@Param("search") String search,@Param("searchby") String searchby,@Param("searchno") String searchno,@Param("searchlist") List<String> searchlist);
	
}
