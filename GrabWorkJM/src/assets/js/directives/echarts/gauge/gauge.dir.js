define([
    'directives/directives.module',
    'assets/vendor/echarts/dist/echarts.min',
    'assets/vendor/lodash/dist/lodash.min'
], function(directives, echarts) {
    'use strict';

    angular.module('wap.directives')
        .directive('ngGauge', function() {

            var defaultOption = {
                tooltip: {
                    formatter: '{a} <br/>{b} : {c}%'
                },
                toolbox: {
                    show: true,
                    feature: {
                        mark: {
                            show: true
                        },
                        restore: {
                            show: true
                        },
                        saveAsImage: {
                            show: true
                        }
                    }
                },
                series: [{
                    name: '业务指标',
                    type: 'gauge',
                    // startAngle:135,
                    // endAngle:-135,  
                    detail: {
                        formatter: '{value}%'
                    },
                    data: [{
                        value: 80,
                        name: '完成率'
                    }]
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
                    startAngle: '=ecStartAngle',
                    endAngle: '=ecEndAngle',
                    data: '=ecData',
                    tooltipFormat: '@ecTooltopFormat',
                },
                link: function(scope, elem, attrs, model) {
                    var option = _.cloneDeep(defaultOption);
                    //scope.data.value=scope.data.value.toFixed(2);
                    if (scope.startAngle !== undefined && scope.endAngle !== undefined) {
                        // option.series[0].startAngle = scope.startAngle;
                        // option.series[0].endAngle = scope.endAngle;
                        option.series[0] = {
                            name: '个性化仪表盘',
                            type: 'gauge',
                            startAngle: scope.startAngle,
                            endAngle: scope.endAngle,
                            min: 0, // 最小值
                            max: 100, // 最大值
                            precision: 0, // 小数精度，默认为0，无小数点
                            splitNumber: 10, // 分割段数，默认为5
                            axisLine: { // 坐标轴线
                                show: true, // 默认显示，属性show控制显示与否
                                lineStyle: { // 属性lineStyle控制线条样式
                                    color: [
                                        [0.2, 'lightgreen'],
                                        [0.4, 'orange'],
                                        [0.8, 'skyblue'],
                                        [1, '#ff4500']
                                    ],
                                    width: 30
                                }
                            },
                            axisTick: { // 坐标轴小标记
                                show: true, // 属性show控制显示与否，默认不显示
                                splitNumber: 5, // 每份split细分多少段
                                length: 8, // 属性length控制线长
                                lineStyle: { // 属性lineStyle控制线条样式
                                    color: '#eee',
                                    width: 1,
                                    type: 'solid'
                                }
                            },
                            axisLabel: { // 坐标轴文本标签，详见axis.axisLabel
                                show: true,
                                formatter: function(v) {
                                    switch (v + '') {
                                        case '10':
                                            return '弱';
                                        case '30':
                                            return '低';
                                        case '60':
                                            return '中';
                                        case '90':
                                            return '高';
                                        default:
                                            return '';
                                    }
                                },
                                textStyle: { // 其余属性默认使用全局文本样式，详见TEXTSTYLE
                                    color: '#333'
                                }
                            },
                            splitLine: { // 分隔线
                                show: true, // 默认显示，属性show控制显示与否
                                length: 30, // 属性length控制线长
                                lineStyle: { // 属性lineStyle（详见lineStyle）控制线条样式
                                    color: '#eee',
                                    width: 2,
                                    type: 'solid'
                                }
                            },
                            pointer: {
                                length: '80%',
                                width: 8,
                                color: 'auto'
                            },
                            title: {
                                show: true,
                                offsetCenter: ['-65%', -10], // x, y，单位px
                                textStyle: { // 其余属性默认使用全局文本样式，详见TEXTSTYLE
                                    color: '#333',
                                    fontSize: 15
                                }
                            },
                            detail: {
                                show: true,
                                backgroundColor: 'rgba(0,0,0,0)',
                                borderWidth: 0,
                                borderColor: '#ccc',
                                width: 100,
                                height: 40,
                                offsetCenter: ['-60%', 10], // x, y，单位px
                                formatter: '{value}%',
                                textStyle: { // 其余属性默认使用全局文本样式，详见TEXTSTYLE
                                    color: 'auto',
                                    fontSize: 30
                                }
                            },
                            data: [{
                                value: 50,
                                name: '仪表盘'
                            }]
                        };
                    }
                    option.series[0].data = scope.data;
                    initChart(elem[0], option);

                }
            };
        });
});