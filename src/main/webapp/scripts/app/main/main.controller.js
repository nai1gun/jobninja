'use strict';

angular.module('jhtestApp')
    .controller('MainController', ['$scope', 'Principal', function ($scope, Principal) {

        $scope.canInstallExtension = function() {
            return chrome && !chrome.app.isInstalled;
        };

        Principal.identity().then(function(account) {
            $scope.account = account;
            $scope.isAuthenticated = Principal.isAuthenticated;
        });
    }]);
