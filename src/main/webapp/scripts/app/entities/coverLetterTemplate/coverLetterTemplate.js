'use strict';

angular.module('jhtestApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('coverLetterTemplate', {
                parent: 'entity',
                url: '/coverLetterTemplates',
                data: {
                    roles: ['ROLE_USER'],
                    pageTitle: 'jhtestApp.coverLetterTemplate.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/coverLetterTemplate/coverLetterTemplates.html',
                        controller: 'CoverLetterTemplateController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('coverLetterTemplate');
                        $translatePartialLoader.addPart('global');
                        return $translate.refresh();
                    }]
                }
            })
            .state('coverLetterTemplate.detail', {
                parent: 'entity',
                url: '/coverLetterTemplate/{id}',
                data: {
                    roles: ['ROLE_USER'],
                    pageTitle: 'jhtestApp.coverLetterTemplate.detail.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/coverLetterTemplate/coverLetterTemplate-detail.html',
                        controller: 'CoverLetterTemplateDetailController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('coverLetterTemplate');
                        return $translate.refresh();
                    }],
                    entity: ['$stateParams', 'CoverLetterTemplate', function($stateParams, CoverLetterTemplate) {
                        return CoverLetterTemplate.get({id : $stateParams.id});
                    }]
                }
            })
            .state('coverLetterTemplate.new', {
                parent: 'coverLetterTemplate',
                url: '/new',
                data: {
                    roles: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/coverLetterTemplate/coverLetterTemplate-dialog.html',
                        controller: 'CoverLetterTemplateDialogController',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {name: null, text: null, id: null};
                            }
                        }
                    }).result.then(function(result) {
                        $state.go('coverLetterTemplate', null, { reload: true });
                    }, function() {
                        $state.go('coverLetterTemplate');
                    })
                }]
            })
            .state('coverLetterTemplate.edit', {
                parent: 'coverLetterTemplate',
                url: '/{id}/edit',
                data: {
                    roles: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/coverLetterTemplate/coverLetterTemplate-dialog.html',
                        controller: 'CoverLetterTemplateDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['CoverLetterTemplate', function(CoverLetterTemplate) {
                                return CoverLetterTemplate.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('coverLetterTemplate', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            });
    });
