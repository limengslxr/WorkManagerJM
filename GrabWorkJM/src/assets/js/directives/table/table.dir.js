define([
    'directives/directives.module',
    'assets/vendor/lodash/dist/lodash.min',
    'css!vendor/bootstrap/dist/css/bootstrap.min'
], function() {
    'use strict';

    angular.module('wap.directives')
        .directive('ngTable', function() {
            return {
                restrict: 'EA',
                replace: true,
                scope: {
                    data: '=tableData',
                    clazz: '=tableClass',
                    heads: '=tableHeads'
                },
                templateUrl: 'assets/js/directives/table/table.tpl.html',
                link: function link(scope, element, attrs, ctrl) {
                    if (scope.clazz !== undefined) {
                        element.addClass(scope.clazz);
                    }
                    if (scope.heads !== undefined) {
                        scope.showHead = true;
                    }
                    // var table=$('<table  class="table" style="font-size:12px;width:100%;"></table>');
                    // if(scope.clazz!=undefined){
                    //     $(table).addClass(scope.clazz);
                    // }
                    // if(scope.heads!=undefined){
                    //     var thead=$('<thead><tr></tr></thead>');
                    //     _.each(scope.heads,function(head){
                    //         $('<th style="text-align:center">'+head+'</th>').appendTo(thead);
                    //     })
                    //     thead.appendTo(table);
                    // }

                    // if(scope.data!=undefined){
                    //     var tbody=$('<tbody></tbody>');
                    //     _.each(scope.data,function(data){
                    //         var tr=$('<tr style="text-align:center"></tr>');
                    //         _.forEach(data,function(d,key) {
                    //             if(key<scope.heads.length)
                    //                 $('<td>'+d+'</th>').appendTo(tr);
                    //         })
                    //        tr.appendTo(tbody);
                    //     })
                    //     tbody.appendTo(table);
                    // }
                    // table.appendTo(element);

                }
            };
        });
});