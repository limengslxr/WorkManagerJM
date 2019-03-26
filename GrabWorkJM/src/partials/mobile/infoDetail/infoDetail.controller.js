define([
    'mobile/mobile.module',
    'css!mobile/infoDetail/infoDetail.style.css',
    'css!mobile/icon.style.css'
], function() {
    'use strict';

    angular.module('wsmp.mobile')
        .controller('infoDetailCtrl', ['$scope','$rootScope','httpService',
            function($scope,$rootScope,httpService) {

                var initial = function() {
                    document.addEventListener('grabWork', grabWork, false);
                    document.addEventListener("backbutton", goBack, false); 
                    $scope.work = httpService.getWork();

                    cordova.plugins.toolbar.updateToolbar(
                        ["抢单",false,false,true],
                        function(data) {
                            console.log(data);
                        },
                        function(error) {
                            console.log(error);
                        });
                };

                function goBack(){
                    document.removeEventListener('grabWork', grabWork, false);
                    document.removeEventListener("backbutton", goBack, false); 
                    history.back();
                    cordova.plugins.toolbar.updateToolbar(
                         ["抢单",true,true,false],
                        function(data) {
                            console.log(data);
                        },
                        function(error) {
                            console.log(error);
                        });
                }

                initial();

                $scope.transform = function(workType){
                    switch(workType){
                        case 2:
                            return "表务工单";
                        case 3:
                            return "催缴工单";
                        case 5:
                            return "报装工单";
                        case 8:
                            return "内部工单";
                        case 9:
                            return "热线工单";
                        default:
                            return "";
                    }
                };

                $scope.transformSubType = function (type, subType) {
                    if (type === 2){
                        switch (subType){
                            case 1:
                                return "拆表";
                            case 2:
                                return "换表";
                            case 3:
                                return "复装";
                            case 4:
                                return "停水";
                            case 5:
                                return "迁表";
                            case 6:
                                return "验表";
                            case 7:
                                return "复用";
                            case 8:
                                return "停水通知单";
                            case 9:
                                return "新装水表通知单";
                            default:
                                return "";
                        }
                    }

                    if (type === 8){
                        switch (subType){
                            case 1:
                                return "工地表续期";
                            case 2:
                                return "楼层验收";
                            default:
                                return "";
                        }
                    }

                    return "";
                };

                $scope.show = function (type, subType) {
                    var work = $scope.work;
                    if (type === 2){
                        return work.type === 2 && subType === work.subType;
                    }

                    return type === work.type;
                };

                $scope.splitType = function () {
                  var work = $scope.work;
                  if (!work || work.type !== 2 || work.subType !== 1){
                      return "拆表类型错误";
                  }

                  switch (work.detailType){
                      case 1:
                          return "欠费拆表";
                      case 2:
                          return "其他拆表";
                      default:
                          return "";
                  }
                };

                $scope.replaceType = function () {
                    var work = $scope.work;
                    if (!work || work.type !== 2 || work.subType !== 2){
                        return "换表类型错误";
                    }

                    switch (work.detailType){
                        case 1:
                            return "申请换表";
                        case 2:
                            return "定期换表";
                        case 3:
                            return "故障换表";
                        default:
                            return "";
                    }
                };

                $scope.moveType = function () {
                    var work = $scope.work;
                    if (!work || work.type !== 2 || work.subType !== 5){
                        return "迁表类型错误";
                    }

                    switch (work.detailType){
                        case 1:
                            return "有偿迁表";
                        case 2:
                            return "无偿迁表";
                        default:
                            return "";
                    }
                };

                $scope.insideType = function () {
                    var work = $scope.work;
                    if (!work || work.type !== 8 ){
                        return "内部类型错误";
                    }

                    switch (work.detailType){
                        case 1:
                            return "工地表续期";
                        case 2:
                            return "楼层验收";
                        default:
                            return "";
                    }
                };

                $scope.meterKind = function () {
                    var volume = $scope.work.volume;
                    if (!volume){
                        return "";
                    }

                    if (volume.indexOf("AAA", 0) !== -1){
                        return "普通表";
                    } else if (volume.indexOf("BBB", 0) !== -1){
                        return "远传表";
                    }

                    return "";
                };

                $scope.formatDetailTime = function (time){
                    var date = new Date(time);
                    return date.getFullYear() + "-" + (date.getMonth() + 1) + "-" + date.getDate()
                    + " " + date.getHours() + ":" + date.getMinutes();
                };

                function grabWork(){
                    if (!confirm("你确定要抢单吗？")) {
                        return;
                    }

                    var work = $scope.work;
                    var params = {
                        "account":httpService.getAccount(),
                        "taskId":work.taskId,
                        "type":work.type,
                        "extend":""
                    };

                    var url = httpService.getUrl()+'API/v1/mobile/task/dispatch';

                    httpService.grabWork(url,params,
                        function(){
                            document.removeEventListener('grabWork', grabWork, false);
                            document.removeEventListener("backbutton", goBack, false); 
                            history.back();  
                            $rootScope.$broadcast('grabWorkSuccess',params);
                        },
                        function(){
                            alert("抢单失败。");
                        }
                    );

                }
                
            }
        ]);

});
