/**
 * Created by nailgun on 31.01.16.
 */
'use strict';

angular.module('jhtestApp')
    .directive('insertText', ['$rootScope', function($rootScope) {
        return {
            link: function(scope, element, attrs, ctrl) {
                $rootScope.$on('insertText', function(e, val) {
                    var domElement = element[0];
                    if (document.selection) {
                        domElement.focus();
                        var sel = document.selection.createRange();
                        sel.text = val;
                        domElement.focus();
                    } else if (domElement.selectionStart || domElement.selectionStart === 0) {
                        var startPos = domElement.selectionStart;
                        var endPos = domElement.selectionEnd;
                        var scrollTop = domElement.scrollTop;
                        domElement.value = domElement.value.substring(0, startPos) + val + domElement.value.substring(endPos, domElement.value.length);
                        domElement.focus();
                        domElement.selectionStart = startPos + val.length;
                        domElement.selectionEnd = startPos + val.length;
                        domElement.scrollTop = scrollTop;
                    } else {
                        domElement.value += val;
                        domElement.focus();
                    }
                    eval('scope.' + attrs.ngModel + '="' + domElement.value + '"');//TODO: resolve in a proper way
                });
            }
        }
    }]);
