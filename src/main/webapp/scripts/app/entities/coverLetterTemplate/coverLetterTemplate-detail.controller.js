'use strict';

angular.module('jhtestApp')
    .controller('CoverLetterTemplateDetailController', [
        '$scope', '$rootScope', '$state', 'entity', 'CoverLetterTemplate',
        function ($scope, $rootScope, $state, entity, CoverLetterTemplate) {
        $scope.coverLetterTemplate = entity;
        $scope.$coverLetterTemplate = angular.copy($scope.coverLetterTemplate);
        $scope.editing = $state.$current.data.editing;

        $scope.load = function (id) {
            CoverLetterTemplate.get({id: id}, function(result) {
                $scope.coverLetterTemplate = result;
            });
        };
        $rootScope.$on('jhtestApp:coverLetterTemplateUpdate', function(event, result) {
            $scope.coverLetterTemplate = result;
        });
    }]);
