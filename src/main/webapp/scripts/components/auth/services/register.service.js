'use strict';

angular.module('jhtestApp')
    .factory('Register', function ($resource) {
        return $resource('api/register', {}, {
        });
    });


