define([
    'settings',
    'mobile/mobile.module'
], function(settings, mobile) {
    'use strict';

    var app = angular.module('wap.app', [
            //'ngMessages',
            'ngResource',            
 
            /* 第三方组件 */
            'ui.router',
            'oc.lazyLoad',

            mobile.name
        ])
        .constant('settings', settings)
        /* 初始化路由器配置 */
        .config(['$stateProvider', '$urlRouterProvider',
            function($stateProvider, $urlRouterProvider) {
                // Default Page
                $urlRouterProvider.otherwise('mobile/grabWork');
            }
        ])
        /* 加入拦截器 */
        .config(['$httpProvider',
            function($httpProvider) {
                
                $httpProvider.interceptors.push('apiRedirectInterceptor');
            }
        ])
        /* API请求地址重定向 */
        /* 当向API请求数据时，根据路由配置重定向到指定的API域名 */
        .factory('apiRedirectInterceptor', ['$location',
            function($location) {
                var router = settings.router;
                var interceptor = {
                    request: function(config) {
                        if (router) {
                            for (var key in router) {
                                var pattern = key.replace('*', '.*').replace('/', '\\/');
                                var re = new RegExp(pattern);
                                if (re.test(config.url)) {
                                    config.url = router[key] + config.url;
                                }
                            }
                        }

                        var url = config.url;
                        if (angular.isObject(config.url)) {
                            url = config.url.url;
                        }
                        console.log(url);

                        return config;
                    }
                };
                return interceptor;
            }
        ]);

    return app;
});