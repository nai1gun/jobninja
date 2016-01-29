'use strict';

angular.module('jhtestApp')
    .service('ColorUtils', function () {
        this.ratingToColor = function(rating, minimum, maximum) {
            if (rating < minimum || rating > maximum) {
                return '#000000';
            }
            var median = (minimum + maximum) / 2;
            var red;
            var green;
            if (rating <= median) {
                red   = 200;
                green = Math.round(200 * (rating - minimum) / (median - minimum));
            } else {
                green = 200;
                red   = Math.round(200 * (maximum - rating) / (maximum - median));
            }
            return '#' + pad0(red.toString(16)) + pad0(green.toString(16)) + '00';
        };

        function pad0(str) {
            return str ? (str.length == 1 ? '0' : '') + str : '00';
        }
    });
