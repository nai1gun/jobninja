'use strict';

angular.module('jhtestApp')
    .factory('Position', ['$resource', 'DateUtils', function ($resource, DateUtils) {
        return $resource('api/positions/:id', {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    data = angular.fromJson(data);
                    data.created = DateUtils.convertDateTimeFromServer(data.created);
                    data.edited = DateUtils.convertDateTimeFromServer(data.edited);
                    if (data.cv) {
                        data.cv.created = DateUtils.convertDateTimeFromServer(data.cv.created);
                    }
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    }]);
