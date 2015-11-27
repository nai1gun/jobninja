'use strict';

angular.module('jhtestApp')
    .factory('UserCv', ['$resource', function ($resource) {
        return $resource('api/users/current/cv', {}, {
                'query': {method: 'GET', isArray: true },
                'delete': { method: 'PUT' },//DELETE method doesn't allow request body
                'save': { method: 'POST' }
            });
        }])
    .constant('S3_PREFIX', 'https://job-ninja-assets.s3.amazonaws.com/')
;
