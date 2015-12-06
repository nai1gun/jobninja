'use strict';

angular.module('jhtestApp')
    .factory('PositionUtils', ['Config', function(Config) {
        return {
            s3url: function(cv) {
                return Config.s3Prefix() + cv.filePath;
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
