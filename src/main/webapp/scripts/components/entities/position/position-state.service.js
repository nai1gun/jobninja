'use strict';

angular.module('jhtestApp')
    .factory('PositionState', ['$translate', function ($translate) {
        return {
            getAll: function (callback) {
                return $translate('jhtestApp.position.defaultStates').then(function(statesStr){
                    callback(statesStr.split(','));
                });
            }
        };
    }]);
