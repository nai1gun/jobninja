'use strict';

angular.module('jhtestApp').controller('PositionDialogController',
    ['$scope', '$stateParams', '$modalInstance', 'entity', 'Position',
        function($scope, $stateParams, $modalInstance, entity, Position) {

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
            if ($scope.position.id != null) {
                Position.update($scope.position, onSaveFinished);
            } else {
                Position.save($scope.position, onSaveFinished);
            }
        };

        $scope.clear = function() {
            $modalInstance.dismiss('cancel');
        };
}]);
