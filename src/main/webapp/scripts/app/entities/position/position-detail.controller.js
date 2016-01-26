'use strict';

angular.module('jhtestApp')
    .controller('PositionDetailController', ['$scope', '$rootScope', '$stateParams', '$state', '$timeout', 'entity', 'Position', 'PositionState', 'DateUtils', 'PositionUtils', 'CoverLetterTemplate', 'ParseLinks', 'Glassdoor', '$translate',
        function ($scope, $rootScope, $stateParams, $state, $timeout, entity, Position, PositionState, DateUtils, PositionUtils, CoverLetterTemplate, ParseLinks, Glassdoor, $translate) {
        $scope.position = entity;
        $scope.$position = angular.copy($scope.position);
        $scope.editing = $state.$current.data.editing;
        $scope.defaultCoverLetterTemplate = {};
        $scope.coverLetterTemplates = [];
        $scope.glassdoorEntries = {};

        $scope.load = function (id) {
            Position.get({id: id}, function(result) {
                $scope.position = result;
                updatePosition();
            });
        };
        $rootScope.$on('jhtestApp:positionUpdate', function(event, result) {
            $scope.position = result;
            updatePosition();
        });
        $scope.editStart = function() {
            $scope.$position = angular.copy($scope.position);
            $scope.editing = true;
            $state.go('position.edit');
        };
        $scope.editCancel = function() {
            $scope.position = angular.copy($scope.$position);
            updatePosition();
            $scope.editing = false;
            $state.go('position.detail');
        };
        $scope.editDone = function() {
            $scope.editing = false;
            $scope.position.edited = DateUtils.convertLocaleDateToServer(new Date());
            Position.update($scope.position, onSaveFinished);
            $scope.$position = angular.copy($scope.position);
            $state.go('position.detail');
        };
        $scope.addCoverLetter = function(coverLetterTemplate) {
            if (!$scope.editing) {
                $scope.editStart();
            }
            if (coverLetterTemplate) {
                $scope.position.coverLetter = coverLetterTemplate.text;
            } else {
                $scope.position.coverLetter = '';
            }
        };
        $scope.hasCoverLetter = function() {
            return $scope.position.coverLetter != undefined && $scope.position.coverLetter != null;
        };

        $scope.editCV = function() {
            if (!$scope.editing) {
                $scope.editStart();
            }
            $state.go('position.detail.cv');
        };

        $scope.removeCv = function() {
            $scope.position.cv = null;
        };

        $scope.hasCV = function() {
            return $scope.position.cv != undefined && $scope.position.cv != null;
        };

        var glassdoorPending = false;

        $scope.$watch('$position.company', function(company) {
           if (company && $scope.glassdoorEntries[company] === undefined && !glassdoorPending) {
               glassdoorPending = true;
               Glassdoor.get({employerName: company}).$promise.then(function(result) {
                   $scope.glassdoorEntries[company] = result.toJSON();
                   glassdoorPending = false;
               });
           }
        });

        $scope.s3url = PositionUtils.s3url;

        $scope.fileName = PositionUtils.fileName;

        $scope.makeHref = ParseLinks.makeHref;

        var onSaveFinished = function (result) {
            $scope.$emit('jhtestApp:positionUpdate', result);
        };

        var updatePosition = function() {
            $state.$current.data.position = $scope.position;
        };

        PositionState.getAll(function(states) {
            $scope.states = states;
        });

        $translate('jhtestApp.coverLetterTemplate.empty').then(function(emptyTemplate){
            $scope.defaultCoverLetterTemplate = {
                name: emptyTemplate,
                text: ''
            };
        });

        CoverLetterTemplate.query(function(coverLetterTemplates) {
            if (angular.isArray(coverLetterTemplates)) {
                $scope.coverLetterTemplates =
                    $scope.coverLetterTemplates.concat(coverLetterTemplates);
            }
        });

        updatePosition();

    }]);
