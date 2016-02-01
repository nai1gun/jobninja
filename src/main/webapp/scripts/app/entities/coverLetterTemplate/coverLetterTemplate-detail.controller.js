'use strict';

angular.module('jhtestApp')
    .controller('CoverLetterTemplateDetailController', [
        '$scope', '$rootScope', '$state', 'entity', 'CoverLetterTemplate', 'CoverLetterTemplatePlaceholder',
        function ($scope, $rootScope, $state, entity, CoverLetterTemplate, CoverLetterTemplatePlaceholder) {
        $scope.coverLetterTemplate = entity;
        $scope.$coverLetterTemplate = angular.copy($scope.coverLetterTemplate);
        $scope.editing = $state.$current.data.editing;
        $scope.placeholders = {};

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
            } else {
                CoverLetterTemplate.save($scope.coverLetterTemplate, onSaveFinished);
            }
        };
        $scope.putPlaceholder = function(placeholder) {
            if (placeholder && $scope.coverLetterTemplate && $scope.coverLetterTemplate.text) {
                $rootScope.$broadcast('insertText', '[' + placeholder.placeholder + ']');
            }
        };
        var onUpdateFinished = function(result) {
            $scope.$emit('jhtestApp:coverLetterTemplateUpdate', result);
            $state.go('coverLetterTemplate.detail');
        };
        var onSaveFinished = function(result) {
            $scope.$emit('jhtestApp:coverLetterTemplateUpdate', result);
            $state.go('coverLetterTemplate');
        };

        var loadPlaceholders = function() {
            CoverLetterTemplatePlaceholder.getAll().then(function(result) {
                $scope.placeholders = result;
            });
        };

        loadPlaceholders();
    }]);
