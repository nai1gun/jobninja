'use strict';

angular.module('jhtestApp')
    .service('Config', ['$http', function($http) {

        var config = {
            awsS3BucketName: 'job-ninja-assets'
        };

        $http.get('/api/config.json').then(
            function (response) {
                config = response.data;
            });

        this.s3Prefix = function() {
            return 'https://' + config.awsS3BucketName + '.s3.amazonaws.com/';
        }
    }]);
