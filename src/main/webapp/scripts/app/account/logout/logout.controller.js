'use strict';

angular.module('jhtestApp')
    .controller('LogoutController', function (Auth) {
        Auth.logout();
    });
