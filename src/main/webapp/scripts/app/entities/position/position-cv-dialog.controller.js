'use strict';

angular.module('jhtestApp').controller('PositionCvDialogController',
    ['$scope', '$rootScope', '$modalInstance', '$state', 'Position', 'DateUtils', 'PositionState',
        function($scope, $rootScope, $modalInstance, $state, Position, DateUtils, PositionState) {

        $scope.position = $state.$current.parent.data.position;
        $scope.cv = $scope.position.cv;

        $scope.ok = function() {
            $scope.position.cv = $scope.cv;
            $modalInstance.dismiss('ok');
        };

        $scope.cancel = function() {
            $modalInstance.dismiss('cancel');
        };

}]);
