'use strict';

angular.module('jhtestApp')
    .config(['$stateProvider', function ($stateProvider) {
        $stateProvider
            .state('position', {
                parent: 'entity',
                url: '/',
                data: {
                    roles: ['ROLE_USER'],
                    pageTitle: 'jhtestApp.position.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/position/positions.html',
                        controller: 'PositionController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('position');
                        $translatePartialLoader.addPart('global');
                        return $translate.refresh();
                    }]
                }
            })
            .state('position.detail', {
                parent: 'entity',
                url: '/position/{id}',
                data: {
                    roles: ['ROLE_USER'],
                    pageTitle: 'jhtestApp.position.detail.title',
                    editing: false
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/position/position-detail.html',
                        controller: 'PositionDetailController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('position');
                        return $translate.refresh();
                    }],
                    entity: ['$stateParams', 'Position', function($stateParams, Position) {
                        return Position.get({id : $stateParams.id});
                    }]
                }
            })
            .state('position.edit', {
                parent: 'position.detail',
                url: '/edit',
                data: {
                    roles: ['ROLE_USER'],
                    editing: true
                }
            })
            .state('position.detail.cv', {
                parent: 'position.detail',
                url: '/cv',
                data: {
                    roles: ['ROLE_USER'],
                    editing: true
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/position/position-cv-dialog.html',
                        controller: 'PositionCvDialogController',
                        size: 'lg'/*,
                        resolve: {
                            entity: ['Position', function(Position) {
                                return Position.get({id : $stateParams.id});
                            }]
                        }*/
                    }).result.then(function(result) {
                        $state.go('^', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            })
            .state('position.new', {
                parent: 'position',
                url: '/new',
                data: {
                    roles: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', 'PositionState', function($stateParams, $state, $modal, PositionState) {
                    PositionState.getAll(function(states) {
                        $modal.open({
                            templateUrl: 'scripts/app/entities/position/position-dialog.html',
                            controller: 'PositionDialogController',
                            size: 'lg',
                            resolve: {
                                entity: function () {
                                    return {
                                        name: null,
                                        link: null,
                                        state: states && states.length ? states[0] : null,
                                        created: null,
                                        edited: null,
                                        notes: null,
                                        id: null
                                    };
                                }
                            }
                        }).result.then(function (result) {
                            $state.go('position', null, {reload: true});
                        }, function () {
                            $state.go('position');
                        });
                    });
                }]
            });
    }]);
