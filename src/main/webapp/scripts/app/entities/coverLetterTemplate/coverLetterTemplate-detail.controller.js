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
        $scope.editStart = function() {
            $scope.$coverLetterTemplate = angular.copy($scope.coverLetterTemplate);
            $scope.editing = true;
            $state.go('coverLetterTemplate.edit');
        };
        $scope.editCancel = function() {
            $scope.coverLetterTemplate = angular.copy($scope.$coverLetterTemplate);
            $scope.editing = false;
            if ($scope.coverLetterTemplate.id != null) {
                $state.go('coverLetterTemplate.detail');
            } else {
                $state.go('coverLetterTemplate');
            }
        };
        $scope.editDone = function() {
            $scope.editing = false;
            if ($scope.coverLetterTemplate.id != null) {
                CoverLetterTemplate.update($scope.coverLetterTemplate, onUpdateFinished);
                $state.go('coverLetterTemplate.detail');
            } else {
                CoverLetterTemplate.save($scope.coverLetterTemplate, onSaveFinished);
                $state.go('coverLetterTemplate');
            }
        };
        var onUpdateFinished = function (result) {
            $scope.$emit('jhtestApp:coverLetterTemplateUpdate', result);
        };
        var onSaveFinished = function (result) {
            $scope.$emit('jhtestApp:coverLetterTemplateUpdate', result);
        };
    }]);
