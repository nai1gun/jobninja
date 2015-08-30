'use strict';

angular.module('jhtestApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('position', {
                parent: 'entity',
                url: '/positions',
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
                    pageTitle: 'jhtestApp.position.detail.title'
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
            .state('position.new', {
                parent: 'position',
                url: '/new',
                data: {
                    roles: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/position/position-dialog.html',
                        controller: 'PositionDialogController',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {name: null, link: null, state: null, created: null, edited: null, notes: null, id: null};
                            }
                        }
                    }).result.then(function(result) {
                        $state.go('position', null, { reload: true });
                    }, function() {
                        $state.go('position');
                    })
                }]
            })
            .state('position.edit', {
                parent: 'position',
                url: '/{id}/edit',
                data: {
                    roles: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/position/position-dialog.html',
                        controller: 'PositionDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['Position', function(Position) {
                                return Position.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('position', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            });
    });
