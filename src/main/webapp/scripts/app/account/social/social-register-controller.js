'use strict';

angular.module('jhtestApp')
    .controller('SocialRegisterController', ['$scope', '$filter', '$stateParams', function ($scope, $filter, $stateParams) {
        $scope.provider = $stateParams.provider;
        $scope.providerLabel = $filter('capitalize')($scope.provider);
        $scope.success = $stateParams.success;
        $scope.error = !$scope.success;
    }]);
