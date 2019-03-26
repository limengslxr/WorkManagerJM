define([], function() {
    'use strict';

    var scrollbarWidth = null;

    var utils = {

        // 根据函数参数生成依赖对象列表
        getCtrlDeps: function(fn) {
            var args = fn.toString().match(/function\s.*?\(([^)]*)\)/)[1];
            var deps = args.split(',').map(function(arg) {
                return arg.replace(/\/\*.*\*\//, '').trim();
            }).filter(function(arg) {
                return arg;
            });
            deps.push(fn);
            return deps;
        },

        // // 判断指定元素上是否有滚动条
        // hasScrolled: function(el, direction = 'vertical') {
        //     if (direction === 'vertical') {
        //         return el.scrollHeight > el.clientHeight;
        //     } else if (direction === 'horizontal') {
        //         return el.scrollWidth > el.clientWidth;
        //     }
        // },

        // 获取滚动条宽度
        getScrollbarWidth: function() {

            if (scrollbarWidth !== null) {
                return scrollbarWidth;
            }
            var w1, w2,
                div = $('<div style="display:block;position:absolute;width:50px;height:50px;overflow:hidden;"><div style="height:100px;width:auto;"></div></div>'),
                innerDiv = div.children()[0];

            $('body').append(div);
            w1 = innerDiv.offsetWidth;
            div.css('overflow', 'scroll');

            w2 = innerDiv.offsetWidth;

            if (w1 === w2) {
                w2 = div[0].clientWidth;
            }

            div.remove();

            return (scrollbarWidth = w1 - w2);
        }
    };

    return utils;
});