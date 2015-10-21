'use strict';

angular.module('jhtestApp')
    .controller('PositionDetailController', ['$scope', '$rootScope', '$stateParams', 'entity', 'Position', 'PositionState', 'DateUtils',
        function ($scope, $rootScope, $stateParams, entity, Position, PositionState, DateUtils) {
        $scope.position = entity;
        $scope.editing = false;
        $scope.load = function (id) {
            Position.get({id: id}, function(result) {
                $scope.position = result;
            });
        };
        $rootScope.$on('jhtestApp:positionUpdate', function(event, result) {
            $scope.position = result;
        });
        $scope.editStart = function() {
            $scope.$position = angular.copy($scope.position);
            $scope.editing = true;
        };
        $scope.editCancel = function() {
            $scope.position = angular.copy($scope.$position);
            $scope.editing = false;
        };
        $scope.editDone = function() {
            $scope.editing = false;
            $scope.position.edited = DateUtils.convertLocaleDateToServer(new Date());
            Position.update($scope.position, onSaveFinished);
        };

        PositionState.getAll(function(states) {
            $scope.states = states;
        });

        var onSaveFinished = function (result) {
            $scope.$emit('jhtestApp:positionUpdate', result);
        };

    }]);
