define([
    'mobile/mobile.module',
    'css!mobile/grabWork/grabWork.style.css',
    'css!mobile/icon.style.css'
], function() {
    'use strict';

    angular.module('wsmp.mobile')
        .controller('grabWorkCtrl', ['$scope','$rootScope','$state','httpService',
            function($scope,$rootScope,$state,httpService) {

                var clickWork;
                var works;

                var getAccount = function(){
                    document.addEventListener('searchKey', searchKey, false);
                    document.addEventListener('refreshMenu', refreshMenu, false);

                    cordova.plugins.toolbar.updateToolbar(
                        ["抢单列表",true,true,false],
                        function(data) {
                            console.log(data);
                        },
                        function(error) {
                            console.log(error);
                        });

                    cordova.plugins.userInfo.getAccount(
                        function(data) {
                            var strs=data.split("#"); 
                            if (strs&&(strs.length === 2)) {
                                httpService.setAccount(strs[0]);
                                httpService.setUrl(strs[1]);
                                initial();
                            }
                        },
                        function(error) {
                            console.log("error");
                        });
                };

                function searchKey(key){
                    if (!works||!key||!key[0]) {
                        return;
                    }

                    var keyWork = [];
                    angular.forEach(works, function(data){
                        if((data.cardName&&data.cardName.indexOf(key[0]) >= 0) 
                            ||(data.address&&data.address.indexOf(key[0]) >= 0)
                            ||(data.cardId&&data.cardId.indexOf(key[0]) >= 0)
                            ||(data.contacts&&data.contacts.indexOf(key[0]) >= 0)
                            ||(data.barCode&&data.barCode.indexOf(key[0]) >= 0)
                            ||(data.sealNumber&&data.sealNumber.indexOf(key[0]) >= 0)){
                            keyWork = keyWork.concat(data);
                        }
                    });

                    $scope.works = keyWork;
                    $scope.$digest();
                }

                function refreshMenu(){
                    initial();
                }
                
                var initial = function() {
                    var account = httpService.getAccount();
                    if (!account) {
                        console.log("get account error");
                        return;
                    }

                    works = [];

                    var parms = {account:account,since:1,count:100};
                    var url = httpService.getUrl()+'API/v1/mobile/tasks/unDispatchedList';

                    httpService.downloadTask(url,parms,
                        function(data){
                            $scope.showRetry = false;
                            var temp = data.data;
                            for (var i = 0; i < temp.length; i++) {
                                works = works.concat(temp[i]);
                            }
                            $scope.works = works;
                            //$scope.$apply();
                        },
                        function(){
                            console.log("downloadTask error");
                        }
                    );
                };

                getAccount();

                $scope.transformMultiType = function (task) {
                    if (!task){
                        return "";
                    }

                    var multiType = "";
                    switch (task.type){
                        case 2:
                            multiType = "表务工单/";
                            break;
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

                    switch (task.subType){
                        case 1:
                            return multiType + "拆表";
                        case 2:
                            return multiType + "换表";
                        case 3:
                            return multiType + "复装";
                        case 4:
                            return multiType + "停水";
                        case 5:
                            return multiType + "迁表";
                        case 6:
                            return multiType + "验表";
                        case 7:
                            return multiType + "复用";
                        case 8:
                            return multiType + "停通";
                        case 9:
                            return multiType + "新核";
                        default:
                            return multiType + "";
                    }
                };

                $scope.grabWork = function(work){
                    clickWork = work;
                    httpService.setWork(work);
                    $state.go('mobile.infoDetail');
                };

                $rootScope.$on('grabWorkSuccess', function (event,parms) {
                    var index = works.indexOf(clickWork);
                    if (index > -1) {
                        works.splice(index, 1);
                        $scope.$digest();
                        //$scope.$apply();
                    }

                    cordova.plugins.toolbar.updateToolbar(
                        ["抢单列表",true,true,false],
                        function(data) {
                            console.log(data);
                        },
                        function(error) {
                            console.log(error);
                        });
                });
                
            }
        ]);

});
