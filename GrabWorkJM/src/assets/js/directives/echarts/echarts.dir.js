define([
    'directives/directives.module',
    'assets/vendor/echarts/dist/echarts.min',
    'assets/vendor/lodash/dist/lodash.min'
], function(directives, echarts) {
    'use strict';

    angular.module('wap.directives')
        .directive('ngEchart', function() {

            var defaultOption = {
                tooltip: {
                    trigger: 'axis'
                },
                legend: {
                    data: ['最高气温']
                },
                calculable: true,
                xAxis: [{
                    type: 'category',
                    boundaryGap: false,
                    data: ['周一', '周二', '周三', '周四', '周五', '周六', '周日']
                }],
                yAxis: [{
                    type: 'value',
                    axisLabel: {
                        formatter: '{value} °C'
                    }
                }],
                series: [{
                    name: '最高气温',
                    type: 'line',
                    data: [11, 11, 15, 13, 12, 13, 10]
                }]
            };

            function initChart(element, options) {
                var chart = echarts.init(element);
                chart.setOption(options, true);
                return chart;
            }

            return {
                restrict: 'EA',
                replace: true,
                scope: {
                    option: '=ecOption',
                    data: '=ecData',
                    text: '=ecText',
                    name: '=ecName',
                    tooltipFormat: '@ecTooltopFormat'
                },
                link: function(scope, elem, attrs, model) {
                    var option = scope.option !== undefined ? _.cloneDeep(scope.option) : _.cloneDeep(defaultOption);
                     var chart = echarts.init(elem[0]);
                    if (scope.text !== undefined) {
                        option.xAxis[0].data = scope.text;
                    }
                    if (scope.name !== undefined) {
                        option.legend.data[0] = scope.name;
                        option.series[0].name = scope.name;
                    }
                    if (scope.data !== undefined) {
                        option.series[0].data = scope.data;
                    }
                    initChart(elem[0], option);

            /*       scope.$on('WAP_RESEIZED', function(evt, args) {
                       chart.resize(args.height,args.width);
                    });*/
                    scope.$watch('option', function(newValue, oldValue) {
                        if (newValue && newValue != null && newValue != oldValue) {
                            initChart(elem[0], newValue)
                        }
                    }, true)
                }
            };

            //  return {
            //     restrict: 'EA',
            //     replace: true,
            //     scope: {
            //         option: '=ecOption',
            //         data: '=ecData',
            //         text:'=ecText',
            //         tooltipFormat: '@ecTooltopFormat'
            //     },
            //     link: function(scope, elem, attrs, model) {
            //         var option=scope.option!=undefined?_.cloneDeep(scope.option):_.cloneDeep(defaultOption);

            //         if(scope.text!=undefined)
            //             option.xAxis[0].data=scope.text;
            //         if(scope.data!=undefined){
            //             _.each(scope.data,function(data){
            //                 option.series[0].data=data.data;
            //                 option.series[0].name=data.name;
            //             })

            //         }
            //         initChart(elem[0], option);
            //     }
            // };

        });
});