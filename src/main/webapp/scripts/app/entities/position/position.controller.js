'use strict';

angular.module('jhtestApp')
    .controller('PositionController', ['$scope', 'Position', 'ParseLinks', function ($scope, Position, ParseLinks) {
        $scope.positions = [];
        $scope.page = 1;
        $scope.order = {
            sort: 'edited',
            asc: false
        };
        $scope.loadAll = function() {
            Position.query({page: $scope.page, per_page: 20, sort: $scope.order.sort, asc: $scope.order.asc},
                function(result, headers) {
                $scope.links = ParseLinks.parse(headers('link'));
                $scope.positions = result;
            });
        };
        $scope.loadPage = function(page) {
            $scope.page = page;
            $scope.loadAll();
        };

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

        $scope.showPagination = function() {
            return $scope.links && $scope.links.last > 1;
        };

        $scope.changeSort = function(field) {
            $scope.order.asc = $scope.order.sort === field ? !$scope.order.asc : false;
            $scope.order.sort = field;
            $scope.loadAll();
        };

        $scope.getSortClass = function(field) {
            return $scope.order.sort === field ? $scope.order.asc ? 'sort-asc' : 'sort-desc' : '';
        };

        $scope.makeHref = ParseLinks.makeHref;

        $scope.loadAll();
    }]);
