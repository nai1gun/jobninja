'use strict';

angular.module('jhtestApp')
    .controller('LogoutController', ['Auth', function (Auth) {
        Auth.logout();
    }]);
