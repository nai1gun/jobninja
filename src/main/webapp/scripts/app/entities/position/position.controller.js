'use strict';

angular.module('jhtestApp')
    .controller('PositionController', function ($scope, Position) {
        $scope.positions = [];
        $scope.loadAll = function() {
            Position.query(function(result) {
               $scope.positions = result;
            });
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
            $scope.position = {title: null, link: null, state: null, id: null};
        };
    });
