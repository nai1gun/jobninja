'use strict';

angular.module('jhtestApp')
    .factory('PositionUtils', ['S3_PREFIX', function(S3_PREFIX) {
        return {
            s3url: function(cv) {
                return S3_PREFIX + cv.filePath;
            },
            fileName: function(path) {
                if (!path) {
                    return '';
                }
                var n = path.lastIndexOf('/');
                if (n == -1) {
                    return path;
                }
                return path.substring(n + 1)
            }
        };
    }]);
