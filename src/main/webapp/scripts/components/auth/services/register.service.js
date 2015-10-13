'use strict';

angular.module('jhtestApp')
    .factory('Register', ['$resource', function ($resource) {
        return $resource('api/register', {}, {
        });
    }]);


