'use strict';

angular.module('jhtestApp')
    .controller('MainController', ['$scope', 'Principal', function ($scope, Principal) {
        Principal.identity().then(function(account) {
            $scope.account = account;
            $scope.isAuthenticated = Principal.isAuthenticated;
        });
    }]);
