'use strict';

angular.module('jhtestApp')
    .service('DateUtils', function () {
      this.convertLocaleDateToServer = function(date) {
        if (date) {
          return new Date(Date.UTC(date.getFullYear(), date.getMonth(), date.getDate(),
              date.getHours(), date.getMinutes(), date.getSeconds()));
        } else {
          return null;
        }
      };
      this.convertLocaleDateFromServer = function(date) {
        if (date) {
          var dateString = date.split("-");
          return new Date(dateString[0], dateString[1] - 1, dateString[2]);
        }
        return null;
      };
      this.convertDateTimeFromServer = function(date) {
        if (date) {
          return new Date(date);
        } else {
          return null;
        }
      }
    });
