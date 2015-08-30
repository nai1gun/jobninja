'use strict';

angular.module('jhtestApp')
    .controller('PositionController', function ($scope, Position, ParseLinks) {
        $scope.positions = [];
        $scope.page = 1;
        $scope.loadAll = function() {
            Position.query({page: $scope.page, per_page: 20}, function(result, headers) {
                $scope.links = ParseLinks.parse(headers('link'));
                $scope.positions = result;
            });
        };
        $scope.loadPage = function(page) {
            $scope.page = page;
            $scope.loadAll();
        };
        $scope.loadAll();

        $scope.delete = function (id) {
            Position.get({id: id}, function(result) {
                $scope.position = result;
                $('#deletePositionConfirmation').modal('show');
            });
        };

        $scope.confirmDelete = function (id) {
            Position.delete({id: id},
                function () {
                    $scope.loadAll();
                    $('#deletePositionConfirmation').modal('hide');
                    $scope.clear();
                });
        };

        $scope.refresh = function () {
            $scope.loadAll();
            $scope.clear();
        };

        $scope.clear = function () {
            $scope.position = {name: null, link: null, state: null, created: null, edited: null, notes: null, id: null};
        };
    });
