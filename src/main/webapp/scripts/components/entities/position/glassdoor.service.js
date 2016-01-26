'use strict';

angular.module('jhtestApp')
    .factory('Glassdoor', ['$resource', function ($resource) {
        return $resource('api/glassdoor/:employerName', {employerName: '@employerName'}, {
            'get': {
                method: 'GET'
            }
        });
    }]);
