'use strict';

angular.module('jhtestApp')
    .controller('ActivationController', ['$scope', '$stateParams', 'Auth', function ($scope, $stateParams, Auth) {
        Auth.activateAccount({key: $stateParams.key}).then(function () {
            $scope.error = null;
            $scope.success = 'OK';
        }).catch(function () {
            $scope.success = null;
            $scope.error = 'ERROR';
        });
    }]);

