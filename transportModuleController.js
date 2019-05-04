'use strict';

var x = window.location.href;
var lastindex = x.lastIndexOf("#!/");
x = x.split(x.slice(lastindex + 3, x.length));
var ldloadingStyle = "zoom-in";

/**
 * controller for reciptmodule
 */
app.controller('transportModule', ['$rootScope', '$scope', '$state', '$localStorage', '$window', '$document', '$timeout', '$uibModal', "$filter", "$compile", 'SweetAlert', 'toaster', 'permissionHandleService', 'transportModuleService',
    function ($rootScope, $scope, $state, $localStorage, $window, $document, $timeout, $uibModal, $filter, $compile, SweetAlert, toaster, permissionHandleService, transportModuleService) {

        if (permissionHandleService.userId) {

            $scope.vehicleTypes = [{"value": "BAG-LOR", "name": "BAG-LOR"},
                {"value": "BAG-VAN", "name": "BAG-VAN"},
                {"value": "BIGCO", "name": "BIGCO"},
                {"value": "CAR", "name": "CAR"},
                {"value": "MICROVAN", "name": "MICROVAN"},
                {"value": "MINI COAH", "name": "MINI COAH"},
                {"value": "NMICRO", "name": "NMICRO"},
                {"value": "SUV", "name": "SUV"}];

            $scope.MarketLists =[];

            //onload functions
            $timeout(function () {
                //Datepicker initialization
                jQuery('#txt_fromDate,#txt_toDate').datepicker({
                    autoclose: true,
                    todayHighlight: true,
                    format: 'yyyy-mm-dd'
                });

                //Datatable intialization
                // $('#transportTable').DataTable({
                //     dom: 'Bfrtip',
                //     buttons: [
                //         'copy', 'csv', 'excel', 'pdf', 'print'
                //     ]
                // });

                //select2
                $('.select2jqu').select2({
                    placeholder: "Select a state"
                });
            });

            //get all recipt entries from the service(search table on right side of the screen)
            

            // transport module report form functions
            $scope.form2 = {
                submit: function (form) {
                    var firstError = null;
                    if (form.$invalid) {
                        var field = null, firstError = null;
                        for (field in form) {
                            if (field[0] != '$') {
                                if (firstError === null && !form[field].$valid) {
                                    firstError = form[field].$name;
                                }

                                if (form[field].$pristine) {
                                    form[field].$dirty = true;
                                }
                            }
                        }

                        angular.element('.ng-invalid[name=' + firstError + ']').focus();
                        SweetAlert.swal("Complete Required Inputs", "", "error");
                        return;
                    } else {
                        if ($scope.transportSearchModel.rptType == "searhbyvehiclesreqs") {
                            if (!$scope.transportSearchModel.searchBy | !$scope.transportSearchModel.searchNo) {
                                SweetAlert.swal("Complete All Inputs", "error");
                            } else {
                                if (jQuery('#transportTable').DataTable()) {
                                    jQuery('#transportTable').DataTable().destroy();
                                }
                                $('#transportTable').DataTable({
                                    "order": [[0, "desc"]],
                                    'responsive': true,
                                    "processing": true,
                                    "serverSide": true,
                                    "ajax": {
                                        "url": transport.getVehicleRequestsServerProcessedByVehicle,
                                        "data": function (d) {
                                            d.searchBy = $scope.transportSearchModel.searchBy;
                                            d.searchNo = $scope.transportSearchModel.searchNo;
                                        }
                                    },
                                    'columns': [
                                        {
                                            "className": 'details-load',
                                            "data": "reportDateTime",
                                            "defaultContent": '',
                                            "render": function (data, type, row) {
                                                return '<span ng-click="tableClick($event)"><i class="ti-car"></i></span>';
                                            }
                                        },
                                        {
                                            "data": "reportDateTime",
                                            "render": function (value) {
                                                if (value === null) return "";
                                                return moment(value).format('YYYY-MM-DD')
                                            }
                                        },
                                        {
                                            "data": "departureFightTime",
                                            "render": function (value) {
                                                if (value === null) return "";
                                                return moment(value).format('YYYY-MM-DD')
                                            }
                                        },
                                        {"data": "requestedVehicle"},
                                        {"data": "requestedMarketLabel"},
                                        {"data": "requisitionNo"}
                                    ],createdRow: function (row, data, dataIndex) {
                                        $compile(angular.element(row).contents())($scope);
                                    }
                                });
                            }
                        } else if ($scope.transportSearchModel.rptType == "searhbyviewallreq") {
                            if (!$scope.transportSearchModel.frmDate | !$scope.transportSearchModel.toDate) {
                                SweetAlert.swal("Complete Required Inputs", "error");
                            } else {
                                if (jQuery('#transportTable').DataTable()) {
                                    jQuery('#transportTable').DataTable().destroy();
                                }
                                $('#transportTable').DataTable({
                                    "order": [[0, "desc"]],
                                    'responsive': true,
                                    "processing": true,
                                    "serverSide": true,
                                    "ajax": {
                                        "url": transport.getVehicleRequestsServerProcessedByAllReqs,
                                        "data": function (d) {
                                            d.vehicType = (!$scope.transportSearchModel.vehicType) ? null : $scope.transportSearchModel.vehicType;
                                            d.searchBy = (!$scope.transportSearchModel.searchBy) ? null : $scope.transportSearchModel.searchBy;
                                            d.frmDate = $scope.transportSearchModel.frmDate;
                                            d.toDate = $scope.transportSearchModel.toDate;
                                        }
                                    },
                                    'columns': [
                                        {
                                            "className": 'details-load',
                                            "data": "reportDateTime",
                                            "defaultContent": '',
                                            "render": function (data, type, row) {
                                                return '<span ng-click="tableClick($event)"><i class="ti-car"></i></span>';
                                            }
                                        },
                                        {
                                            "data": "reportDateTime",
                                            "render": function (value,data) {
                                                if (value === null) return "";
                                                return moment(value).format('YYYY-MM-DD')
                                            }
                                        },
                                        {
                                            "data": "departureFightTime",
                                            "render": function (value) {
                                                if (value === null) return "";
                                                return moment(value).format('YYYY-MM-DD')
                                            }
                                        },
                                        {"data": "requestedVehicle"},
                                        {"data": "requestedMarketLabel"},
                                        {"data": "requisitionNo"}
                                    ],
                                    createdRow: function (row, data, dataIndex) {
                                        $compile(angular.element(row).contents())($scope);
                                    }
                                });
                            }
                        } else if ($scope.transportSearchModel.rptType == "searhbyHandeReport") {
                            if (jQuery('#transportTable').DataTable()) {
                                jQuery('#transportTable').DataTable().destroy();
                            }
                            $('#transportTable').DataTable({
                                "order": [[0, "desc"]],
                                'responsive': true,
                                "processing": true,
                                "serverSide": true,
                                "ajax": {
                                    "url": transport.getVehicleRequestsServerProcessedByHandleTr,
                                    "data": function (d) {
                                        d.vehicType = (!$scope.transportSearchModel.vehicType) ? 'null' : $scope.transportSearchModel.vehicType;
                                        d.market = (!$scope.transportSearchModel.market) ? 'null' : $scope.transportSearchModel.market;
                                        d.frmDate = (!$scope.transportSearchModel.frmDate) ? 'null' :$scope.transportSearchModel.frmDate;
                                        d.toDate = (!$scope.transportSearchModel.toDate) ? 'null' :$scope.transportSearchModel.toDate;
                                        d.reqNo = (!$scope.transportSearchModel.reqNo) ? 'null' :$scope.transportSearchModel.reqNo;
                                        d.tourNo = (!$scope.transportSearchModel.tourNo) ? 'null' :$scope.transportSearchModel.tourNo;
                                    }
                                },
                                'columns': [
                                    {
                                        "className": 'details-load',
                                        "data": "reportDateTime",
                                        "defaultContent": '',
                                        "render": function (data, type, row) {
                                            return '<span ng-click="tableClick($event)"><i class="ti-car"></i></span>';
                                        }
                                    },
                                    {
                                        "data": "reportDateTime",
                                        "render": function (value,data) {
                                            if (value === null) return "";
                                            return moment(value).format('YYYY-MM-DD')
                                        }
                                    },
                                    {
                                        "data": "departureFightTime",
                                        "render": function (value) {
                                            if (value === null) return "";
                                            return moment(value).format('YYYY-MM-DD')
                                        }
                                    },
                                    {"data": "requestedVehicle"},
                                    {"data": "requestedMarketLabel"},
                                    {"data": "requisitionNo"}
                                ],
                                createdRow: function (row, data, dataIndex) {
                                    $compile(angular.element(row).contents())($scope);
                                }
                            });
                        }else if ($scope.transportSearchModel.rptType == "searhbyPrintableReport") {
                            if (jQuery('#transportTable').DataTable()) {
                                jQuery('#transportTable').DataTable().destroy();
                            }
                            $('#transportTable').DataTable({
                                "order": [[0, "desc"]],
                                'responsive': true,
                                "processing": true,
                                "serverSide": true,
                                "ajax": {
                                    "url": transport.getVehicleRequestsServerProcessedByHandleTr,
                                    "data": function (d) {
                                        d.vehicType = (!$scope.transportSearchModel.vehicType) ? 'null' : $scope.transportSearchModel.vehicType;
                                        d.market = (!$scope.transportSearchModel.market) ? 'null' : $scope.transportSearchModel.market;
                                        d.frmDate = (!$scope.transportSearchModel.frmDate) ? 'null' :$scope.transportSearchModel.frmDate;
                                        d.toDate = (!$scope.transportSearchModel.toDate) ? 'null' :$scope.transportSearchModel.toDate;
                                        d.reqNo = (!$scope.transportSearchModel.reqNo) ? 'null' :$scope.transportSearchModel.reqNo;
                                        d.tourNo = (!$scope.transportSearchModel.tourNo) ? 'null' :$scope.transportSearchModel.tourNo;
                                    }
                                },
                                'columns': [
                                    {"data": "requisitionNo"},
                                    {"data": "requisitionNo"},
                                    {"data": "tourNo"},
                                    {"data": "clientName"},
                                    {
                                        "data": "reportDateTime",
                                        "render": function (value,data) {
                                            if (value === null) return "";
                                            return moment(value).format('YYYY-MM-DD')
                                        }
                                    },
                                    {"data": "reportTo"},
                                    {
                                        "data": "departureFightTime",
                                        "render": function (value) {
                                            if (value === null) return "";
                                            return moment(value).format('YYYY-MM-DD')
                                        }
                                    },
                                    {"data": "requisitionType"},
                                    {"data": "requestedVehicle"},
                                    {"data": "requestedMarketLabel"},
                                    {"data": "requestedMarketLabel"},
                                    {"data": "requestedMarketLabel"}
                                ]
                            });
                        }
                    }
                    //$scope.tableClick(form);
                },
                reset: function (form) {
                    if ($scope.transportSearchModel.rptType == "searhbyvehiclesreqs") {
                        $scope.transportSearchModel.searchBy = "";
                        $scope.transportSearchModel.searchNo = "";
                    } else if ($scope.transportSearchModel.rptType == "searhbyviewallreq") {
                        $scope.transportSearchModel.vehicType = "";
                        $scope.transportSearchModel.searchBy = "";
                    }
                    form.$setPristine(true);
                    $scope.loadHistory();
                }
            };

            //get market list from the service
            permissionHandleService.getMarketList().then(function (response) {
                $scope.MarketLists = response.data;
            });

            //table row click event
            $scope.tableClick = function (event) {
                var tr = $(event.target).closest('tr');
                var row = $('#transportTable').DataTable().row(tr);
                var historyTableData = row.data();
                $scope.transportModel.tourNo = historyTableData.tourNo;
                $scope.transportModel.reqNo = historyTableData.requisitionNo;
                $scope.transportModel.reqBy = historyTableData.requestedBy;
                $scope.transportModel.reqOn = $filter('date')(historyTableData.requestedDate, 'yyyy-MM-dd');
                $scope.transportModel.reqType = historyTableData.requisitionType;
                $scope.transportModel.market = historyTableData.requestedMarketLabel;
                $scope.transportModel.clientName = historyTableData.clientName;
                $scope.transportModel.adults = historyTableData.noOfAdults;
                $scope.transportModel.childrens = historyTableData.noOfChild;
                $scope.transportModel.reqVehicle = historyTableData.requestedVehicle;
                $scope.transportModel.reportTo = historyTableData.reportTo;
                $scope.transportModel.reportDate = $filter('date')(historyTableData.reportDateTime, 'yyyy-MM-dd');
                $scope.transportModel.reportTime = $filter('date')(historyTableData.reportDateTime, 'HH:mm:ss');
                $scope.transportModel.endDate = $filter('date')(historyTableData.departureFightTime, 'yyyy-MM-dd');
                $scope.transportModel.endTime = $filter('date')(historyTableData.departureFightTime, 'HH:mm:ss');
                $scope.transportModel.watterbottle = historyTableData.waterBottle;
                $scope.transportModel.simcards = historyTableData.simCard;
                $scope.transportModel.remarks = $scope.Blobext(historyTableData.remarks);

                //flightdetails
                $scope.transportModel.arrivalFlightNo = historyTableData.arrivalFlightNo;
                $scope.transportModel.departureFlightNo = historyTableData.departureFlightNo;
                $scope.transportModel.arrivalDateTime = $filter('date')(historyTableData.arrivalFlightTime, 'yyyy-MM-dd HH:mm:ss');
                $scope.transportModel.departureDateTime = $filter('date')(historyTableData.departureFightTime, 'yyyy-MM-dd HH:mm:ss');
                $scope.transportModel.arrivalAirport = historyTableData.arrivalAirpot;
                $scope.transportModel.departureAirport = historyTableData.departureAirport;

                //load vehicle request details
                transportModuleService.loadVehicleRequestDetails(historyTableData.uuid).then(function (response) {
                    var out=response.data;
                    $scope.transportModel.vehiclechange = out.rf1;
                    $scope.transportModel.driverchange = out.rf2;
                    $scope.transportModel.guidechange = out.rf3;
                });

                //load nightstops
                transportModuleService.loadVehicleRequestNightStops(historyTableData.uuid).then(function (response) {
                    if (jQuery('#nightstops').DataTable()) {
                        jQuery('#nightstops').DataTable().destroy();
                    }
                    $timeout(function () {
                        $('#nightstops').DataTable({
                            'data': response.data,
                            'columns': [
                                {"width": "70%","data": "stopOrder"},
                                {"data": "travelDate"},
                                {"data": "route"},
                                {"data": "nightStop"}
                            ]
                        });
                    }, 50);
                });
            };

            $scope.open = function (size, event,modal) {
                var modalInstance =
                    $uibModal.open({
                        templateUrl: modal,
                        size: size,
                        controller: function ($scope, $uibModalInstance) {
                            $scope.items = event;
                            $scope.ok = function (e) {
                                $uibModalInstance.close();
                                // e.stopPropagation();
                            };
                            $scope.cancel = function (e) {
                                $uibModalInstance.dismiss();
                                // e.stopPropagation();
                            };
                        }
                    });
                $timeout(function () {
                    angular.element('#accountsTable').DataTable({
                        dom: 'Bfrtip',
                        buttons: [
                            'csv', 'excel', 'pdf'
                        ]
                    });
                }, 50);
            };

            $scope.Blobext = function (buffer) {
                var binary = '';
                var bytes = new Uint8Array(buffer);
                var len = bytes.byteLength;
                for (var i = 0; i < len; i++) {
                    binary += String.fromCharCode(bytes[i]);
                }
                return window.btoa(binary);
            };

        } else {
            SweetAlert.swal("Session Invalid,Please Login");
            $timeout(function () {
                window.location.href = x[0] + "login/signin";
            }, 50);
        }
    }]);
