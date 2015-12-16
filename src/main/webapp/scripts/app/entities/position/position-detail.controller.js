'use strict';

angular.module('jhtestApp')
    .controller('PositionDetailController', ['$scope', '$rootScope', '$stateParams', '$state', '$timeout', 'entity', 'Position', 'PositionState', 'DateUtils', 'PositionUtils',
        function ($scope, $rootScope, $stateParams, $state, $timeout, entity, Position, PositionState, DateUtils, PositionUtils) {
        $scope.position = entity;
        $scope.$position = angular.copy($scope.position);
        $scope.editing = $state.$current.data.editing;
        $scope.load = function (id) {
            Position.get({id: id}, function(result) {
                $scope.position = result;
                updatePosition();
            });
        };
        $rootScope.$on('jhtestApp:positionUpdate', function(event, result) {
            $scope.position = result;
            updatePosition();
        });
        $scope.editStart = function() {
            $scope.$position = angular.copy($scope.position);
            $scope.editing = true;
            $state.go('position.edit');
        };
        $scope.editCancel = function() {
            $scope.position = angular.copy($scope.$position);
            updatePosition();
            $scope.editing = false;
            $state.go('position.detail');
        };
        $scope.editDone = function() {
            $scope.editing = false;
            $scope.position.edited = DateUtils.convertLocaleDateToServer(new Date());
            Position.update($scope.position, onSaveFinished);
            $state.go('position.detail');
        };
        $scope.addCoverLetter = function() {
            if (!$scope.editing) {
                $scope.editStart();
            }
            $scope.position.coverLetter = "";
        };
        $scope.hasCoverLetter = function() {
            return $scope.position.coverLetter != undefined && $scope.position.coverLetter != null;
        };

        $scope.editCV = function() {
            if (!$scope.editing) {
                $scope.editStart();
            }
            $state.go('position.detail.cv');
        };

        $scope.removeCv = function() {
            $scope.position.cv = null;
        };

        $scope.hasCV = function() {
            return $scope.position.cv != undefined && $scope.position.cv != null;
        };

        $scope.s3url = PositionUtils.s3url;

        $scope.fileName = PositionUtils.fileName;

        var onSaveFinished = function (result) {
            $scope.$emit('jhtestApp:positionUpdate', result);
        };

        var updatePosition = function() {
            $state.$current.data.position = $scope.position;
        };

        PositionState.getAll(function(states) {
            $scope.states = states;
        });

        updatePosition();

    }]);
