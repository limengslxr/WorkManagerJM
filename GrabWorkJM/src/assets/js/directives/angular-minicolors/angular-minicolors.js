define([
    'directives/directives.module',
    'vendor/jquery-minicolors/jquery.minicolors.min',
    'css!vendor/jquery-minicolors/jquery.minicolors'
], function() {
    'use strict';

    angular.module('wap.directives')
        .directive('ngMinicolors', ['$timeout',
            function($timeout) {

                var defauts = {
                    animationSpeed: 50,
                    animationEasing: 'swing',
                    change: null,
                    changeDelay: 500,
                    control: 'hex',
                    dataUris: true,
                    defaultValue: '',
                    format: 'hex',
                    hide: null,
                    hideSpeed: 100,
                    inline: false,
                    keywords: '',
                    letterCase: 'lowercase',
                    opacity: false,
                    position: 'bottom left',
                    show: null,
                    showSpeed: 100,
                    theme: 'bootstrap',
                    swatches: []
                };

                var hexReg = /^#[0-9a-fA-F]{3,6}/i;
                var rgbReg = '';

                return {
                    restrict: 'EA',
                    replace: true,
                    require: '?ngModel',
                    scope: {
                        control: '@colorControl',
                        opacity: '@colorOpacity',
                        position: '@colorPosition',
                        format: '@colorFormat',
                        delay: '@colorDelay',
                        onChange: '&colorChanged'
                    },
                    link: function(scope, elem, attrs, model) {

                        // 获取控件配置
                        var getOptions = function() {
                            var options = angular.copy(defauts);
                            if (scope.control) {
                                options.control = scope.control;
                            }
                            if (scope.opacity) {
                                options.opacity = scope.opacity;
                            }
                            if (scope.position) {
                                options.position = scope.position;
                            }
                            if (scope.format) {
                                options.format = scope.format;
                            }
                            if (scope.delay) {
                                options.changeDelay = parseInt(scope.delay);
                                if(options.changeDelay <= 0) {
                                    options.changeDelay = 500;
                                }
                            }
                            options.change = function(value, opacity) {
                                var color = {
                                    color: value,
                                    opacity: opacity
                                };
                                if (scope.onChange) {
                                    scope.onChange(color);
                                }
                            };
                            return options;
                        };

                        // 初始化控件
                        var options = getOptions();
                        elem.minicolors(options);

                        // 接收ngModel中绑定变量的变化
                        if (model) {
                            var promise = null;
                            model.$render = function() {
                                var value = model.$modelValue;
                                if (promise !== null) {
                                    $timeout.cancel(promise);
                                }
                                promise = $timeout(function() {
                                    elem.minicolors('value', value);
                                }, options.changeDelay, false);
                            };
                        }
                    }
                };
            }
        ]);
});