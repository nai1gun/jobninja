 'use strict';

angular.module('jhtestApp')
    .factory('notificationInterceptor', ['$q', 'AlertService', function ($q, AlertService) {
        return {
            response: function(response) {
                var alertKey = response.headers('X-jhtestApp-alert');
                if (angular.isString(alertKey)) {
                    AlertService.success(alertKey, { param : response.headers('X-jhtestApp-params')});
                }
                return response;
            }
        };
    }]);
