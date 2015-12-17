'use strict';

angular.module('jhtestApp')
    .controller('CoverLetterTemplateController', function ($scope, CoverLetterTemplate) {
        $scope.coverLetterTemplates = [];
        $scope.loadAll = function() {
            CoverLetterTemplate.query(function(result) {
               $scope.coverLetterTemplates = result;
            });
        };
        $scope.loadAll();

        $scope.delete = function (id) {
            CoverLetterTemplate.get({id: id}, function(result) {
                $scope.coverLetterTemplate = result;
                $('#deleteCoverLetterTemplateConfirmation').modal('show');
            });
        };

        $scope.confirmDelete = function (id) {
            CoverLetterTemplate.delete({id: id},
                function () {
                    $scope.loadAll();
                    $('#deleteCoverLetterTemplateConfirmation').modal('hide');
                    $scope.clear();
                });
        };

        $scope.refresh = function () {
            $scope.loadAll();
            $scope.clear();
        };

        $scope.clear = function () {
            $scope.coverLetterTemplate = {name: null, text: null, id: null};
        };

        $scope.textPreview = function(text) {
            if (!text || text.length < 52) {
                return text;
            } else {
                return text.substring(0, 50) + '...';
            }
        };
    });
