'use strict';

angular.module('jhtestApp')
    .factory('Password', ['$resource', function ($resource) {
        return $resource('api/account/change_password', {}, {
        });
    }]);

angular.module('jhtestApp')
    .factory('PasswordResetInit', ['$resource', function ($resource) {
        return $resource('api/account/reset_password/init', {}, {
        })
    }]);

angular.module('jhtestApp')
    .factory('PasswordResetFinish', ['$resource', function ($resource) {
        return $resource('api/account/reset_password/finish', {}, {
        })
    }]);
