'use strict';

angular.module('jhtestApp')
    .config(['$stateProvider', function ($stateProvider) {
        $stateProvider
            .state('account', {
                abstract: true,
                parent: 'site'
            });
    }]);
