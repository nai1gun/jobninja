'use strict';

angular.module('jhtestApp')
    .controller('CoverLetterTemplateDetailController', function ($scope, $rootScope, $stateParams, entity, CoverLetterTemplate) {
        $scope.coverLetterTemplate = entity;
        $scope.load = function (id) {
            CoverLetterTemplate.get({id: id}, function(result) {
                $scope.coverLetterTemplate = result;
            });
        };
        $rootScope.$on('jhtestApp:coverLetterTemplateUpdate', function(event, result) {
            $scope.coverLetterTemplate = result;
        });
    });
