'use strict';

angular.module('jhtestApp').controller('CoverLetterTemplateDialogController',
    ['$scope', '$stateParams', '$modalInstance', 'entity', 'CoverLetterTemplate',
        function($scope, $stateParams, $modalInstance, entity, CoverLetterTemplate) {

        $scope.coverLetterTemplate = entity;
        $scope.load = function(id) {
            CoverLetterTemplate.get({id : id}, function(result) {
                $scope.coverLetterTemplate = result;
            });
        };

        var onSaveFinished = function (result) {
            $scope.$emit('jhtestApp:coverLetterTemplateUpdate', result);
            $modalInstance.close(result);
        };

        $scope.save = function () {
            if ($scope.coverLetterTemplate.id != null) {
                CoverLetterTemplate.update($scope.coverLetterTemplate, onSaveFinished);
            } else {
                CoverLetterTemplate.save($scope.coverLetterTemplate, onSaveFinished);
            }
        };

        $scope.clear = function() {
            $modalInstance.dismiss('cancel');
        };
}]);
