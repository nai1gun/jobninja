'use strict';

angular.module('jhtestApp')
    .controller('PositionDetailController', function ($scope, $rootScope, $stateParams, entity, Position) {
        $scope.position = entity;
        $scope.load = function (id) {
            Position.get({id: id}, function(result) {
                $scope.position = result;
            });
        };
        $rootScope.$on('jhtestApp:positionUpdate', function(event, result) {
            $scope.position = result;
        });
    });
