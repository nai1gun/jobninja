'use strict';

angular.module('jhtestApp')
    .config(['$stateProvider', function ($stateProvider) {
        $stateProvider
            .state('entity', {
                abstract: true,
                parent: 'site'
            });
    }]);
