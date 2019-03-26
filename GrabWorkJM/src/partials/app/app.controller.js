define([
    'app/app.module',
    'app/resizable.dir',
    'css!app/app.style.css'
], function(app) {
    'use strict';

    function appCtrl($scope, settings,$state) {
        $scope.page = {
            info: {
                title: settings.info.name,
                description: settings.info.description
            }
        };
        $scope.goto = function(state, params) {
            if (state) {
                $state.go(state, params);
            }
        };
    }

    app.controller('appCtrl', ['$scope', 'settings', '$state',appCtrl]);
});