/// This is the main entry point of the site.
/// Make sure this file has been refered in app.html before others js file.

(function(window) {
    'use strict';

    // Avoid `console` errors in browsers that lack a console.    
    var method;
    var noop = function() {};
    var methods = [
        'assert', 'clear', 'count', 'debug', 'dir', 'dirxml', 'error',
        'exception', 'group', 'groupCollapsed', 'groupEnd', 'info', 'log',
        'markTimeline', 'profile', 'profileEnd', 'table', 'time', 'timeEnd',
        'timeline', 'timelineEnd', 'timeStamp', 'trace', 'warn'
    ];
    var length = methods.length;
    var console = (window.console = window.console || {});

    while (length--) {
        method = methods[length];

        // Only stub undefined methods.
        if (!console[method]) {
            console[method] = noop;
        }
    }

    // 初始化 RequireJS
    requirejs.config({
        baseUrl: '/',
        paths: {
            /* 定义第三方类库 */
            'angular': 'GrabWorkJM/src/assets/vendor/angular/angular.min',

            /* 定义常用文件夹 */
            'vendor': 'GrabWorkJM/src/assets/vendor',
            'directives': 'GrabWorkJM/src/assets/js/directives',

            /* 定义全局模块 */
            'settings': 'GrabWorkJM/src/settings',
            'utils': 'GrabWorkJM/src/assets/js/utils',

            /* 定义模块文件夹 */
            'app': 'GrabWorkJM/src/partials/app',
            'mobile': 'GrabWorkJM/src/partials/mobile',

            'cordova': 'GrabWorkJM/src/assets/js/cordova'
        },
        shim: {

        },
        bundles: {},
        map: {
            '*': {
                'css': 'GrabWorkJM/src/assets/vendor/require-css/css'
            }
        }
        // urlArgs: ''
    });

    var platform = window.navigator.platform;
    if (platform === "Win32") {
        window.cordova = {
            plugins: {
                whitelist : {

                },

                toolbar : {
                    updateToolbar : function(array) {

                    }
                },

                userInfo : {
                    getAccount : function(){
                        
                    }
                }
            }
        }
        // 加载启动模块
        requirejs([
            'app/app.index'
        ], function(app) {
            /// Bootstrap ///    
            angular.element(document).ready(function() {
                angular.bootstrap(document, app);
            });
        });
    } else {
        // 加载启动模块
        requirejs([
            'app/app.index',
            'cordova'
        ], function(app) {
            document.addEventListener(
                'deviceready',
                function() {
                    /// Bootstrap ///    
                    angular.element(document).ready(function() {
                        angular.bootstrap(document, app);
                    });
                },
                false);
        });
    }



})(window);
