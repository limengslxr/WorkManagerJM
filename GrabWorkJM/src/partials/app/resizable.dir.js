define([
    'utils',
    'app/app.module'
], function(utils, app) {
    'use strict';

    function resizable($window, $timeout) {
        return {
            restrict: 'A',
            link: function($scope) {

                // 根据Window窗体大小计算视图尺寸
                var getWindowSize = function() {                    

                    return {
                        'width': $window.innerWidth,
                        'height': $window.innerHeight 
                    };
                };

                // 初始化视图尺寸
                $scope.viewSize = getWindowSize();

                // 监听窗体resize事件
                var promise = null;
                angular.element($window)
                    .unbind('resize')
                    .bind('resize', function() {
                        if (promise) {
                            $timeout.cancel(promise);
                            promise = null;
                        }
                        promise = $timeout(function() {
                            $scope.viewSize = getWindowSize();
                            $scope.$broadcast('WAP_RESEIZED',$scope.viewSize);
                            console.log('window resized: ' + JSON.stringify($scope.viewSize));
                        }, 150);
                    });
            }
        };
    }

    app.directive('resizable', ['$window', '$timeout', resizable]);

});