define([
    'css!mobile/icon.style',
    'css!mobile/mobile.style'
], function() {
    'use strict';

    var mobile = angular.module('wsmp.mobile', [])
        /* 初始化路由器配置 */
        .config(['$stateProvider',
            function($stateProvider) {

                /*  Base Demo */
                $stateProvider.state({
                    name: 'mobile',
                    url: '/mobile',
                    templateUrl: 'partials/mobile/mobile.tpl.html',
                    abstract: true
                });

                // grabWork
                $stateProvider.state({
                    name: 'mobile.grabWork',
                    url: '/grabWork',
                    templateUrl: 'partials/mobile/grabWork/grabWork.tpl.html',
                    controller: 'grabWorkCtrl',
                    resolve: {
                        loadModule: ['$ocLazyLoad',
                            function($ocLazyLoad) {
                                return $ocLazyLoad.load({
                                    files: ['partials/mobile/grabWork/grabWork.controller.js']
                                });
                            }
                        ]
                    }
                });

                // infoDetail
                $stateProvider.state({
                    name: 'mobile.infoDetail',
                    url: '/infoDetail',
                    templateUrl: 'partials/mobile/infoDetail/infoDetail.tpl.html',
                    controller: 'infoDetailCtrl',
                    resolve: {
                        loadModule: ['$ocLazyLoad',
                            function($ocLazyLoad) {
                                return $ocLazyLoad.load({
                                    files: ['partials/mobile/infoDetail/infoDetail.controller.js']
                                });
                            }
                        ]
                    }
                });

            }
        ])
        .run(function() {

        });

        mobile.service('httpService', ['$http', function($http){
            return {   
                account : [],

                setAccount:function(setAccount){
                    this.account = setAccount;
                } ,

                getAccount:function(){
                    return this.account;
                },  

                url : [],

                setUrl:function(setUrl){
                    this.url = setUrl;
                } ,

                getUrl:function(){
                    return this.url;
                },


                work : [],

                setWork:function(setWork){
                    this.work = setWork;
                },

                getWork:function(){
                    return this.work;
                },

                downloadTask:function(url,params,onSuccess,onError){
                    $http({
                        method:'GET',
                        url:url,
                        params:params
                    })
                    .success(function(data, status, headers, config) {
                        if (!data || data.code || !data.data) {
                            onError();
                            return;
                        }

                        onSuccess(data);
                    })
                    .error(function(data, status, headers, config) {
                        onError();
                    });
                },

                grabWork:function(url,params,onSuccess,onError){
                    $http.put(url, params)
                    .success(function(data, status, headers, config) {
                        if (data&&!data.code&&data.statusCode==200) {
                            onSuccess();
                        }else{
                            onError();
                        }
                    })
                    .error(function(data, status, headers, config) {
                        onError();
                    });
                }
            };
        }]);


    return mobile;
});
