'use strict';

angular.module('jhtestApp').controller('PositionDialogController',
    ['$scope', '$stateParams', '$modalInstance', 'entity', 'Position', 'DateUtils',
        function($scope, $stateParams, $modalInstance, entity, Position, DateUtils) {

        $scope.position = entity;
        $scope.load = function(id) {
            Position.get({id : id}, function(result) {
                $scope.position = result;
            });
        };

        var onSaveFinished = function (result) {
            $scope.$emit('jhtestApp:positionUpdate', result);
            $modalInstance.close(result);
        };

        $scope.save = function () {
            $scope.position.edited = DateUtils.convertLocaleDateToServer(new Date());
            if ($scope.position.id != null) {
                Position.update($scope.position, onSaveFinished);
            } else {
                $scope.position.created = DateUtils.convertLocaleDateToServer(new Date());
                Position.save($scope.position, onSaveFinished);
            }
        };

        $scope.clear = function() {
            $modalInstance.dismiss('cancel');
        };
}]);
